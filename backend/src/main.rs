#[macro_use]
extern crate diesel;
extern crate validator_derive;
extern crate validator;

use diesel::prelude::*;
use diesel::r2d2::{self, ConnectionManager};
pub type Pool = r2d2::Pool<ConnectionManager<PgConnection>>;

use actix_web::{middleware, web, App, HttpServer};
use actix_web_httpauth::middleware::HttpAuthentication;

mod auth;
mod errors;
mod models;
mod schema;
mod api;
use self::api::handlers;
use self::api::websocket::websocket;
use self::auth::validator;

#[actix_rt::main]
async fn main() -> std::io::Result<()> {
    dotenv::dotenv().ok();
    std::env::set_var("RUST_LOG", "actix_web=debug");
    env_logger::init();

    let database_url = std::env::var("DATABASE_URL").expect("DATABASE_URL must be set");

     // create db connection pool
     let manager = ConnectionManager::<PgConnection>::new(database_url);
     let pool: Pool = r2d2::Pool::builder()
         .build(manager)
         .expect("Failed to create pool.");

    // Start http server
    HttpServer::new(move || {
        let auth = HttpAuthentication::bearer(validator);
        let auth2 = HttpAuthentication::bearer(validator);
        App::new()
            .data(pool.clone())
            .wrap(middleware::Logger::default())
            .service(web::resource("/websocket").to(websocket::index))
            .service(
                web::scope("/auth/")
                    .wrap(auth)
                    .service(web::resource("/ticket").route(web::get().to(handlers::ticket)))
            )
            .service(
                web::scope("/game/")
                    .wrap(auth2)
                    .service(web::resource("/new").route(web::post().to(handlers::new_game)))
                    .service(web::resource("/list").route(web::get().to(handlers::get_games)))
                    .service(web::resource("/{id}").route(web::get().to(handlers::get_game)))
            )
    })
    .bind("0.0.0.0:8082")?
    .run()
    .await
}