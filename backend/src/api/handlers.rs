use actix_web::Responder;
use actix_web::dev::ServiceRequest;
use actix_web::{web, Error, HttpResponse, HttpRequest};
use actix_files as fs;
use serde_derive::Deserialize;
use  std::collections::HashMap;

use super::super::models::{NewTicket, Ticket};
use super::super::schema::tickets::dsl::*;
use super::super::Pool;
use diesel::dsl::{insert_into};
use crate::diesel::RunQueryDsl;

pub async fn ticket(db: web::Data<Pool>, req: HttpRequest, query: web::Query<HashMap<String, String>>) -> Result<HttpResponse, Error> {
    let headers = req.headers();
    let auth_header = headers.get("Authorization");
    let auth_header_str: &str = &auth_header.unwrap().to_str().unwrap();
    let auth_token = auth_header_str.replace("Bearer ", "");
    let remote_socket = req.peer_addr().unwrap();
    let remote_ip = remote_socket.ip();

    Ok(web::block(move || get_ticket(db, &auth_token, &remote_ip.to_string()))
        .await
        .map(|ticket| HttpResponse::Created().json(ticket.id))
        .map_err(|_| HttpResponse::InternalServerError())?)
}

fn get_ticket(db: web::Data<Pool>, auth_token: &str, remote_ip: &str) -> Result<Ticket, diesel::result::Error> {
    let conn = db.get().unwrap();
    let new_ticket = NewTicket {
        token: auth_token,
        timestamp: chrono::Local::now().naive_local(),
        ip: remote_ip,
        used: false,
    };
    let res = insert_into(tickets).values(&new_ticket).get_result::<Ticket>(&conn)?;
    Ok(res)
}