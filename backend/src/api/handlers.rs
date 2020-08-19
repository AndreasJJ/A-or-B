use actix_web::Responder;
use actix_web::dev::ServiceRequest;
use actix_web::{web, Error, HttpResponse};
use actix_files as fs;
use serde_derive::Deserialize;
use  std::collections::HashMap;

pub async fn ticket(query: web::Query<HashMap<String, String>>) -> Result<HttpResponse, Error> {
    println!("Hello ticket");
    return Ok(HttpResponse::Ok().finish());
}