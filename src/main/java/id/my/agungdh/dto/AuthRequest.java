package id.my.agungdh.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank(message = "Username harus diisi")
        String username,

        @NotBlank(message = "Password harus diisi")
        String password
) {}
