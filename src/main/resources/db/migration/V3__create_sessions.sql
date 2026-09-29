CREATE TABLE sessions (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    token       VARCHAR(64)  NOT NULL,
    user_id     BIGINT       NOT NULL,
    expires_at  TIMESTAMPTZ  NOT NULL,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_session_token ON sessions USING hash (token);

CREATE INDEX idx_session_user_id ON sessions (user_id);
