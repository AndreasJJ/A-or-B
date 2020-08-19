table! {
  tickets (id) {
      id -> Uuid,
      token -> Text,
      timestamp -> Timestamp,
      used -> Bool,
  }
}