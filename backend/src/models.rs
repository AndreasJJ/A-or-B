use crate::schema::tickets;
use crate::schema::games;
use crate::schema::rounds;
use serde::{Deserialize, Serialize};
use uuid;

#[derive(Debug, Serialize, Deserialize, Queryable)]
pub struct Ticket {
    pub id: uuid::Uuid,
    pub token: String,
    pub timestamp: chrono::NaiveDateTime,
    pub ip: String,
    pub used: bool,
}

#[derive(Insertable, Debug)]
#[table_name = "tickets"]
pub struct NewTicket<'a> {
    pub token: &'a str,
    pub timestamp: chrono::NaiveDateTime,
    pub ip: &'a str,
    pub used: bool,
}

#[derive(Debug, Serialize, Deserialize, Identifiable, Associations, Queryable)]
pub struct Game {
    pub id: uuid::Uuid,
    pub owner: String,
    pub timestamp: chrono::NaiveDateTime,
    pub title: String,
    pub left_text: String,
    pub right_text: String,
}

#[derive(Debug, Serialize, Deserialize, Identifiable, Associations, Queryable)]
#[belongs_to(Game)]
pub struct Round {
    pub id: i32,
    pub game_id: uuid::Uuid,
    pub title: String,
    pub link: String,
}

#[derive(Insertable, Debug)]
#[table_name = "games"]
pub struct NewGame<'a> {
    pub owner: &'a str,
    pub timestamp: chrono::NaiveDateTime,
    pub title: &'a str,
    pub left_text: &'a str,
    pub right_text: &'a str,
}

#[derive(Insertable, Debug)]
#[table_name = "rounds"]
pub struct NewRound<'a> {
    pub game_id: uuid::Uuid,
    pub title: &'a str,
    pub link: &'a str,
}