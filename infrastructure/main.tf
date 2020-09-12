provider "aws" {
  region = "eu-central-1"
  shared_credentials_file = ".aws/creds"
}

variable "root_domain_name" {
  default = "andreas.software"
}

locals {
  application_domain = "app.${var.root_domain_name}"
}

resource "aws_acm_certificate" "ssl_cert" {
  domain_name   = "*.${var.root_domain_name}"
  validation_method = "DNS"

  lifecycle {
    create_before_destroy = true
  }
}