package ru.effective_mobile.shortlinks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasLength;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.effective_mobile.utils.ControllerUtils.*;
import static ru.effective_mobile.utils.ServiceUtils.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
@Sql(scripts = {
        "/scripts/create_table.sql",
        "/scripts/insert_values.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "/scripts/drop_table.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class LinkControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @DisplayName("Контроллер - создание новой ссылки по переданному alias - успешно")
    @Test
    void test_createShortLink_alias_success() throws Exception {
        var request = objectMapper.writeValueAsString(LINK_REQUEST_WITH_ALIAS);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/link/create")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpectAll(
                        status().isCreated(),
                        content().string("g7"));
    }

    @DisplayName("Контроллер - создание новой ссылки без переданного alias - успешно")
    @Test
    void test_createShortLink_generate_success() throws Exception {
        var request = objectMapper.writeValueAsString(LINK_REQUEST_WITHOUT_ALIAS);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/link/create")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpectAll(
                        status().isCreated(),
                        content().string(hasLength(8)));
    }

    @DisplayName("Контроллер - создание новой ссылки - ошибка")
    @Test
    void test_createShortLink_ConstraintViolationException() throws Exception {
        var request1 = objectMapper.writeValueAsString(LINK_REQUEST_WITH_NOT_BLANK_CHECK);
        var request2 = objectMapper.writeValueAsString(LINK_REQUEST_WITH_URL_check);
        var request3 = objectMapper.writeValueAsString(LINK_REQUEST_WITH_NOT_BLANK_ALIAS_CHECK);
        var request4 = objectMapper.writeValueAsString(LINK_REQUEST_WITH_LARGE_ALIAS_CHECK);
        var request5 = objectMapper.writeValueAsString(LINK_REQUEST_WITH_FUTURE_OR_PRESENT_CHECK);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/link/create")
                        .contentType(APPLICATION_JSON)
                        .content(request1))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/link/create")
                        .contentType(APPLICATION_JSON)
                        .content(request2))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/link/create")
                        .contentType(APPLICATION_JSON)
                        .content(request3))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/link/create")
                        .contentType(APPLICATION_JSON)
                        .content(request4))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/link/create")
                        .contentType(APPLICATION_JSON)
                        .content(request5))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());
    }

    @DisplayName("Контроллер - получение ссылки по id - успешно")
    @Test
    void test_getLinkById_success() throws Exception {
        var response = objectMapper.writeValueAsString(RESPONSE_GA);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/link/get/{id}", 1))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(APPLICATION_JSON),
                        content().json(response));
    }

    @DisplayName("Контроллер - получение ссылки по id - ошибка")
    @Test
    void test_getLinkById_LinkNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/link/get/{id}", 99))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());
    }

    @DisplayName("Контроллер - получение ссылки по id - ошибка")
    @Test
    void test_getLinkById_ConstraintViolationException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/link/get/{id}", 0))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());
    }

    @DisplayName("Контроллер - получение ссылки по alias - успешно")
    @Test
    void test_getLinkByAlias_success() throws Exception {
        var response = objectMapper.writeValueAsString(RESPONSE_GA);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/link/get/alias")
                        .param("alias", "ga"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(APPLICATION_JSON),
                        content().json(response));
    }

    @DisplayName("Контроллер - получение ссылки по alias - ошибка")
    @Test
    void test_getLinkByAlias_LinkNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/link/get/alias")
                        .param("alias", "getAll"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());
    }

    @DisplayName("Контроллер - получение ссылки по alias - ошибка")
    @Test
    void test_getLinkByAlias_ConstraintViolationException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/link/get/alias")
                        .param("alias", " "))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/link/get/alias")
                        .param("alias", "*****ga*****"))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());
    }

    @DisplayName("Контроллер - получение всех ссылок - успешно")
    @Test
    void test_getAllLinks_success() throws Exception {
        var response = objectMapper.writeValueAsString(GET_ALL);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/link/get/all"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(APPLICATION_JSON),
                        content().json(response));
    }

    @DisplayName("Контроллер - получение всех ссылок, найденных по неполному alias - успешно")
    @Test
    void test_getAllLinksByAlias_success() throws Exception {
        var response = objectMapper.writeValueAsString(GET_ALL_BY_ALIAS);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/link/get/all/alias")
                        .param("alias", "g"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(APPLICATION_JSON),
                        content().json(response));
    }

    @DisplayName("Контроллер - получение всех ссылок, найденных по неполному alias - ошибка")
    @Test
    void test_getAllLinksByAlias_ConstraintViolationException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/link/get/all/alias")
                        .param("alias", " "))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/link/get/all/alias")
                        .param("alias", "*****g*****"))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());
    }

    @DisplayName("Контроллер - получение всех ссылок, найденных по ttl - успешно")
    @Test
    void test_getAllLinksByTtl_success() throws Exception {
        var response = objectMapper.writeValueAsString(GET_ALL_BY_TTL);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/link/get/all/ttl")
                        .param("ttl", LocalDate.now().plusDays(1).toString()))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(APPLICATION_JSON),
                        content().json(response));
    }

    @DisplayName("Контроллер - изменение alias ссылки по id - успешно")
    @Test
    void test_updateAliasByLinkId_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/link/update/{id}/alias", 1)
                        .param("alias", "getAll"))
                .andExpect(status().isOk());
    }

    @DisplayName("Контроллер - изменение alias ссылки по id - ошибка")
    @Test
    void test_updateAliasByLinkId_LinkNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/link/update/{id}/alias", 99)
                        .param("alias", "getAll"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());
    }

    @DisplayName("Контроллер - изменение alias ссылки по id - ошибка")
    @Test
    void test_updateAliasByLinkId_ConstraintViolationException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/link/update/{id}/alias", 0)
                        .param("alias", "getAll"))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/link/update/{id}/alias", 1)
                        .param("alias", " "))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/link/update/{id}/alias", 1)
                        .param("alias", "***getAll***"))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());
    }

    @DisplayName("Контроллер - изменение ttl ссылки по id - успешно")
    @Test
    void test_updateTtlByLinkId_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/link/update/{id}/ttl", 1)
                        .param("ttl", LocalDate.now().plusDays(10).toString()))
                .andExpect(status().isOk());
    }

    @DisplayName("Контроллер - изменение ttl ссылки по id - ошибка")
    @Test
    void test_updateTtlByLinkId_LinkNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/link/update/{id}/ttl", 99)
                        .param("ttl", LocalDate.now().plusDays(10).toString()))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());
    }

    @DisplayName("Контроллер - изменение ttl ссылки по id - ошибка")
    @Test
    void test_updateTtlByLinkId_ConstraintViolationException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/link/update/{id}/ttl", 0)
                        .param("ttl", LocalDate.now().plusDays(10).toString()))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/link/update/{id}/ttl", 1)
                        .param("ttl", LocalDate.now().minusDays(1).toString()))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());
    }

    @DisplayName("Контроллер - удаление ссылки по id - успешно")
    @Test
    void test_deleteLinkById_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/link/delete/{id}", 1))
                .andExpect(status().isOk());
    }

    @DisplayName("Контроллер - удаление ссылки по id - ошибка")
    @Test
    void test_deleteLinkById_LinkNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/link/delete/{id}", 99))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());
    }

    @DisplayName("Контроллер - удаление ссылки по id - ошибка")
    @Test
    void test_deleteLinkById_ConstraintViolationException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/link/delete/{id}", 0))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());
    }

    @DisplayName("Контроллер - удаление ссылки по alias - успешно")
    @Test
    void test_deleteLinkByAlias_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/link/delete/alias")
                        .param("alias", "ga"))
                .andExpect(status().isOk());
    }

    @DisplayName("Контроллер - удаление ссылки по alias - ошибка")
    @Test
    void test_deleteLinkByAlias_LinkNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/link/delete/alias")
                        .param("alias", "getAll"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());
    }

    @DisplayName("Контроллер - удаление ссылки по alias - ошибка")
    @Test
    void test_deleteLinkByAlias_ConstraintViolationException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/link/delete/alias")
                        .param("alias", " "))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/link/delete/alias")
                        .param("alias", "*****ga*****"))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());
    }

    @DisplayName("Контроллер - удаление всех ссылок - успешно")
    @Test
    void test_deleteAllLinks_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/link/delete/all"))
                .andExpect(status().isOk());
    }

    @DisplayName("Контроллер - удаление всех ссылок с истекши ttl - успешно")
    @Test
    void test_deleteAllLinksByTtl_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/link/delete/all/ttl"))
                .andExpect(status().isOk());
    }
}