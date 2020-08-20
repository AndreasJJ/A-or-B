table! {
    tickets (id) {
        id -> Uuid,
        token -> Text,
        timestamp -> Timestamp,
        ip -> Text,
        used -> Bool,
    }
}
