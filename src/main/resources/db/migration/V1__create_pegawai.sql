CREATE TABLE pegawai (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid       UUID         NOT NULL DEFAULT gen_random_uuid(),
    nip        VARCHAR(20)  NOT NULL,
    nama       VARCHAR(100) NOT NULL,
    jabatan    VARCHAR(100) NOT NULL,
    created_at TIMESTAMPTZ  NULL,
    created_by BIGINT       NULL,
    updated_at TIMESTAMPTZ  NULL,
    updated_by BIGINT       NULL,
    deleted_at TIMESTAMPTZ  NULL,
    deleted_by BIGINT       NULL
);

CREATE INDEX idx_pegawai_uuid_hash ON pegawai USING hash (uuid);

CREATE UNIQUE INDEX idx_pegawai_nip ON pegawai (nip) WHERE deleted_at IS NULL;
