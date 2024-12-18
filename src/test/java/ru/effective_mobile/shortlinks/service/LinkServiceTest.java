package ru.effective_mobile.shortlinks.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.effective_mobile.shortlinks.entity.Link;
import ru.effective_mobile.shortlinks.exception.LinkNotFoundException;
import ru.effective_mobile.shortlinks.repository.LinkRepository;
import ru.effective_mobile.shortlinks.service.impl.LinkServiceImpl;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.effective_mobile.utils.RepositoryUtils.*;
import static ru.effective_mobile.utils.ServiceUtils.*;

@ExtendWith(MockitoExtension.class)
class LinkServiceTest {

    @Mock
    private LinkRepository linkRepository;

    @InjectMocks
    private LinkServiceImpl linkService;

    @DisplayName("Сервис - создание новой ссылки - успешно")
    @Test
    void test_createShortLink_success() {
        when(linkRepository.save(any(Link.class)))
                .thenReturn(GA);

        var actual = linkService.createShortLink(REQUEST);

        assertEquals("ga", actual);

        verify(linkRepository, times(1))
                .save(any(Link.class));
    }

    @DisplayName("Сервис - получение ссылки по id - успешно")
    @Test
    void test_getLinkById_success() {
        when(linkRepository.findById(anyLong()))
                .thenReturn(Optional.of(GA));

        var actual = linkService.getLinkById(1L);

        assertEquals(RESPONSE_GA, actual);

        verify(linkRepository, times(1))
                .findById(anyLong());
    }

    @DisplayName("Сервис - получение ссылки по id - ошибка")
    @Test
    void test_getLinkById_LinkNotFoundException() {
        assertThrows(LinkNotFoundException.class,
                () -> linkService.getLinkById(1L));

        verify(linkRepository, times(1))
                .findById(anyLong());
    }

    @DisplayName("Сервис - получение ссылки по alias - успешно")
    @Test
    void test_getLinkByAlias_success() {
        when(linkRepository.findByAlias(anyString()))
                .thenReturn(Optional.of(GA));

        var actual = linkService.getLinkByAlias("ga");

        assertEquals(RESPONSE_GA, actual);

        verify(linkRepository, times(1))
                .findByAlias(anyString());
    }

    @DisplayName("Сервис - получение ссылки по alias - ошибка")
    @Test
    void test_getLinkByAlias_LinkNotFoundException() {
        assertThrows(LinkNotFoundException.class,
                () -> linkService.getLinkByAlias(anyString()));

        verify(linkRepository, times(1))
                .findByAlias(anyString());
    }

    @DisplayName("Сервис - получение всех ссылок - успешно")
    @Test
    void test_getAllLinks_success() {
        when(linkRepository.findAll())
                .thenReturn(FIND_ALL);

        assertEquals(GET_ALL, linkService.getAllLinks());

        verify(linkRepository, times(1))
                .findAll();
    }

    @DisplayName("Сервис - получение всех ссылок, найденных по неполному alias - успешно")
    @Test
    void test_getAllLinksByAlias_success() {
        when(linkRepository.findAllByAliasIsContainingIgnoreCaseOrderByAlias(anyString()))
                .thenReturn(FIND_ALL_BY_ALIAS);

        assertEquals(GET_ALL_BY_ALIAS, linkService.getAllLinksByAlias("g"));

        verify(linkRepository, times(1))
                .findAllByAliasIsContainingIgnoreCaseOrderByAlias(anyString());
    }

    @DisplayName("Сервис - получение всех ссылок, найденных по ttl - успешно")
    @Test
    void test_getAllLinksByTtl_success() {
        when(linkRepository.findAllByTtlLessThanEqualOrderByTtl(any(LocalDate.class)))
                .thenReturn(FIND_ALL_BY_TTL);

        assertEquals(GET_ALL_BY_TTL, linkService.getAllLinksByTtl(LocalDate.now().plusDays(1)));

        verify(linkRepository, times(1))
                .findAllByTtlLessThanEqualOrderByTtl(any(LocalDate.class));
    }

    @DisplayName("Сервис - получение полной ссылки по alias - успешно")
    @Test
    void test_getOriginalLink_success() {
        var expected = "http://localhost:8080/link/get/all";

        when(linkRepository.findOriginalUrl(anyString()))
                .thenReturn(Optional.of(expected));

        assertEquals(expected, linkService.getOriginalLink("ga"));

        verify(linkRepository, times(1))
                .findOriginalUrl(anyString());
    }

