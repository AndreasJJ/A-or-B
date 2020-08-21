table! {
    games (id) {
        id -> Uuid,
        owner -> Text,
        timestamp -> Timestamp,
        title -> Text,
        left_text -> Text,
        right_text -> Text,
    }
}

table! {
    rounds (id) {
        id -> Int4,
        game_id -> Uuid,
        link -> Text,
        title -> Text,
    }
}

table! {
    tickets (id) {
        id -> Uuid,
        token -> Text,
        timestamp -> Timestamp,
        ip -> Text,
        used -> Bool,
    }
}

joinable!(rounds -> games (game_id));

allow_tables_to_appear_in_same_query!(
    games,
    rounds,
    tickets,
);
