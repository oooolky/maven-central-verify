# ECR Repo
#
# One repo per microservice image
# Team members push their built Docker images here
# ECS task definitions pull from these URIs

resource "aws_ecr_repository" "product_service_good" {
  name                 = "${var.project_name}/product-service-good"
  image_tag_mutability = "MUTABLE"
  force_delete         = true

  image_scanning_configuration {
    scan_on_push = false
  }

  tags = {
    Name    = "${var.project_name}-product-service-good"
    Project = var.project_name
  }
}

resource "aws_ecr_repository" "product_service_bad" {
  name                 = "${var.project_name}/product-service-bad"
  image_tag_mutability = "MUTABLE"
  force_delete         = true

  image_scanning_configuration {
    scan_on_push = false
  }

  tags = {
    Name    = "${var.project_name}-product-service-bad"
    Project = var.project_name
  }
}

resource "aws_ecr_repository" "shopping_cart_service" {
  name                 = "${var.project_name}/shopping-cart-service"
  image_tag_mutability = "MUTABLE"
  force_delete         = true

  image_scanning_configuration {
    scan_on_push = false
  }

  tags = {
    Name    = "${var.project_name}-shopping-cart-service"
    Project = var.project_name
  }
}

resource "aws_ecr_repository" "credit_card_service" {
  name                 = "${var.project_name}/credit-card-service"
  image_tag_mutability = "MUTABLE"
  force_delete         = true

  image_scanning_configuration {
    scan_on_push = false
  }

  tags = {
    Name    = "${var.project_name}-credit-card-service"
    Project = var.project_name
  }
}

resource "aws_ecr_repository" "warehouse_consumer" {
  name                 = "${var.project_name}/warehouse-consumer"
  image_tag_mutability = "MUTABLE"
  force_delete         = true

  image_scanning_configuration {
    scan_on_push = false
  }

  tags = {
    Name    = "${var.project_name}-warehouse-consumer"
    Project = var.project_name
  }
}
