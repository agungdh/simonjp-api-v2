package id.my.agungdh.dto;

import java.time.LocalDateTime;

public record PegawaiResponse(
        Long id,
        String nip,
        String nama,
        String jabatan,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}