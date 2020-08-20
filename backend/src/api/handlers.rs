//use actix_web::Responder;
//use actix_web::dev::ServiceRequest;
use actix_web::{web, Error, HttpResponse, HttpRequest};
//use actix_files as fs;
//use serde_derive::Deserialize;
use  std::collections::HashMap;

use super::super::Pool;
use super::database_utilty::create_ticket;

pub async fn ticket(db: web::Data<Pool>, req: HttpRequest, query: web::Query<HashMap<String, String>>) -> Result<HttpResponse, Error> {
    let headers = req.headers();
    let auth_header = headers.get("Authorization");
    let auth_header_str: &str = &auth_header.unwrap().to_str().unwrap();
    let auth_token = auth_header_str.replace("Bearer ", "");
    let remote_socket = req.peer_addr().unwrap();
    let remote_ip = remote_socket.ip();

    Ok(web::block(move || create_ticket(&db, &auth_token, &remote_ip.to_string()))
        .await
        .map(|ticket| HttpResponse::Created().json(ticket.id))
        .map_err(|_| HttpResponse::InternalServerError())?)
}