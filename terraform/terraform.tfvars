# Terraform Variable Values
# Adjust values to tune deployment. 

# General
aws_region   = "us-west-2"
project_name = "cs6650-assignment03"


# Networking
container_port    = 8080
alb_listener_port = 80

# =============================================================================
# ECS Resources 
# Increase if throughput bottlenecks at the server
# =============================================================================

ecs_task_cpu    = "512"  # 0.5 vCPU per microservice task
ecs_task_memory = "1024" # 1 GB per microservice task

rabbitmq_task_cpu    = "1024" # 1 vCPU for RabbitMQ broker
rabbitmq_task_memory = "2048" # 2 GB for RabbitMQ broker

# =============================================================================
# Service Instance Counts
# =============================================================================

product_service_good_count  = 2 # Two healthy Product Service instances
product_service_bad_count   = 1 # One "bad" instance for ATW demonstration
shopping_cart_service_count = 2 # Scale up if SCS becomes a bottleneck
credit_card_service_count   = 1 # Lightweight mock service
warehouse_consumer_count    = 1 # Single consumer task (multi-threaded internally)

# =============================================================================
# Health Checks
# =============================================================================
# Unhealthy threshold is set high (10) so the "bad" Product Service
# stays in service long enough for ATW to demonstrate traffic shifting.
# Its /health endpoint always returns 200, but the high threshold is an
# extra safety net.

health_check_path                = "/health"
health_check_interval            = 30
health_check_healthy_threshold   = 2
health_check_unhealthy_threshold = 10

# =============================================================================
# RabbitMQ
# =============================================================================

rabbitmq_port            = 5672
rabbitmq_management_port = 15672
rabbitmq_default_user    = "guest"
rabbitmq_default_pass    = "guest"
rabbitmq_host            = "REPLACE_WITH_RABBITMQ_PRIVATE_IP"
ship_queue               = "ship-order"
rmq_confirm_timeout_ms   = 5000
consumer_concurrency     = 8

# =============================================================================
# Logging
# =============================================================================

log_retention_days = 7
