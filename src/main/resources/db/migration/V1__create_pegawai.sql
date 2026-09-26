CREATE TABLE pegawai (
    id         BIGSERIAL PRIMARY KEY,
    nip        VARCHAR(20)  NOT NULL UNIQUE,
    nama       VARCHAR(100) NOT NULL,
    jabatan    VARCHAR(100) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at TIMESTAMP    NOT NULL DEFAULT now()
);