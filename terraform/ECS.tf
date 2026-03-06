# ECS Cluster, Task Definitions & Services
# Services deployed:
#   1. product-service-good   (behind ALB, 2 instances)
#   2. product-service-bad    (behind ALB, 1 instance, 503 50% of time)
#   3. shopping-cart-service  (behind ALB, connects to RabbitMQ & CCA)
#   4. credit-card-service    (behind ALB)
#   5. rabbitmq               (standalone, Service Connect or DNS discovery)
#   6. warehouse-consumer     (standalone, consumes from RabbitMQ)

# =============================================================================
# ECS Cluster
# =============================================================================

resource "aws_ecs_cluster" "main" {
  name = "${var.project_name}-cluster"

  setting {
    name  = "containerInsights"
    value = "enabled"
  }

  tags = {
    Name    = "${var.project_name}-cluster"
    Project = var.project_name
  }
}

# =============================================================================
# CloudWatch Log Groups (one per service for clean separation)
# =============================================================================

resource "aws_cloudwatch_log_group" "product_service_good" {
  name              = "/ecs/${var.project_name}/product-service-good"
  retention_in_days = var.log_retention_days

  tags = { Project = var.project_name }
}

resource "aws_cloudwatch_log_group" "product_service_bad" {
  name              = "/ecs/${var.project_name}/product-service-bad"
  retention_in_days = var.log_retention_days

  tags = { Project = var.project_name }
}

resource "aws_cloudwatch_log_group" "shopping_cart_service" {
  name              = "/ecs/${var.project_name}/shopping-cart-service"
  retention_in_days = var.log_retention_days

  tags = { Project = var.project_name }
}

resource "aws_cloudwatch_log_group" "credit_card_service" {
  name              = "/ecs/${var.project_name}/credit-card-service"
  retention_in_days = var.log_retention_days

  tags = { Project = var.project_name }
}

resource "aws_cloudwatch_log_group" "rabbitmq" {
  name              = "/ecs/${var.project_name}/rabbitmq"
  retention_in_days = var.log_retention_days

  tags = { Project = var.project_name }
}

resource "aws_cloudwatch_log_group" "warehouse_consumer" {
  name              = "/ecs/${var.project_name}/warehouse-consumer"
  retention_in_days = var.log_retention_days

  tags = { Project = var.project_name }
}

# =============================================================================
# Task Definitions
#
# Each task definition specifies the Docker image, resource limits, port
# mappings, environment variables, and logging configuration for one service.
# =============================================================================

# =============================================================================
# Product Service, Good
# =============================================================================

resource "aws_ecs_task_definition" "product_service_good" {
  family                   = "${var.project_name}-product-service-good"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = var.ecs_task_cpu
  memory                   = var.ecs_task_memory
  execution_role_arn       = data.aws_iam_role.lab_role.arn
  task_role_arn            = data.aws_iam_role.lab_role.arn

  container_definitions = jsonencode([
    {
      name      = "product-service-good"
      image     = "${aws_ecr_repository.product_service_good.repository_url}:latest"
      essential = true

      portMappings = [
        {
          containerPort = var.container_port
          protocol      = "tcp"
        }
      ]

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.product_service_good.name
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "product"
        }
      }
    }
  ])

  tags = {
    Name    = "${var.project_name}-product-service-good"
    Project = var.project_name
  }
}

# =============================================================================
# Product Service, Bad
# Returns 503 for 50% of requests
# The /health endpoint always returns 200 so ALB keeps it in service,
# allowing ATW to demonstrate traffic-shifting behaviour
# =============================================================================

resource "aws_ecs_task_definition" "product_service_bad" {
  family                   = "${var.project_name}-product-service-bad"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = var.ecs_task_cpu
  memory                   = var.ecs_task_memory
  execution_role_arn       = data.aws_iam_role.lab_role.arn
  task_role_arn            = data.aws_iam_role.lab_role.arn

  container_definitions = jsonencode([
    {
      name      = "product-service-bad"
      image     = "${aws_ecr_repository.product_service_bad.repository_url}:latest"
      essential = true

      portMappings = [
        {
          containerPort = var.container_port
          protocol      = "tcp"
        }
      ]

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.product_service_bad.name
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "product-bad"
        }
      }
    }
  ])

  tags = {
    Name    = "${var.project_name}-product-service-bad"
    Project = var.project_name
  }
}

# =============================================================================
# Shopping Cart Service
# =============================================================================
# Needs to know: ALB DNS (to call CCA), RabbitMQ host (to publish messages)

