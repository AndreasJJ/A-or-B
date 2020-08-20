use actix::{Actor, StreamHandler};
use actix_web_actors::ws;
use actix_web::{web, Error, HttpRequest, HttpResponse};
use serde::Deserialize;
use uuid;
use chrono::prelude::Utc;

struct Websocket;

use super::super::super::Pool;
use super::handler;
use super::super::database_utilty::get_ticket;

impl Actor for Websocket {
  type Context = ws::WebsocketContext<Self>;
}

impl StreamHandler<Result<ws::Message, ws::ProtocolError>> for Websocket {
  fn handle(
    &mut self,
    msg: Result<ws::Message, ws::ProtocolError>,
    ctx: &mut Self::Context,
  ) {
    match msg {
      Ok(ws::Message::Ping(msg)) => ctx.pong(&msg),
      Ok(ws::Message::Text(text)) => {
        let result = handler::text_handler(&text);
        ctx.text(result);
      },
      Ok(ws::Message::Binary(bin)) => ctx.binary(bin),
      Ok(ws::Message::Close(_)) => {
        ctx.close(Some(ws::CloseReason::from(ws::CloseCode::Normal)));
      }
      Ok(ws::Message::Continuation(_)) => {
        ctx.close(Some(ws::CloseReason::from(ws::CloseCode::Normal)));
      }
      Ok(ws::Message::Nop) => (),
      _ => (),
    }
  }
}

impl Websocket {
  fn routeHandler(route: &str) {

  }
}

#[derive(Deserialize)]
pub struct Info {
    ticket: String,
}

pub async fn index(db: web::Data<Pool>, info: web::Query<Info>, req: HttpRequest, stream: web::Payload) -> Result<HttpResponse, Error> {
  let ticket_res = uuid::Uuid::parse_str(&info.ticket);
  match ticket_res {
    Ok(ticket_uuid) => {
      // Get the ticket from the provided id
      let ticket = get_ticket(db, ticket_uuid);
      let ticket_res = ticket.unwrap();
      // Check that the ticket is less than 30s old
      if ticket_res.timestamp.timestamp_millis() + 30000 < Utc::now().timestamp_millis() {
        println!("Token too old, token: {}, now: {}", ticket_res.timestamp.timestamp_millis() + 30000, Utc::now().timestamp_millis());
        return Err(Error::from(HttpResponse::BadRequest().json("Ticket is too old")));
      }

      // Check that the ip is the same
      let remote_socket = req.peer_addr().unwrap();
      let remote_ip = remote_socket.ip();
      if remote_ip.to_string() != ticket_res.ip {
        println!("Not samee Ip");
        return Err(Error::from(HttpResponse::BadRequest().json("Failed to recognize user")));
      }

      let resp = ws::start(Websocket {}, &req, stream);
      resp
    }
    Err(_) => Err(Error::from(HttpResponse::BadRequest().json("Failed to parse token"))),
  }
}

