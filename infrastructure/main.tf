provider "aws" {
  region = "us-east-1"
  shared_credentials_file = ".aws/creds"
}

variable "root_domain_name" {
  default = "andreas.software"
}

locals {
  application_domain = "app.${var.root_domain_name}"
}