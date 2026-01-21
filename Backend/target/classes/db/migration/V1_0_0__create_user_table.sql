CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    email           VARCHAR(255) NOT NULL UNIQUE,
    full_name       VARCHAR(255),
    google_id     VARCHAR(255) unique,
    daml_party_id VARCHAR(255) unique,
    user_level varchar(10),
    created_date      TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by      VARCHAR(255) default 'SYSTEM',
    modified_date      TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    modified_by      VARCHAR(255),
    is_deleted bool
);