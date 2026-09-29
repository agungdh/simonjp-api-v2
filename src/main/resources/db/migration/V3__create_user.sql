CREATE UNIQUE INDEX idx_pegawai_nip_full ON pegawai (nip);

CREATE TABLE "user" (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid       UUID         NOT NULL DEFAULT gen_random_uuid(),
    username   VARCHAR(20)  NOT NULL,
    password   VARCHAR(255) NULL,
    created_at TIMESTAMPTZ  NULL,
    created_by BIGINT       NULL,
    updated_at TIMESTAMPTZ  NULL,
    updated_by BIGINT       NULL,
    deleted_at TIMESTAMPTZ  NULL,
    deleted_by BIGINT       NULL,

    CONSTRAINT fk_user_username FOREIGN KEY (username)
        REFERENCES pegawai (nip)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
);

CREATE INDEX idx_user_uuid_hash ON "user" USING hash (uuid);

CREATE INDEX idx_user_username ON "user" (username);

CREATE UNIQUE INDEX idx_user_username_unique ON "user" (username) WHERE deleted_at IS NULL;
