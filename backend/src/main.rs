#[macro_use]
extern crate diesel;

use diesel::prelude::*;
use diesel::r2d2::{self, ConnectionManager};
pub type Pool = r2d2::Pool<ConnectionManager<PgConnection>>;

use actix_web::{dev::ServiceRequest, middleware, web, App, Error, HttpServer};

mod auth;
mod errors;
mod models;
mod schema;
mod api;
use self::api::handlers;
use self::api::websocket::websocket;

use actix_web_httpauth::extractors::bearer::{BearerAuth, Config};
use actix_web_httpauth::extractors::AuthenticationError;
use actix_web_httpauth::middleware::HttpAuthentication;

async fn validator(req: ServiceRequest, credentials: BearerAuth) -> Result<ServiceRequest, Error> {
    let config = req
        .app_data::<Config>()
        .map(|data| data.get_ref().clone())
        .unwrap_or_else(Default::default);

    match auth::validate_token(credentials.token()) {
        Ok(res) => {
            if res == true {
                Ok(req)
            } else {
                Err(AuthenticationError::from(config).into())
            }
        }
        Err(_) => Err(AuthenticationError::from(config).into()),
    }
}

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
        App::new()
            .data(pool.clone())
            .wrap(middleware::Logger::default())
            .service(web::resource("/websocket/").to(websocket::index))
            .service(
                web::scope("/auth/")
                    .wrap(auth)
                    .service(web::resource("/ticket").route(web::get().to(handlers::ticket)))
            )
    })
    .bind("0.0.0.0:8082")?
    .run()
    .await
}