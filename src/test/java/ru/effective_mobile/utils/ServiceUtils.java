package ru.effective_mobile.utils;

import ru.effective_mobile.shortlinks.dto.LinkRequest;
import ru.effective_mobile.shortlinks.dto.LinkResponse;

import java.time.LocalDate;
import java.util.List;

public class ServiceUtils {

    public static final LinkRequest REQUEST = LinkRequest.builder()
            .originalUrl("http://localhost:8080/link/get/all")
            .alias("ga")
            .ttl(LocalDate.now().plusDays(5))
            .build();

    public static final LinkResponse RESPONSE_GA = LinkResponse.builder()
            .id(1L)
            .originalUrl("http://localhost:8080/link/get/all")
            .shortUrl("http://localhost:8080/redirect/ga")
            .alias("ga")
            .createdAt(LocalDate.now())
            .ttl(LocalDate.now().plusDays(5))
            .build();

    public static final LinkResponse RESPONSE_G1 = LinkResponse.builder()
            .id(2L)
            .originalUrl("http://localhost:8080/link/get/1")
            .shortUrl("http://localhost:8080/redirect/g1")
            .alias("g1")
            .createdAt(LocalDate.now())
            .ttl(LocalDate.now().plusDays(1))
            .build();

    public static final LinkResponse RESPONSE_G2 = LinkResponse.builder()
            .id(3L)
            .originalUrl("http://localhost:8080/link/get/2")
            .shortUrl("http://localhost:8080/redirect/g2")
            .alias("g2")
            .createdAt(LocalDate.now())
            .ttl(LocalDate.now().plusDays(1))
            .build();

    public static final LinkResponse RESPONSE_G3 = LinkResponse.builder()
            .id(4L)
            .originalUrl("http://localhost:8080/link/get/3")
            .shortUrl("http://localhost:8080/redirect/g3")
            .alias("g3")
            .createdAt(LocalDate.now())
            .ttl(LocalDate.now().plusDays(1))
            .build();

    public static final LinkResponse RESPONSE_G4 = LinkResponse.builder()
            .id(5L)
            .originalUrl("http://localhost:8080/link/get/4")
            .shortUrl("http://localhost:8080/redirect/g4")
            .alias("g4")
            .createdAt(LocalDate.now().minusDays(2))
            .ttl(LocalDate.now().minusDays(1))
            .build();

    public static final LinkResponse RESPONSE_G5 = LinkResponse.builder()
            .id(6L)
            .originalUrl("http://localhost:8080/link/get/5")
            .shortUrl("http://localhost:8080/redirect/g5")
            .alias("g5")
            .createdAt(LocalDate.now().minusDays(2))
            .ttl(LocalDate.now().minusDays(1))
            .build();

    public static final LinkResponse RESPONSE_G6 = LinkResponse.builder()
            .id(7L)
            .originalUrl("http://localhost:8080/link/get/6")
            .shortUrl("http://localhost:8080/redirect/g6")
            .alias("g6")
            .createdAt(LocalDate.now().minusDays(2))
            .ttl(LocalDate.now().minusDays(1))
            .build();

    public static final LinkResponse RESPONSE_GG = LinkResponse.builder()
            .id(8L)
            .originalUrl("http://localhost:8080/link/get/all/alias?alias=g")
            .shortUrl("http://localhost:8080/redirect/gg")
            .alias("gg")
            .createdAt(LocalDate.now())
            .ttl(LocalDate.now().plusDays(10))
            .build();

    public static final List<LinkResponse> GET_ALL_BY_ALIAS = List.of(RESPONSE_G1, RESPONSE_G2, RESPONSE_G3, RESPONSE_G4, RESPONSE_G5, RESPONSE_G6, RESPONSE_GA, RESPONSE_GG);
    public static final List<LinkResponse> GET_ALL_BY_TTL = List.of(RESPONSE_G4, RESPONSE_G5, RESPONSE_G6, RESPONSE_G1, RESPONSE_G2, RESPONSE_G3);
    public static final List<LinkResponse> GET_ALL = List.of(RESPONSE_GA, RESPONSE_G1, RESPONSE_G2, RESPONSE_G3, RESPONSE_G4, RESPONSE_G5, RESPONSE_G6, RESPONSE_GG);
}