    @DisplayName("Сервис - получение полной ссылки по alias - ошибка")
    @Test
    void test_getOriginalLink_LinkNotFoundException() {
        assertThrows(LinkNotFoundException.class,
                () -> linkService.getOriginalLink("ga"));

        verify(linkRepository, times(1))
                .findOriginalUrl(anyString());
    }

    @DisplayName("Сервис - изменение alias ссылки по id - успешно")
    @Test
    void test_updateAliasByLinkId_success() {
        when(linkRepository.updateAlias(anyLong(), anyString(), anyString()))
                .thenReturn(1);

        linkService.updateAliasByLinkId(1L, "getAll");

        verify(linkRepository, times(1))
                .updateAlias(anyLong(), anyString(), anyString());
    }

    @DisplayName("Сервис - изменение alias ссылки по id - ошибка")
    @Test
    void test_updateAliasByLinkId_LinkNotFoundException() {
        assertThrows(LinkNotFoundException.class,
                () -> linkService.updateAliasByLinkId(1L, "getAll"));

        verify(linkRepository, times(1))
                .updateAlias(anyLong(), anyString(), anyString());
    }

    @DisplayName("Сервис - изменение ttl ссылки по id - успешно")
    @Test
    void test_updateTtlByLinkId_success() {
        when(linkRepository.updateTtl(anyLong(), any(LocalDate.class)))
                .thenReturn(1);

        linkService.updateTtlByLinkId(1L, LocalDate.now().plusDays(1));

        verify(linkRepository, times(1))
                .updateTtl(anyLong(), any(LocalDate.class));
    }

    @DisplayName("Сервис - изменение ttl ссылки по id - ошибка")
    @Test
    void test_updateTtlByLinkId_LinkNotFoundException() {
        assertThrows(LinkNotFoundException.class,
                () -> linkService.updateTtlByLinkId(1L, LocalDate.now().plusDays(1)));

        verify(linkRepository, times(1))
                .updateTtl(anyLong(), any(LocalDate.class));
    }

    @DisplayName("Сервис - удаление ссылки по id - успешно")
    @Test
    void test_deleteLinkById_success() {
        when(linkRepository.deleteLinkById(anyLong()))
                .thenReturn(1);

        linkService.deleteLinkById(1L);

        verify(linkRepository, times(1))
                .deleteLinkById(anyLong());
    }

    @DisplayName("Сервис - удаление ссылки по id - ошибка")
    @Test
    void test_deleteLinkById_LinkNotFoundException() {
        assertThrows(LinkNotFoundException.class,
                () -> linkService.deleteLinkById(1L));

        verify(linkRepository, times(1))
                .deleteLinkById(anyLong());
    }

    @DisplayName("Сервис - удаление ссылки по alias - успешно")
    @Test
    void test_deleteLinkByAlias_success() {
        when(linkRepository.deleteLinkByAlias(anyString()))
                .thenReturn(1);

        linkService.deleteLinkByAlias("ga");

        verify(linkRepository, times(1))
                .deleteLinkByAlias(anyString());
    }

    @DisplayName("Сервис - удаление ссылки по alias - ошибка")
    @Test
    void test_deleteLinkByAlias_LinkNotFoundException() {
        assertThrows(LinkNotFoundException.class,
                () -> linkService.deleteLinkByAlias("ga"));

        verify(linkRepository, times(1))
                .deleteLinkByAlias(anyString());
    }

    @DisplayName("Сервис - удаление всех ссылок - успешно")
    @Test
    void test_deleteAllLinks_success() {
        doNothing().when(linkRepository)
                .deleteAll();

        linkService.deleteAllLinks();

        verify(linkRepository, times(1))
                .deleteAll();

    }

    @DisplayName("Сервис - удаление всех ссылок с истекшим ttl - успешно")
    @Test
    void test_deleteAllLinksByTtl_success() {
        doNothing().when(linkRepository)
                .deleteAllLinksByTtlBefore(any(LocalDate.class));

        linkService.deleteAllLinksByTtl();

        verify(linkRepository, times(1))
                .deleteAllLinksByTtlBefore(any(LocalDate.class));
    }
}