# Infrastructure as code with Terraform

On mac, use the command ```export $(grep -v '^#' .scw/creds | xargs -0)``` to export all the variables in the .env file.

## Dependencies
- [Terraform](https://www.terraform.io/)
- [Terraform ACME provider](https://github.com/vancluever/terraform-provider-acme)