CREATE TABLE IF NOT EXISTS test_links
(
    id           BIGSERIAL PRIMARY KEY,
    original_url VARCHAR(255) NOT NULL UNIQUE,
    short_url    VARCHAR(255) NOT NULL UNIQUE,
    alias        VARCHAR(255) NOT NULL UNIQUE,
    created_at   DATE         NOT NULL DEFAULT CURRENT_DATE,
    ttl          DATE
);