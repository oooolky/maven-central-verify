# Terraform Variables
# General Settings

variable "aws_region" {
  description = "AWS regions"
  type        = string
  default     = "us-west-2"
}

variable "project_name" {
  description = "Project name used as prefix for resource naming"
  type        = string
  default     = "cs6650-assignment03"
}

# Networking

variable "container_port" {
  description = "Port that all Spring Boot microservices listen on"
  type        = number
  default     = 8080
}

variable "alb_listener_port" {
  description = "Port that the Application Load Balancer listens on"
  type        = number
  default     = 80
}

# ECS Cluster & Compute
# Assign more resource if needed

variable "ecs_task_cpu" {
  description = "CPU units for each microservice ECS task (1024 = 1 vCPU)"
  type        = string
  default     = "512"
}

variable "ecs_task_memory" {
  description = "Memory in MB for each microservice ECS task"
  type        = string
  default     = "1024"
}

variable "rabbitmq_task_cpu" {
  description = "CPU units for the RabbitMQ broker task"
  type        = string
  default     = "1024"
}

variable "rabbitmq_task_memory" {
  description = "Memory in MB for the RabbitMQ broker task"
  type        = string
  default     = "2048"
}

# Service Instance Counts

variable "product_service_good_count" {
  description = "Number of healthy Product Service instances"
  type        = number
  default     = 2
}

variable "product_service_bad_count" {
  description = "Number of 'bad' Product Service instances (returns 503 50% of time)"
  type        = number
  default     = 1
}

variable "shopping_cart_service_count" {
  description = "Number of ShoppingCart Service instances"
  type        = number
  default     = 2
}

variable "credit_card_service_count" {
  description = "Number of CreditCard Authorizer instances"
  type        = number
  default     = 1
}

variable "warehouse_consumer_count" {
  description = "Number of Warehouse consumer instances"
  type        = number
  default     = 1
}

# Health Check Configuration

variable "health_check_path" {
  description = "Default health check endpoint path for microservices"
  type        = string
  default     = "/health"
}

variable "health_check_interval" {
  description = "Seconds between health checks"
  type        = number
  default     = 30
}

variable "health_check_healthy_threshold" {
  description = "Consecutive successes required to mark target healthy"
  type        = number
  default     = 2
}

variable "health_check_unhealthy_threshold" {
  description = "Consecutive failures required to mark target unhealthy"
  type        = number
  default     = 10
}

# RabbitMQ Configuration

variable "rabbitmq_port" {
  description = "RabbitMQ AMQP protocol port"
  type        = number
  default     = 5672
}

variable "rabbitmq_management_port" {
  description = "RabbitMQ management console port"
  type        = number
  default     = 15672
}

variable "rabbitmq_default_user" {
  description = "RabbitMQ default admin username"
  type        = string
  default     = "guest"
}

variable "rabbitmq_default_pass" {
  description = "RabbitMQ default admin password"
  type        = string
  default     = "guest"
  sensitive   = true
}

variable "rabbitmq_host" {
  description = "RabbitMQ host for ShoppingCart and Warehouse services"
  type        = string
  default     = "REPLACE_WITH_RABBITMQ_PRIVATE_IP"
}

variable "ship_queue" {
  description = "RabbitMQ queue name for shipping messages"
  type        = string
  default     = "ship-order"
}

variable "rmq_confirm_timeout_ms" {
  description = "Publisher confirm timeout (ms) for ShoppingCart service"
  type        = number
  default     = 5000
}

variable "consumer_concurrency" {
  description = "Warehouse consumer concurrency threads"
  type        = number
  default     = 8
}

# Logging

variable "log_retention_days" {
  description = "Number of days to retain CloudWatch logs"
  type        = number
  default     = 7
}
