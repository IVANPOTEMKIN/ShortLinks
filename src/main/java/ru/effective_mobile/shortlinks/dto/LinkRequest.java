package ru.effective_mobile.shortlinks.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@Schema(name = "Запрос", description = "Запрос на создание ссылки")
@Builder
public record LinkRequest(@NotBlank @URL
                          @Schema(example = "http://localhost:8080/link/get/all")
                          String originalUrl,

                          @Pattern(regexp = "^[a-zA-Z0-9]{1,10}$")
                          @Schema(example = "getAll")
                          String alias,

                          @FutureOrPresent
                          @Schema(example = "2024-12-30")
                          LocalDate ttl) {
}