use actix::{Actor, StreamHandler};
use actix_web_actors::ws;
use actix_web::{web, Error, HttpRequest, HttpResponse};

struct Websocket;

use crate::api::websocket::handler;

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

pub async fn index(req: HttpRequest, stream: web::Payload) -> Result<HttpResponse, Error> {
  let resp = ws::start(Websocket {}, &req, stream);
  println!("{:?}", resp);
  resp
}