resource "aws_ecs_task_definition" "shopping_cart_service" {
  family                   = "${var.project_name}-shopping-cart-service"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = var.ecs_task_cpu
  memory                   = var.ecs_task_memory
  execution_role_arn       = data.aws_iam_role.lab_role.arn
  task_role_arn            = data.aws_iam_role.lab_role.arn

  container_definitions = jsonencode([
    {
      name      = "shopping-cart-service"
      image     = "${aws_ecr_repository.shopping_cart_service.repository_url}:latest"
      essential = true

      portMappings = [
        {
          containerPort = var.container_port
          protocol      = "tcp"
        }
      ]

      environment = [
        {
          name  = "SPRING_RABBITMQ_HOST"
          value = var.rabbitmq_host
        },
        {
          name  = "SPRING_RABBITMQ_PORT"
          value = tostring(var.rabbitmq_port)
        },
        {
          name  = "SPRING_RABBITMQ_USERNAME"
          value = var.rabbitmq_default_user
        },
        {
          name  = "SPRING_RABBITMQ_PASSWORD"
          value = var.rabbitmq_default_pass
        },
        {
          name  = "SHIP_QUEUE"
          value = var.ship_queue
        },
        {
          name  = "RMQ_CONFIRM_TIMEOUT_MS"
          value = tostring(var.rmq_confirm_timeout_ms)
        },
        {
          name  = "CCA_BASE_URL"
          value = "http://${aws_lb.main.dns_name}"
        }
      ]

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.shopping_cart_service.name
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "cart"
        }
      }
    }
  ])

  tags = {
    Name    = "${var.project_name}-shopping-cart-service"
    Project = var.project_name
  }
}

# =============================================================================
# Credit Card Authorizer (CCA)
# =============================================================================

resource "aws_ecs_task_definition" "credit_card_service" {
  family                   = "${var.project_name}-credit-card-service"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = var.ecs_task_cpu
  memory                   = var.ecs_task_memory
  execution_role_arn       = data.aws_iam_role.lab_role.arn
  task_role_arn            = data.aws_iam_role.lab_role.arn

  container_definitions = jsonencode([
    {
      name      = "credit-card-service"
      image     = "${aws_ecr_repository.credit_card_service.repository_url}:latest"
      essential = true

      portMappings = [
        {
          containerPort = var.container_port
          protocol      = "tcp"
        }
      ]

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.credit_card_service.name
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "cca"
        }
      }
    }
  ])

  tags = {
    Name    = "${var.project_name}-credit-card-service"
    Project = var.project_name
  }
}

# =============================================================================
# RabbitMQ Broker
# =============================================================================
# Uses the official RabbitMQ image with management plugin enabled
# Deployed as a single task with a known private IP for service discovery

resource "aws_ecs_task_definition" "rabbitmq" {
  family                   = "${var.project_name}-rabbitmq"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = var.rabbitmq_task_cpu
  memory                   = var.rabbitmq_task_memory
  execution_role_arn       = data.aws_iam_role.lab_role.arn
  task_role_arn            = data.aws_iam_role.lab_role.arn

  container_definitions = jsonencode([
    {
      name      = "rabbitmq"
      image     = "rabbitmq:3-management"
      essential = true

      portMappings = [
        {
          containerPort = var.rabbitmq_port
          protocol      = "tcp"
        },
        {
          containerPort = var.rabbitmq_management_port
          protocol      = "tcp"
        }
      ]

      environment = [
        {
          name  = "RABBITMQ_DEFAULT_USER"
          value = var.rabbitmq_default_user
        },
        {
          name  = "RABBITMQ_DEFAULT_PASS"
          value = var.rabbitmq_default_pass
        }
      ]

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.rabbitmq.name
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "rabbitmq"
        }
      }
    }
  ])

  tags = {
    Name    = "${var.project_name}-rabbitmq"
    Project = var.project_name
  }
}

# =============================================================================
# Warehouse Consumer
# =============================================================================
# Standalone consumer (not behind ALB)
# Connects to RabbitMQ to consume order messages with manual acknowledgements

