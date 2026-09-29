CREATE TABLE sessions (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    token_hash  VARCHAR(64)  NOT NULL,
    user_id     BIGINT       NOT NULL,
    ip_address  VARCHAR(45)  NULL,
    user_agent  VARCHAR(500) NULL,
    expires_at  TIMESTAMPTZ  NOT NULL,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_sessions_user_id FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
);

CREATE UNIQUE INDEX idx_session_token_hash ON sessions (token_hash);

CREATE INDEX idx_session_user_id ON sessions (user_id);

CREATE INDEX idx_session_expires_at ON sessions (expires_at);
