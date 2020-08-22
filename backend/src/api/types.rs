use serde_derive::{Serialize, Deserialize};
use validator::{Validate, ValidationError};
use validator_derive::*;
use url::{Url};
use regex::Regex;

#[derive(Validate, Serialize, Deserialize, Queryable)]
pub struct RoundType {
    #[validate(length(min = 1))]
    pub title: String,
    #[validate(url, custom = "validate_link")]
    pub link: String,
}

fn validate_link(link: &str) -> Result<(), ValidationError> {
    let link_res = Url::parse(link);
    match link_res {
        Ok(link_option) => {
            let link_str = link_option.host_str();
            let mut pairs = link_option.query_pairs();
            let id = pairs.find(|tup| tup.0 == "v");
            let path = link_option.path();
            match link_str {
                Some(link_url) => {
                    println!("{}", link_url);
                    let re = Regex::new(r"^((www\.)?youtube\.com|youtu\.be)$").unwrap();
                    if !re.is_match(link_url) {
                        if link_option.cannot_be_a_base() || (!path.ends_with(".png") && !path.ends_with(".gif") && !path.ends_with(".jpg")  && !path.ends_with(".jpeg")) {
                            return Err(ValidationError::new("Invalid link, not a valid image link"));
                        }
                    } else {
                        if let None = id {
                            return Err(ValidationError::new("Invalid link, youtube video is missing the video id"));
                        }
                    }
                },
                None => {
                    return Err(ValidationError::new("Invalid link, not an image or a youtube video"));
                },
            };
        },
        Err(_) => {
            return Err(ValidationError::new("Invalid link, not an image or a youtube video"));
        },
    }
    
    Ok(())
}

#[derive(Validate, Serialize, Deserialize)]
pub struct GameData {
    #[validate(length(min = 1))]
    pub title: String,
    #[validate(length(min = 1))]
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
  pub thumbnail: String,
}