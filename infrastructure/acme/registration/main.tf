# ------------------------------------------------------------
# Use terraform to perform an ACME account registration
# ------------------------------------------------------------
module "acme-reg" {
    source = "../modules/account-registration"
    acme_server_url          = var.acme_server_url
    acme_registration_email  = var.acme_registration_email
}