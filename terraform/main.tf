# Main Terraform Configuration
# Provider setup and data sources for existing AWS resources
# Uses default VPC

terraform {
  required_version = ">= 1.5.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = ">= 5.32.0, < 6.0.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

# Data Sources: Existing AWS Resources
data "aws_vpc" "default" {
  default = true
}

data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }

  filter {
    name   = "default-for-az"
    values = ["true"]
  }
}

# Lab Role pre-created in AWS Academy Learner Lab
data "aws_iam_role" "lab_role" {
  name = "LabRole"
}
