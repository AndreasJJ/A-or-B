use validator::{Validate};
//use actix_web::Responder;
//use actix_web::dev::ServiceRequest;
use actix_web::{web, Error, HttpResponse, HttpRequest};
//use actix_files as fs;
use std::collections::HashMap;
use serde_derive::{Serialize, Deserialize};

use super::super::Pool;
use super::database_utilty::create_ticket;
use super::database_utilty::create_game;
use super::database_utilty::get_my_games;
use super::database_utilty::get_my_game;

use super::types::GameData;
use super::types::User;

pub async fn ticket(db: web::Data<Pool>, req: HttpRequest, query: web::Query<HashMap<String, String>>) -> Result<HttpResponse, Error> {
    let headers = req.headers();
    let auth_header = headers.get("Authorization");
    let auth_header_str: &str = &auth_header.unwrap().to_str().unwrap();
    let auth_token = auth_header_str.replace("Bearer ", "");
    let remote_socket = req.peer_addr().unwrap();
    let remote_ip = remote_socket.ip();

    Ok(web::block(move || create_ticket(&db, &auth_token, &remote_ip.to_string()))
        .await
        .map(|ticket| HttpResponse::Ok().json(ticket.id))
        .map_err(|_| HttpResponse::InternalServerError())?)
}

pub async fn new_game(db: web::Data<Pool>, req: HttpRequest, data: web::Json<GameData>) -> Result<HttpResponse, Error> {
    if let Err(e) = data.validate() {
        return Err(Error::from(HttpResponse::BadRequest().json(e)))
    }

    let extensions = req.extensions();
    let user_option = extensions.get::<User>();

    match user_option {
        Some(user) => {
            let email = user.email.clone();
            Ok(web::block(move || create_game(&db, &email, data.into_inner()))
            .await
            .map(|game| HttpResponse::Created().json(game.id))
            .map_err(|_| HttpResponse::InternalServerError())?)
        },
        None => Err(Error::from(HttpResponse::BadRequest().json("Failed to fetch user"))),
    }
}

pub async fn get_games(db: web::Data<Pool>, req: HttpRequest) -> Result<HttpResponse, Error> {
    let extensions = req.extensions();
    let user_option = extensions.get::<User>();

    match user_option {
        Some(user) => {
            let email = user.email.clone();
            Ok(web::block(move || get_my_games(&db, &email))
            .await
            .map(|games| HttpResponse::Ok().json(games))
            .map_err(|_| HttpResponse::InternalServerError())?)
        },
        None => Err(Error::from(HttpResponse::BadRequest().json("Failed to fetch user"))),
    }
}

#[derive(Serialize, Deserialize)]
pub struct GamePath {
    pub id: String,
}

pub async fn get_game(db: web::Data<Pool>, params: web::Path<GamePath>, req: HttpRequest) -> Result<HttpResponse, Error> {
    let extensions = req.extensions();
    let user_option = extensions.get::<User>();
    let parsed: uuid::Uuid = uuid::Uuid::parse_str(&params.id).unwrap();

    match user_option {
        Some(user) => {
            let email = user.email.clone();
            Ok(web::block(move || get_my_game(&db, &email, &parsed))
            .await
            .map(|game| HttpResponse::Ok().json(game))
            .map_err(|_| HttpResponse::InternalServerError())?)
        },
        None => Err(Error::from(HttpResponse::BadRequest().json("Failed to fetch user"))),
    }
}