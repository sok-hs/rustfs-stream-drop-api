CREATE TABLE user_documents (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    object_key VARCHAR(500) NOT NULL UNIQUE,
    content_type VARCHAR(100),
    file_size BIGINT
);

create table users(
    id BIGSERIAL PRIMARY KEY,
    username varchar(100) not null,
);