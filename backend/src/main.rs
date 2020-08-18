use actix_web::{dev::ServiceRequest, middleware, web, App, Error, HttpServer};

mod auth;
mod errors;
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

    // Start http server
    HttpServer::new(move || {
        let auth = HttpAuthentication::bearer(validator);
        App::new()
            .wrap(middleware::Logger::default())
            .wrap(auth)
            .service(web::resource("/websocket/").to(websocket::index))
            .service(
                web::scope("/api")
                    .service(web::resource("/login").route(web::post().to(handlers::login)))
                    .service(web::resource("/register").route(web::post().to(handlers::register)))
            )
    })
    .bind("0.0.0.0:8082")?
    .run()
    .await
}