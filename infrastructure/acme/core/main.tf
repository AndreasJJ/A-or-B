# ---------------------------------------------------------
# Load the outputs from part 1 of the separately managed 
# process (terraform acme registration in this case)
# ---------------------------------------------------------
data "terraform_remote_state" "letsencrypt_registration" {
    backend = "local"
    config {
        path = "${path.root}/../acme/registration/terraform.tfstate"
    }
}

# ---------------------------------------------------------
# Perform part 2 of the demo setup
# ---------------------------------------------------------
module "dns" {
    source = "../modules/dns/indirect"
    dns_domain_name               = var.domain_name
    dns_domain_subdomain          = var.domain_subdomain
    dns_cname_value               = module.aws-env.demo_env_elb_dnsname
}

module "acme-cert" {
    source = "../modules/certificate-request"
    acme_server_url               = data.terraform_remote_state.letsencrypt_registration.server_url
    acme_account_registration_url = data.terraform_remote_state.letsencrypt_registration.registration_url
    acme_account_key_pem          = data.terraform_remote_state.letsencrypt_registration.registration_private_key_pem
    acme_certificate_common_name  = module.dns.fqdn_domain_name
    # To make use of a single direct DNS record, comment out the line 
    # above, uncomment the one below, and ensure the dns module source
    # is loaded from modules/dns/direct. This current approach has been
    # done to remove a cyclic dependency.
    # acme_certificate_common_name  = "${var.demo_domain_name}.${var.demo_domain_subdomain}"

    acme_challenge_aws_access_key_id     = var.acme_challenge_aws_access_key_id
    acme_challenge_aws_secret_access_key = var.acme_challenge_aws_secret_access_key
    acme_challenge_aws_region            = var.acme_challenge_aws_region
}


module "aws-env" {
    source = "../modules/environment"
    env_nginx_count                = "2"
    env_cert_body                  = module.acme-cert.certificate_pem
    env_cert_chain                 = module.acme-cert.certificate_issuer_pem
    env_cert_privkey               = module.acme-cert.certificate_private_key_pem
}