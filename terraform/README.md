# Terraform Guide

## Team Init = Use the same commands

Run same workflow on every team member's machine

```bash
terraform init -upgrade
terraform validate
terraform plan
```

## Notes

Use this order for every environment check

Keep provider versions consistent across the team

Please do not skip `validate` before `plan`

## 2026.02.21 update

### ALB health check and service endpoint mismatch

Current ALB target groups still use shared health check settings

Product service has `/health`, but ShoppingCart and CCA may not expose `/health` yet

If target group matcher/path is not aligned with each service, targets can stay unhealthy

Will fix after teammate endpoints are finalised

### RabbitMQ placeholder host strategy

`rabbitmq_host` is still a placeholder value 

Current value set as `REPLACE_WITH_RABBITMQ_PRIVATE_IP`

Quick lookup = `terraform/variables.tf:145`

Quick lookup = `terraform/terraform.tfvars:55`

Will finalise after team integration is complete
