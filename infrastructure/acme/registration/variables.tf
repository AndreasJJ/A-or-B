# ----------------------------------------------------------------
# Variables required for ACME provider registration
# ----------------------------------------------------------------

# Let's Encrypt Account Registration Config
# -- Production
# variable "acme_server_url"          { default = "https://acme.api.letsencrypt.org/directory"}
# variable "acme_registration_email"  { default = "contact@andreas.software" }
# -- Staging
variable "acme_server_url"          { default = "https://acme-staging.api.letsencrypt.org/directory"}
variable "acme_registration_email"  { default = "contact@andreas.software" }