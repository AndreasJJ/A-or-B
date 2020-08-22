use actix_web::{web};
use super::super::models::{NewTicket, Ticket, Game, NewGame, Round, NewRound};
use super::super::schema::tickets::dsl::{tickets, used};
use super::super::schema::games::dsl::{games, id, owner, title};
use super::super::schema::rounds::dsl::{rounds, game_id, link};
use super::super::Pool;
use super::types::{GameData, PublicGame};
use diesel::dsl::{insert_into, update};
use crate::diesel::QueryDsl;
use crate::diesel::RunQueryDsl;
use crate::diesel::ExpressionMethods;
use crate::diesel::Connection;
use crate::diesel::JoinOnDsl;
use uuid;

pub fn create_ticket(db: &web::Data<Pool>, auth_token: &str, remote_ip: &str) -> Result<Ticket, diesel::result::Error> {
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

pub fn get_ticket(db: &web::Data<Pool>, ticket_id: uuid::Uuid) -> Result<Ticket, diesel::result::Error> {
  let conn = db.get().unwrap();
  let res = tickets.find(ticket_id).get_result::<Ticket>(&conn)?;
  Ok(res)
}

pub fn invalidate_ticket(db: &web::Data<Pool>, ticket_id: uuid::Uuid) -> Result<Ticket, diesel::result::Error> {
  let conn = db.get().unwrap();
  let res = update(tickets.find(ticket_id)).set(used.eq(true)).get_result::<Ticket>(&conn)?;
  Ok(res)
}

pub fn create_game(db: &web::Data<Pool>, email: &str, game_data: GameData) -> Result<Game, diesel::result::Error>  {
  let conn = db.get().unwrap();
  conn.transaction::<Game, diesel::result::Error, _>(|| {
    let new_game = NewGame {
      owner: email,
      timestamp: chrono::Local::now().naive_local(),
      title: &game_data.title,
      left_text: &game_data.left_text,
      right_text: &game_data.right_text,
    };
    let game_res = insert_into(games).values(&new_game).get_result::<Game>(&conn)?;
    
    for round in game_data.rounds.iter() {
      let new_round = NewRound {
        game_id: game_res.id,
        title: &round.title,
        link: &round.link,
      };
      insert_into(rounds).values(&new_round).execute(&conn)?;
    }

    Ok(game_res)
  })
}

pub fn get_my_games(db: &web::Data<Pool>, email: &str) -> Result<Vec<PublicGame>, diesel::result::Error> {
  let conn = db.get().unwrap();
  let filter = games.filter(owner.eq(email));
  let joined = filter.inner_join(rounds.on(id.eq(game_id)));
  let distinct = joined.distinct_on(id);
  let selected = distinct.select((id, title, link));
  let res = selected.load::<PublicGame>(&conn)?;
  Ok(res)
}