package ru.effective_mobile.shortlinks.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.effective_mobile.shortlinks.dto.LinkRequest;
import ru.effective_mobile.shortlinks.dto.LinkResponse;
import ru.effective_mobile.shortlinks.entity.Link;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LinkMapperTest {

    @DisplayName("Маппер - конвертация сущности в дто")
    @Test
    public void test_entityToDto() {
        var link = Link.builder()
                .id(1L)
                .originalUrl("http://localhost:8080/link/get/all")
                .shortUrl("http://localhost:8080/redirect/ga")
                .alias("ga")
                .createdAt(LocalDate.now())
                .ttl(LocalDate.now().plusDays(5))
                .build();

        var expected = LinkResponse.builder()
                .id(1L)
                .originalUrl("http://localhost:8080/link/get/all")
                .shortUrl("http://localhost:8080/redirect/ga")
                .alias("ga")
                .createdAt(LocalDate.now())
                .ttl(LocalDate.now().plusDays(5))
                .build();

        var actual = LinkMapper.INSTANCE.linkToResponse(link);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @DisplayName("Маппер - конвертация дто в сущность")
    @Test
    public void test_dtoToEntity() {
        var request = LinkRequest.builder()
                .originalUrl("http://localhost:8080/link/get/all")
                .alias("ga")
                .ttl(LocalDate.now().plusDays(5))
                .build();

        var expected = Link.builder()
                .id(null)
                .originalUrl("http://localhost:8080/link/get/all")
                .shortUrl(null)
                .alias("ga")
                .createdAt(null)
                .ttl(LocalDate.now().plusDays(5))
                .build();

        var actual = LinkMapper.INSTANCE.linkFromRequest(request);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}