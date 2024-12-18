package ru.effective_mobile.utils;

import ru.effective_mobile.shortlinks.entity.Link;

import java.time.LocalDate;
import java.util.List;

public class RepositoryUtils {

    public static final Link GA = Link.builder()
            .id(1L)
            .originalUrl("http://localhost:8080/link/get/all")
            .shortUrl("http://localhost:8080/redirect/ga")
            .alias("ga")
            .createdAt(LocalDate.now())
            .ttl(LocalDate.now().plusDays(5))
            .build();

    public static final Link G1 = Link.builder()
            .id(2L)
            .originalUrl("http://localhost:8080/link/get/1")
            .shortUrl("http://localhost:8080/redirect/g1")
            .alias("g1")
            .createdAt(LocalDate.now())
            .ttl(LocalDate.now().plusDays(1))
            .build();

    public static final Link G2 = Link.builder()
            .id(3L)
            .originalUrl("http://localhost:8080/link/get/2")
            .shortUrl("http://localhost:8080/redirect/g2")
            .alias("g2")
            .createdAt(LocalDate.now())
            .ttl(LocalDate.now().plusDays(1))
            .build();

    public static final Link G3 = Link.builder()
            .id(4L)
            .originalUrl("http://localhost:8080/link/get/3")
            .shortUrl("http://localhost:8080/redirect/g3")
            .alias("g3")
            .createdAt(LocalDate.now())
            .ttl(LocalDate.now().plusDays(1))
            .build();

    public static final Link G4 = Link.builder()
            .id(5L)
            .originalUrl("http://localhost:8080/link/get/4")
            .shortUrl("http://localhost:8080/redirect/g4")
            .alias("g4")
            .createdAt(LocalDate.now().minusDays(2))
            .ttl(LocalDate.now().minusDays(1))
            .build();

    public static final Link G5 = Link.builder()
            .id(6L)
            .originalUrl("http://localhost:8080/link/get/5")
            .shortUrl("http://localhost:8080/redirect/g5")
            .alias("g5")
            .createdAt(LocalDate.now().minusDays(2))
            .ttl(LocalDate.now().minusDays(1))
            .build();

    public static final Link G6 = Link.builder()
            .id(7L)
            .originalUrl("http://localhost:8080/link/get/6")
            .shortUrl("http://localhost:8080/redirect/g6")
            .alias("g6")
            .createdAt(LocalDate.now().minusDays(2))
            .ttl(LocalDate.now().minusDays(1))
            .build();

    public static final Link GG = Link.builder()
            .id(8L)
            .originalUrl("http://localhost:8080/link/get/all/alias?alias=g")
            .shortUrl("http://localhost:8080/redirect/gg")
            .alias("gg")
            .createdAt(LocalDate.now())
            .ttl(LocalDate.now().plusDays(10))
            .build();

    public static final List<Link> FIND_ALL_BY_ALIAS = List.of(G1, G2, G3, G4, G5, G6, GA, GG);
    public static final List<Link> FIND_ALL_BY_TTL = List.of(G4, G5, G6, G1, G2, G3);
    public static final List<Link> FIND_ALL = List.of(GA, G1, G2, G3, G4, G5, G6, GG);

    public static final Link NEW = Link.builder()
            .id(9L)
            .originalUrl("http://localhost:8080/link/get/7")
            .shortUrl("http://localhost:8080/redirect/g7")
            .alias("g7")
            .createdAt(LocalDate.now())
            .ttl(LocalDate.now().plusDays(1))
            .build();
}