CREATE TABLE users (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid        UUID         NOT NULL DEFAULT gen_random_uuid(),
    pegawai_id  BIGINT       NOT NULL,
    username    VARCHAR(20)  NOT NULL,
    password    VARCHAR(255) NULL,
    created_at  TIMESTAMPTZ  NULL,
    created_by  BIGINT       NULL,
    updated_at  TIMESTAMPTZ  NULL,
    updated_by  BIGINT       NULL,
    deleted_at  TIMESTAMPTZ  NULL,
    deleted_by  BIGINT       NULL,

    CONSTRAINT fk_users_pegawai_id FOREIGN KEY (pegawai_id)
        REFERENCES pegawais (id)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
);

CREATE INDEX idx_user_uuid_hash ON users USING hash (uuid);

CREATE INDEX idx_user_pegawai_id ON users (pegawai_id);

CREATE UNIQUE INDEX idx_user_username_unique ON users (username) WHERE deleted_at IS NULL;
