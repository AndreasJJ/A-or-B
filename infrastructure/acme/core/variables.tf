# ----------------------------------------------------------------
# Variables required for ACME provider demo
# ----------------------------------------------------------------

# Domain against which certificate will be created
# i.e. letsencrypt-terraform.example.com
variable "domain_name"              { default = "andreas.software"}
variable "domain_subdomain"         { default = "app"}

# Leave blank here, supply securely at runtime 
variable "acme_challenge_aws_access_key_id"     { }
variable "acme_challenge_aws_secret_access_key" { }
variable "acme_challenge_aws_region"            { }