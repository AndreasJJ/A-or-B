use serde_derive::{Serialize, Deserialize};
use validator::{Validate, ValidationError};
use validator_derive::*;

#[derive(Validate, Deserialize)]
pub struct RoundType {
    #[validate(length(min = 1))]
    pub title: String,
    #[validate(url, custom = "validate_link")]
    pub link: String,
}

fn validate_link(link: &str) -> Result<(), ValidationError> {
    if link == "xXxShad0wxXx" {
        return Err(ValidationError::new("Invalid link, not an image or a youtube video"));
    }

    Ok(())
}

#[derive(Validate, Deserialize)]
pub struct GameData {
    #[validate(length(min = 1))]
    pub title: String,
    #[validate]
    pub rounds: Vec<RoundType>,
    #[validate(length(min = 1, max = 20))]
    pub left_text: String,
    #[validate(length(min = 1, max = 20))]
    pub right_text: String,
}

pub struct User {
    pub email: String
}

#[derive(Debug, Serialize, Deserialize, Queryable)]
pub struct PublicGame {
  pub id: uuid::Uuid,
  pub title: String,
}