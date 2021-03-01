# -----------------------------------------------------------
# Inputs required to create our demo (NGINX, ELB fronted)
# environment in AWS
# -----------------------------------------------------------

variable "env_cert_body"       {}
variable "env_cert_chain"      {}
variable "env_cert_privkey"    {}
variable "env_nginx_count"     {}