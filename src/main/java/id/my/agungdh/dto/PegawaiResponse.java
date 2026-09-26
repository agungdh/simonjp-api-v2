package id.my.agungdh.dto;

import java.util.UUID;

public record PegawaiResponse(
        UUID uuid,
        String nip,
        String nama,
        String jabatan
) {}
