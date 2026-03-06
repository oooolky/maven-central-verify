# Network & Security Groups
#
# Three security groups with least-privilege access:
#   1. ALB - accepts inbound HTTP from anywhere
#   2. ECS Services - accepts traffic only from the ALB
#   3. RabbitMQ - accepts AMQP from ECS, management UI from anywhere

# ALB Security Group

resource "aws_security_group" "alb" {
  name        = "${var.project_name}-alb-sg"
  description = "Allow inbound HTTP to the Application Load Balancer"
  vpc_id      = data.aws_vpc.default.id

  ingress {
    description = "HTTP from anywhere"
    from_port   = var.alb_listener_port
    to_port     = var.alb_listener_port
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "Allow all outbound"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name    = "${var.project_name}-alb-sg"
    Project = var.project_name
  }
}

# ECS Services Security Group

resource "aws_security_group" "ecs_services" {
  name        = "${var.project_name}-ecs-sg"
  description = "Allow traffic from ALB to ECS microservices"
  vpc_id      = data.aws_vpc.default.id

  # Accept traffic from ALB on the application port
  ingress {
    description     = "Traffic from ALB"
    from_port       = var.container_port
    to_port         = var.container_port
    protocol        = "tcp"
    security_groups = [aws_security_group.alb.id]
  }

  # Allow inter-service communication within this security group.
  # The SCS needs to call the CCA directly via the ALB, so this rule
  # ensures ECS tasks can reach the ALB and other services.
  ingress {
    description = "Inter-service communication"
    from_port   = var.container_port
    to_port     = var.container_port
    protocol    = "tcp"
    self        = true
  }

  egress {
    description = "Allow all outbound"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name    = "${var.project_name}-ecs-sg"
    Project = var.project_name
  }
}

# RabbitMQ Security Group

resource "aws_security_group" "rabbitmq" {
  name        = "${var.project_name}-rabbitmq-sg"
  description = "Allow AMQP from ECS services and management UI access"
  vpc_id      = data.aws_vpc.default.id

  # AMQP port: only accessible from ECS services
  ingress {
    description     = "AMQP from ECS services"
    from_port       = var.rabbitmq_port
    to_port         = var.rabbitmq_port
    protocol        = "tcp"
    security_groups = [aws_security_group.ecs_services.id]
  }

  # Management UI: accessible from anywhere for monitoring during load tests
  ingress {
    description = "RabbitMQ management console"
    from_port   = var.rabbitmq_management_port
    to_port     = var.rabbitmq_management_port
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "Allow all outbound"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name    = "${var.project_name}-rabbitmq-sg"
    Project = var.project_name
  }
}
