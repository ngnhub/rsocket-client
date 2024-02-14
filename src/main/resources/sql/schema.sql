CREATE TABLE IF NOT EXISTS saved_requests
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    host     TEXT      NOT NULL,
    port     INT       NOT NULL,
    route    TEXT      NOT NULL,
    saved_at TIMESTAMP NOT NULL DEFAULT NOW()
);
