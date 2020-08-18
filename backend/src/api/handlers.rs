use actix_web::Responder;
use actix_web::dev::ServiceRequest;
use actix_web::web;
use actix_files as fs;
use serde_derive::Deserialize;
use  std::collections::HashMap;

#[derive(Deserialize)]
pub struct Info {
    username: String,
}

pub async fn login(path: web::Path<Info>, query: web::Query<HashMap<String, String>>, body: web::Json<Info>) -> impl Responder {
    format!("login")
}

pub async fn register() -> impl Responder {
    format!("register")
}