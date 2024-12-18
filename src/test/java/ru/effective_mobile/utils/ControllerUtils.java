package ru.effective_mobile.utils;

import ru.effective_mobile.shortlinks.dto.LinkRequest;

import java.time.LocalDate;

public class ControllerUtils {

    public static final LinkRequest LINK_REQUEST_WITH_ALIAS_MOCK = LinkRequest.builder()
            .originalUrl("http://localhost:8080/link/get/all")
            .alias("ga")
            .ttl(LocalDate.now().plusDays(5))
            .build();

    public static final LinkRequest LINK_REQUEST_WITHOUT_ALIAS_MOCK = LinkRequest.builder()
            .originalUrl("http://localhost:8080/link/get/all")
            .build();

    public static final LinkRequest LINK_REQUEST_WITH_ALIAS = LinkRequest.builder()
            .originalUrl("http://localhost:8080/link/get/7")
            .alias("g7")
            .ttl(LocalDate.now().plusDays(1))
            .build();

    public static final LinkRequest LINK_REQUEST_WITHOUT_ALIAS = LinkRequest.builder()
            .originalUrl("http://localhost:8080/link/get/8")
            .build();

    public static final LinkRequest LINK_REQUEST_WITH_NOT_BLANK_CHECK = LinkRequest.builder()
            .originalUrl("")
            .build();

    public static final LinkRequest LINK_REQUEST_WITH_URL_check = LinkRequest.builder()
            .originalUrl("string")
            .build();

    public static final LinkRequest LINK_REQUEST_WITH_NOT_BLANK_ALIAS_CHECK = LinkRequest.builder()
            .originalUrl("http://localhost:8080/link/get/7")
            .alias(" ")
            .build();

    public static final LinkRequest LINK_REQUEST_WITH_LARGE_ALIAS_CHECK = LinkRequest.builder()
            .originalUrl("http://localhost:8080/link/get/7")
            .alias("*****g7*****")
            .build();

    public static final LinkRequest LINK_REQUEST_WITH_FUTURE_OR_PRESENT_CHECK = LinkRequest.builder()
            .originalUrl("http://localhost:8080/link/get/7")
            .alias("g7")
            .ttl(LocalDate.now().minusDays(1))
            .build();
}