resource "aws_ecs_task_definition" "warehouse_consumer" {
  family                   = "${var.project_name}-warehouse-consumer"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = var.ecs_task_cpu
  memory                   = var.ecs_task_memory
  execution_role_arn       = data.aws_iam_role.lab_role.arn
  task_role_arn            = data.aws_iam_role.lab_role.arn

  container_definitions = jsonencode([
    {
      name      = "warehouse-consumer"
      image     = "${aws_ecr_repository.warehouse_consumer.repository_url}:latest"
      essential = true

      environment = [
        {
          name  = "SPRING_RABBITMQ_HOST"
          value = var.rabbitmq_host
        },
        {
          name  = "SPRING_RABBITMQ_PORT"
          value = tostring(var.rabbitmq_port)
        },
        {
          name  = "SPRING_RABBITMQ_USERNAME"
          value = var.rabbitmq_default_user
        },
        {
          name  = "SPRING_RABBITMQ_PASSWORD"
          value = var.rabbitmq_default_pass
        },
        {
          name  = "SHIP_QUEUE"
          value = var.ship_queue
        },
        {
          name  = "CONSUMER_CONCURRENCY"
          value = tostring(var.consumer_concurrency)
        }
      ]

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.warehouse_consumer.name
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "warehouse"
        }
      }
    }
  ])

  tags = {
    Name    = "${var.project_name}-warehouse-consumer"
    Project = var.project_name
  }
}

# =============================================================================
# ECS Services
# =============================================================================

# =============================================================================
# Product Service, Good
# Registered with ALB
# =============================================================================

resource "aws_ecs_service" "product_service_good" {
  name            = "${var.project_name}-product-service-good"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.product_service_good.arn
  desired_count   = var.product_service_good_count
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = data.aws_subnets.default.ids
    security_groups  = [aws_security_group.ecs_services.id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.product.arn
    container_name   = "product-service-good"
    container_port   = var.container_port
  }

  depends_on = [aws_lb_listener.http]

  tags = {
    Name    = "${var.project_name}-product-service-good"
    Project = var.project_name
  }
}

# =============================================================================
# Product Service, Bad
# Same target group, ATW will reduce its traffic
# =============================================================================

resource "aws_ecs_service" "product_service_bad" {
  name            = "${var.project_name}-product-service-bad"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.product_service_bad.arn
  desired_count   = var.product_service_bad_count
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = data.aws_subnets.default.ids
    security_groups  = [aws_security_group.ecs_services.id]
    assign_public_ip = true
  }

  # Registered in the SAME target group as the good product service
  load_balancer {
    target_group_arn = aws_lb_target_group.product.arn
    container_name   = "product-service-bad"
    container_port   = var.container_port
  }

  depends_on = [aws_lb_listener.http]

  tags = {
    Name    = "${var.project_name}-product-service-bad"
    Project = var.project_name
  }
}

# =============================================================================
# Shopping Cart Service, registered with ALB
# =============================================================================

resource "aws_ecs_service" "shopping_cart_service" {
  name            = "${var.project_name}-shopping-cart-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.shopping_cart_service.arn
  desired_count   = var.shopping_cart_service_count
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = data.aws_subnets.default.ids
    security_groups  = [aws_security_group.ecs_services.id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.shopping_cart.arn
    container_name   = "shopping-cart-service"
    container_port   = var.container_port
  }

  depends_on = [aws_lb_listener.http]

  tags = {
    Name    = "${var.project_name}-shopping-cart-service"
    Project = var.project_name
  }
}

# =============================================================================
# Credit Card Authorizer, registered with ALB
# =============================================================================

resource "aws_ecs_service" "credit_card_service" {
  name            = "${var.project_name}-credit-card-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.credit_card_service.arn
  desired_count   = var.credit_card_service_count
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = data.aws_subnets.default.ids
    security_groups  = [aws_security_group.ecs_services.id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.credit_card.arn
    container_name   = "credit-card-service"
    container_port   = var.container_port
  }

  depends_on = [aws_lb_listener.http]

  tags = {
    Name    = "${var.project_name}-credit-card-service"
    Project = var.project_name
  }
}

# =============================================================================
# RabbitMQ Broker
# Standalone, not behind ALB
# =============================================================================

resource "aws_ecs_service" "rabbitmq" {
  name            = "${var.project_name}-rabbitmq"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.rabbitmq.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = data.aws_subnets.default.ids
    security_groups  = [aws_security_group.rabbitmq.id]
    assign_public_ip = true
  }

  tags = {
    Name    = "${var.project_name}-rabbitmq"
    Project = var.project_name
  }
}

# =============================================================================
# Warehouse Consumer
# Standalone, not behind ALB
# =============================================================================

resource "aws_ecs_service" "warehouse_consumer" {
  name            = "${var.project_name}-warehouse-consumer"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.warehouse_consumer.arn
  desired_count   = var.warehouse_consumer_count
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = data.aws_subnets.default.ids
    security_groups  = [aws_security_group.ecs_services.id]
    assign_public_ip = true
  }

  # Warehouse depends on RabbitMQ being available
  depends_on = [aws_ecs_service.rabbitmq]

  tags = {
    Name    = "${var.project_name}-warehouse-consumer"
    Project = var.project_name
  }
}
