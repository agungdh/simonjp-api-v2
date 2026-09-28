package id.my.agungdh.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ValidationTestRequest(
        @NotBlank(message = "Nama harus diisi")
        String nama,

        @NotNull(message = "Nilai harus diisi")
        @Min(value = 5, message = "Nilai harus lebih dari 5")
        @Max(value = 3, message = "Nilai harus kurang dari 3")
        Integer nilai
) {}
