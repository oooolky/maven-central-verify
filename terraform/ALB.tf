# Application Load Balancer (ALB)

# =============================================================================
# ALB
# =============================================================================

resource "aws_lb" "main" {
  name               = "${var.project_name}-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb.id]
  subnets            = data.aws_subnets.default.ids

  tags = {
    Name    = "${var.project_name}-alb"
    Project = var.project_name
  }
}

# =============================================================================
# Target Groups
# =============================================================================
# Each microservice gets its own target group
# Health checks use a dedicated /health endpoint
# "bad" Product instance is registered in the SAME target group as
# the good ones — ATW handles the traffic shifting

resource "aws_lb_target_group" "product" {
  name        = "${var.project_name}-product-tg"
  port        = var.container_port
  protocol    = "HTTP"
  vpc_id      = data.aws_vpc.default.id
  target_type = "ip"

  # Enable ATW-compatible balancing
  load_balancing_algorithm_type     = "weighted_random"
  load_balancing_anomaly_mitigation = "on"

  health_check {
    enabled             = true
    path                = var.health_check_path
    port                = "traffic-port"
    protocol            = "HTTP"
    matcher             = "200"
    interval            = var.health_check_interval
    timeout             = 5
    healthy_threshold   = var.health_check_healthy_threshold
    unhealthy_threshold = var.health_check_unhealthy_threshold
  }

  tags = {
    Name    = "${var.project_name}-product-tg"
    Project = var.project_name
  }
}

resource "aws_lb_target_group" "shopping_cart" {
  name        = "${var.project_name}-cart-tg"
  port        = var.container_port
  protocol    = "HTTP"
  vpc_id      = data.aws_vpc.default.id
  target_type = "ip"

  # Enable ATW-compatible balancing
  load_balancing_algorithm_type     = "weighted_random"
  load_balancing_anomaly_mitigation = "on"

  health_check {
    enabled             = true
    path                = var.health_check_path
    port                = "traffic-port"
    protocol            = "HTTP"
    matcher             = "200"
    interval            = var.health_check_interval
    timeout             = 5
    healthy_threshold   = var.health_check_healthy_threshold
    unhealthy_threshold = var.health_check_unhealthy_threshold
  }

  tags = {
    Name    = "${var.project_name}-cart-tg"
    Project = var.project_name
  }
}

resource "aws_lb_target_group" "credit_card" {
  name        = "${var.project_name}-cca-tg"
  port        = var.container_port
  protocol    = "HTTP"
  vpc_id      = data.aws_vpc.default.id
  target_type = "ip"

  # Enable ATW-compatible balancing
  load_balancing_algorithm_type     = "weighted_random"
  load_balancing_anomaly_mitigation = "on"

  health_check {
    enabled             = true
    path                = var.health_check_path
    port                = "traffic-port"
    protocol            = "HTTP"
    matcher             = "200"
    interval            = var.health_check_interval
    timeout             = 5
    healthy_threshold   = var.health_check_healthy_threshold
    unhealthy_threshold = var.health_check_unhealthy_threshold
  }

  tags = {
    Name    = "${var.project_name}-cca-tg"
    Project = var.project_name
  }
}

# =============================================================================
# ALB Listener & Path-Based Routing Rules
# =============================================================================

resource "aws_lb_listener" "http" {
  load_balancer_arn = aws_lb.main.arn
  port              = var.alb_listener_port
  protocol          = "HTTP"

  # Default action: return 404 for unmatched paths
  default_action {
    type = "fixed-response"
    fixed_response {
      content_type = "text/plain"
      message_body = "No matching route found"
      status_code  = "404"
    }
  }

  tags = {
    Name    = "${var.project_name}-http-listener"
    Project = var.project_name
  }
}

# Route /product* to Product target group
resource "aws_lb_listener_rule" "product_routing" {
  listener_arn = aws_lb_listener.http.arn
  priority     = 100

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.product.arn
  }

  condition {
    path_pattern {
      values = ["/product*"]
    }
  }

  tags = {
    Name    = "${var.project_name}-product-rule"
    Project = var.project_name
  }
}

# Route /shopping-cart* to ShoppingCart target group
resource "aws_lb_listener_rule" "shopping_cart_routing" {
  listener_arn = aws_lb_listener.http.arn
  priority     = 200

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.shopping_cart.arn
  }

  condition {
    path_pattern {
      values = ["/shopping-cart*"]
    }
  }

  tags = {
    Name    = "${var.project_name}-cart-rule"
    Project = var.project_name
  }
}

# Route /credit-card* to CreditCard Authorizer target group
resource "aws_lb_listener_rule" "credit_card_routing" {
  listener_arn = aws_lb_listener.http.arn
  priority     = 300

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.credit_card.arn
  }

  condition {
    path_pattern {
      values = ["/credit-card*"]
    }
  }

  tags = {
    Name    = "${var.project_name}-cca-rule"
    Project = var.project_name
  }
}
