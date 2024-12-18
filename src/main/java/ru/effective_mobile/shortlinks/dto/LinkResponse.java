package ru.effective_mobile.shortlinks.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Schema(name = "Ответ", description = "Ответ с информацией о ссылке")
@Builder
public record LinkResponse(@Schema(example = "1")
                           Long id,

                           @Schema(example = "http://localhost:8080/link/get/all")
                           String originalUrl,

                           @Schema(example = "http://localhost:8080/redirect/getAll")
                           String shortUrl,

                           @Schema(example = "getAll")
                           String alias,

                           @Schema(example = "2024-12-20")
                           LocalDate createdAt,

                           @Schema(example = "2024-12-30")
                           LocalDate ttl) {
}