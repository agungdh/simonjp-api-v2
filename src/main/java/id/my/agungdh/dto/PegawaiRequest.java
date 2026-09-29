package id.my.agungdh.dto;

import id.my.agungdh.validation.Unique;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PegawaiRequest(
        @NotBlank(message = "NIP harus diisi")
        @Pattern(regexp = "\\d{18}", message = "NIP harus 18 digit angka")
        @Unique(entity = "id.my.agungdh.entity.Pegawai", field = "nip", message = "NIP sudah ada")
        String nip,

        @NotBlank(message = "Nama harus diisi")
        @Size(min = 2, max = 255, message = "Nama harus 2-255 karakter")
        String nama,

        @NotBlank(message = "Jabatan harus diisi")
        @Size(min = 2, max = 255, message = "Jabatan harus 2-255 karakter")
        String jabatan,

        @Size(max = 255, message = "Password maksimal 255 karakter")
        String password
) {}
