use actix_web::{web};
use super::super::models::{NewTicket, Ticket};
use super::super::schema::tickets::dsl::{tickets, used};
use super::super::Pool;
use diesel::dsl::{insert_into, update};
use crate::diesel::QueryDsl;
use crate::diesel::RunQueryDsl;
use crate::diesel::ExpressionMethods;
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