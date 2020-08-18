pub fn text_handler(text: &str) -> &str {
  match text {
    "/join" => {

    }
    "/2" => {

    }
    "/3" => {

    }
    _ => {
      format!("!!! unknown command: {:?}", text);
    }
  }
  text
}