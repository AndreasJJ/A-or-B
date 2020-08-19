use crate::schema::*;
use serde::{Deserialize, Serialize};
use uuid;

#[derive(Debug, Serialize, Deserialize, Queryable)]
pub struct Ticket {
    pub id: uuid::Uuid,
    pub token: String,
    pub timestamp: chrono::NaiveDateTime,
    pub used: bool,
}

#[derive(Insertable, Debug)]
#[table_name = "tickets"]
pub struct NewTicket<'a> {
    pub token: &'a str,
    pub timestamp: chrono::NaiveDateTime,
    pub used: bool,
}