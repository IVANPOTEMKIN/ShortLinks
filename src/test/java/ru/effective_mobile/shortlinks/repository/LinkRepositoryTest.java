package ru.effective_mobile.shortlinks.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.effective_mobile.utils.RepositoryUtils.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Sql(scripts = {
        "/scripts/create_table.sql",
        "/scripts/insert_values.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "/scripts/drop_table.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class LinkRepositoryTest {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    public LinkRepository linkRepository;

    @Test
    @DisplayName("Репозиторий - сохранение нового элемента")
    void test_save() {
        var expected = Optional.of(NEW);
        var actual = linkRepository.findById(9L);
        var actualSize = linkRepository.findAll().size();

        assertFalse(actual.isPresent());
        assertEquals(8, actualSize);

        linkRepository.save(NEW);

        actualSize = linkRepository.findAll().size();
        actual = linkRepository.findById(9L);

        assertTrue(actual.isPresent());
        assertEquals(expected, actual);
        assertEquals(9, actualSize);
    }

    @Test
    @DisplayName("Репозиторий - получение элемента по его id")
    void test_findById() {
        var expected = Optional.of(GA);
        var actual = linkRepository.findById(1L);

        assertTrue(actual.isPresent());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Репозиторий - получение элемента по его alias")
    void test_findByAlias() {
        var expected = Optional.of(GA);
        var actual = linkRepository.findByAlias("ga");

        assertTrue(actual.isPresent());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Репозиторий - получение списка всех элементов")
    void test_findAll() {
        var actual = linkRepository.findAll();
        assertEquals(FIND_ALL, actual);
    }

    @Test
    @DisplayName("Репозиторий - получение списка всех элементов, найденных по неполному alias")
    void test_findAllByAliasIsContainingIgnoreCaseOrderByAlias() {
        var actual = linkRepository.findAllByAliasIsContainingIgnoreCaseOrderByAlias("g");
        assertEquals(FIND_ALL_BY_ALIAS, actual);
    }

    @Test
    @DisplayName("Репозиторий - получение списка всех элементов, найденных по ttl")
    void test_findAllByTtlLessThanEqualOrderByTtl() {
        var actual = linkRepository.findAllByTtlLessThanEqualOrderByTtl(LocalDate.now().plusDays(1));
        assertEquals(FIND_ALL_BY_TTL, actual);
    }

    @DisplayName("Репозиторий - получение полной ссылки элемента по его alias")
    @Test
    void test_findOriginalUrl() {
        var expected = Optional.of("http://localhost:8080/link/get/all");
        var actual = linkRepository.findOriginalUrl("ga");
        assertEquals(expected, actual);
    }

    @DisplayName("Репозиторий - изменение alias элемента по его id")
    @Test
    void test_updateAlias() {
        var actual = linkRepository.updateAlias(1L, "getAll", "http://localhost:8080/redirect/getAll");
        assertEquals(1, actual);
    }

    @DisplayName("Репозиторий - изменение ttl элемента по его id")
    @Test
    void test_updateTtl() {
        var actual = linkRepository.updateTtl(1L, LocalDate.now().plusDays(10));
        assertEquals(1, actual);
    }

    @DisplayName("Репозиторий - удаление элемента по его id")
    @Test
    void test_deleteLinkById() {
        var actual = linkRepository.findAll().size();
        assertEquals(8, actual);

        linkRepository.deleteLinkById(1L);

        actual = linkRepository.findAll().size();
        assertEquals(7, actual);
    }

    @DisplayName("Репозиторий - удаление элемента по его alias")
    @Test
    void test_deleteLinkByAlias() {
        var actual = linkRepository.findAll().size();
        assertEquals(8, actual);

        linkRepository.deleteLinkByAlias("ga");

        actual = linkRepository.findAll().size();
        assertEquals(7, actual);
    }

    @DisplayName("Репозиторий - удаление всех элементов")
    @Test
    void test_deleteAll() {
        var actual = linkRepository.findAll().size();
        assertEquals(8, actual);

        linkRepository.deleteAll();

        actual = linkRepository.findAll().size();
        assertEquals(0, actual);
    }

    @DisplayName("Репозиторий - удаление всех элементов с истекшим ttl")
    @Test
    void test_deleteAllByTtlBefore() {
        var actual = linkRepository.findAll().size();
        assertEquals(8, actual);

        linkRepository.deleteAllLinksByTtlBefore(LocalDate.now());

        actual = linkRepository.findAll().size();
        assertEquals(5, actual);
    }
}