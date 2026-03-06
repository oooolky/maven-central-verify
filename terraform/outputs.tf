# Terraform Outputs
# To display these values, use command "terraform apply" 
# To retrieve these values, use command "terraform output"

# ALB Outputs
output "alb_dns_name" {
  description = "DNS name of the Application Load Balancer (use this for all client requests)"
  value       = aws_lb.main.dns_name
}

output "product_service_good_url" {
  description = "URL to reach the Product Service good instance group through the ALB"
  value       = "http://${aws_lb.main.dns_name}/product"
}

output "shopping_cart_service_url" {
  description = "URL to reach the Shopping Cart Service through the ALB"
  value       = "http://${aws_lb.main.dns_name}/shopping-cart"
}

output "credit_card_service_url" {
  description = "URL to reach the Credit Card Authorizer through the ALB"
  value       = "http://${aws_lb.main.dns_name}/credit-card"
}

# ECR Repository URLs
output "ecr_product_service_good_url" {
  description = "ECR repository URL for the Product Service good image"
  value       = aws_ecr_repository.product_service_good.repository_url
}

output "ecr_product_service_bad_url" {
  description = "ECR repository URL for the 'bad' Product Service image"
  value       = aws_ecr_repository.product_service_bad.repository_url
}

output "ecr_shopping_cart_service_url" {
  description = "ECR repository URL for the Shopping Cart Service image"
  value       = aws_ecr_repository.shopping_cart_service.repository_url
}

output "ecr_credit_card_service_url" {
  description = "ECR repository URL for the Credit Card Authorizer image"
  value       = aws_ecr_repository.credit_card_service.repository_url
}

output "ecr_warehouse_consumer_url" {
  description = "ECR repository URL for the Warehouse Consumer image"
  value       = aws_ecr_repository.warehouse_consumer.repository_url
}

# Cluster Information
output "ecs_cluster_name" {
  description = "Name of the ECS cluster"
  value       = aws_ecs_cluster.main.name
}

# Helper Commands
output "ecr_login_command" {
  description = "Command to authenticate Docker with ECR"
  value       = "aws ecr get-login-password --region ${var.aws_region} | docker login --username AWS --password-stdin $(aws sts get-caller-identity --query Account --output text).dkr.ecr.${var.aws_region}.amazonaws.com"
}

output "test_product_endpoint" {
  description = "curl command to test the Product endpoint through ALB"
  value       = "curl -X POST http://${aws_lb.main.dns_name}/product -H 'Content-Type: application/json' -d '{\"name\":\"TestProduct\",\"price\":9.99}'"
}

output "test_checkout_endpoint" {
  description = "curl command to test the checkout endpoint through ALB"
  value       = "curl -X POST http://${aws_lb.main.dns_name}/shopping-cart/checkout -H 'Content-Type: application/json' -d '{\"shoppingCartId\":1,\"creditCard\":\"1234-5678-9012-3456\"}'"
}
