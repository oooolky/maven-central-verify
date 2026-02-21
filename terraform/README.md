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
