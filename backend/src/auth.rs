use crate::errors::ServiceError;
use alcoholic_jwt::{token_kid, validate, Validation, JWKS};
use serde::{Deserialize};
use std::error::Error;

use actix_web::{dev::ServiceRequest, HttpRequest};
use actix_web_httpauth::extractors::bearer::{BearerAuth, Config};
use actix_web_httpauth::extractors::AuthenticationError;

use super::api::types::User;

pub async fn validator(req: ServiceRequest, credentials: BearerAuth) -> Result<ServiceRequest, actix_web::Error> {
    let config = req
        .app_data::<Config>()
        .map(|data| data.get_ref().clone())
        .unwrap_or_else(Default::default);

    match validate_token(credentials.token()).await {
        Ok(res) => {
            if res.0 == true {
                let new_user = User {
                    email: res.1
                };

                let parts = req.into_parts();
                let http_request = parts.0;
                let http_payload = parts.1;
                add_extensions(&http_request, new_user);

                let result = ServiceRequest::from_request(http_request);
                if let Ok(mut new_request) = result {
                    new_request.set_payload(http_payload);
                    Ok(new_request)
                } else {
                    Err(AuthenticationError::from(config).into())
                }
            } else {
                Err(AuthenticationError::from(config).into())
            }
        }
        Err(_) => Err(AuthenticationError::from(config).into()),
    }
}

pub fn add_extensions(re: &HttpRequest, user: User) {
    re.extensions_mut().insert(user);
}

pub async fn validate_token(token: &str) -> Result<(bool, String), ServiceError> {
    let authority = std::env::var("AUTHORITY").expect("AUTHORITY must be set");
    let openid = fetch_openid_config(authority.as_str()).await
        .expect("failed to fetch openid config");
    let jwks_uri = openid.jwks_uri;
    // TODO: Solve the problem of the issuer url being keycloak:8080.... temp solution is env. variable
    // let issuer = openid.issuer;
    let issuer = std::env::var("ISSUER").expect("ISSUER must be set");
    let jwks = fetch_jwks(&format!("{}", jwks_uri)).await
        .expect("failed to fetch jwks");
    let validations = vec![Validation::Issuer(issuer), Validation::SubjectPresent];
    let kid = match token_kid(&token) {
        Ok(res) => res.expect("failed to decode kid"),
        Err(_) => return Err(ServiceError::JWKSFetchError),
    };
    let jwk = jwks.find(&kid).expect("Specified key not found in set");
    let res = validate(token, jwk, validations);
    if let Err(e) = &res {
        return Err(ServiceError::InternalServerError)
    }
    let ok = res.is_ok();
    let result = res.unwrap();
    let email = result.claims.get("email").unwrap();
    Ok((ok, email.to_string()))
}

#[derive(Clone, Debug, Deserialize)]
struct OpenidConfig {
    authorization_endpoint: String,
    check_session_iframe: String,
    claim_types_supported: Vec<String>,
    claims_parameter_supported: bool,
    claims_supported: Vec<String>,
    code_challenge_methods_supported: Vec<String>,
    end_session_endpoint: String,
    grant_types_supported: Vec<String>,
    id_token_encryption_alg_values_supported: Vec<String>,
    id_token_encryption_enc_values_supported: Vec<String>,
    id_token_signing_alg_values_supported: Vec<String>,
    introspection_endpoint: String,
    issuer: String,
    jwks_uri: String,
    registration_endpoint: String,
    request_object_signing_alg_values_supported: Vec<String>,
    request_parameter_supported: bool,
    request_uri_parameter_supported: bool,
    response_modes_supported: Vec<String>,
    response_types_supported: Vec<String>,
    scopes_supported: Vec<String>,
    subject_types_supported: Vec<String>,
    tls_client_certificate_bound_access_tokens: bool,
    token_endpoint: String,
    token_endpoint_auth_methods_supported: Vec<String>,
    token_endpoint_auth_signing_alg_values_supported: Vec<String>,
    token_introspection_endpoint: String,
    userinfo_endpoint: String,
    userinfo_signing_alg_values_supported: Vec<String>,
}

async fn fetch_openid_config(uri: &str) -> Result<OpenidConfig, Box<dyn Error>> {
    let mut res = reqwest::get(uri).await?;
    let val = res.json::<OpenidConfig>().await?;
    return Ok(val);
}

async fn fetch_jwks(uri: &str) -> Result<JWKS, Box<dyn Error>> {
    let mut res = reqwest::get(uri).await?;
    let val = res.json::<JWKS>().await?;
    return Ok(val);
}