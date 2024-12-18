package ru.effective_mobile.shortlinks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.effective_mobile.shortlinks.dto.LinkRequest;
import ru.effective_mobile.shortlinks.entity.Link;
import ru.effective_mobile.shortlinks.repository.LinkRepository;
import ru.effective_mobile.shortlinks.service.impl.LinkServiceImpl;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.Matchers.hasLength;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.effective_mobile.utils.ControllerUtils.*;
import static ru.effective_mobile.utils.RepositoryUtils.*;
import static ru.effective_mobile.utils.ServiceUtils.*;

@WebMvcTest(controllers = LinkController.class)
public class LinkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LinkRepository linkRepository;

    @SpyBean
    private LinkServiceImpl linkService;

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
        var request = objectMapper.writeValueAsString(LINK_REQUEST_WITH_ALIAS_MOCK);

        when(linkRepository.save(any(Link.class)))
                .thenReturn(GA);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/link/create")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpectAll(
                        status().isCreated(),
                        content().string("ga"));

        verify(linkRepository, times(1))
                .save(any(Link.class));
        verify(linkService, times(1))
                .createShortLink(any(LinkRequest.class));
    }

    @DisplayName("Контроллер - создание новой ссылки без переданного alias - успешно")
    @Test
    void test_createShortLink_generate_success() throws Exception {
        var request = objectMapper.writeValueAsString(LINK_REQUEST_WITHOUT_ALIAS_MOCK);

        when(linkRepository.save(any(Link.class)))
                .thenReturn(GA);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/link/create")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpectAll(
                        status().isCreated(),
                        content().string(hasLength(8)));

        verify(linkRepository, times(1))
                .save(any(Link.class));
        verify(linkService, times(1))
                .createShortLink(any(LinkRequest.class));
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

        verify(linkRepository, times(0))
                .save(any(Link.class));
        verify(linkService, times(0))
                .createShortLink(any(LinkRequest.class));
    }

    @DisplayName("Контроллер - получение ссылки по id - успешно")
    @Test
    void test_getLinkById_success() throws Exception {
        when(linkRepository.findById(anyLong()))
                .thenReturn(Optional.of(GA));

        var response = objectMapper.writeValueAsString(RESPONSE_GA);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/link/get/{id}", 1))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(APPLICATION_JSON),
                        content().json(response));

        verify(linkRepository, times(1))
                .findById(anyLong());
        verify(linkService, times(1))
                .getLinkById(anyLong());
    }

    @DisplayName("Контроллер - получение ссылки по id - ошибка")
    @Test
    void test_getLinkById_LinkNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/link/get/{id}", 1))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());

        verify(linkRepository, times(1))
                .findById(anyLong());
        verify(linkService, times(1))
                .getLinkById(anyLong());
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

        verify(linkRepository, times(0))
                .findById(anyLong());
        verify(linkService, times(0))
                .getLinkById(anyLong());
    }

    @DisplayName("Контроллер - получение ссылки по alias - успешно")
    @Test
    void test_getLinkByAlias_success() throws Exception {
        when(linkRepository.findByAlias(anyString()))
                .thenReturn(Optional.of(GA));

        var response = objectMapper.writeValueAsString(RESPONSE_GA);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/link/get/alias")
                        .param("alias", "ga"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(APPLICATION_JSON),
                        content().json(response));

        verify(linkRepository, times(1))
                .findByAlias(anyString());
        verify(linkService, times(1))
                .getLinkByAlias(anyString());
    }

    @DisplayName("Контроллер - получение ссылки по alias - ошибка")
    @Test
    void test_getLinkByAlias_LinkNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/link/get/alias")
                        .param("alias", "ga"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());

        verify(linkRepository, times(1))
                .findByAlias(anyString());
        verify(linkService, times(1))
                .getLinkByAlias(anyString());
    }

    @DisplayName("Контроллер - получение ссылки по alias - ошибка")
    @Test
    void test_getLinkByAlias_ConstraintViolationException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/link/get/alias")
                        .param("alias", ""))
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

        verify(linkRepository, times(0))
                .findByAlias(anyString());
        verify(linkService, times(0))
                .getLinkByAlias(anyString());
    }

    @DisplayName("Контроллер - получение всех ссылок - успешно")
    @Test
    void test_getAllLinks_success() throws Exception {
        when(linkRepository.findAll())
                .thenReturn(FIND_ALL);

        var response = objectMapper.writeValueAsString(GET_ALL);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/link/get/all"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(APPLICATION_JSON),
                        content().json(response));

        verify(linkRepository, times(1))
                .findAll();
        verify(linkService, times(1))
                .getAllLinks();
    }

    @DisplayName("Контроллер - получение всех ссылок, найденных по неполному alias - успешно")
    @Test
    void test_getAllLinksByAlias_success() throws Exception {
        when(linkRepository.findAllByAliasIsContainingIgnoreCaseOrderByAlias(anyString()))
                .thenReturn(FIND_ALL_BY_ALIAS);

        var response = objectMapper.writeValueAsString(GET_ALL_BY_ALIAS);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/link/get/all/alias")
                        .param("alias", "g"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(APPLICATION_JSON),
                        content().json(response));

        verify(linkRepository, times(1))
                .findAllByAliasIsContainingIgnoreCaseOrderByAlias(anyString());
        verify(linkService, times(1))
                .getAllLinksByAlias(anyString());
    }

    @DisplayName("Контроллер - получение всех ссылок, найденных по неполному alias - ошибка")
    @Test
    void test_getAllLinksByAlias_ConstraintViolationException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/link/get/all/alias")
                        .param("alias", ""))
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

        verify(linkRepository, times(0))
                .findAllByAliasIsContainingIgnoreCaseOrderByAlias(anyString());
        verify(linkService, times(0))
                .getAllLinksByAlias(anyString());
    }

    @DisplayName("Контроллер - получение всех ссылок, найденных по ttl - успешно")
    @Test
    void test_getAllLinksByTtl_success() throws Exception {
        when(linkRepository.findAllByTtlLessThanEqualOrderByTtl(any(LocalDate.class)))
                .thenReturn(FIND_ALL_BY_TTL);

        var response = objectMapper.writeValueAsString(GET_ALL_BY_TTL);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/link/get/all/ttl")
                        .param("ttl", LocalDate.now().plusDays(1).toString()))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(APPLICATION_JSON),
                        content().json(response));

        verify(linkRepository, times(1))
                .findAllByTtlLessThanEqualOrderByTtl(any(LocalDate.class));
        verify(linkService, times(1))
                .getAllLinksByTtl(any(LocalDate.class));
    }

    @DisplayName("Контроллер - изменение alias ссылки по id - успешно")
    @Test
    void test_updateAliasByLinkId_success() throws Exception {
        when(linkRepository.updateAlias(anyLong(), anyString(), anyString()))
                .thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/link/update/{id}/alias", 1)
                        .param("alias", "getAll"))
                .andExpect(status().isOk());

        verify(linkRepository, times(1))
                .updateAlias(anyLong(), anyString(), anyString());
        verify(linkService, times(1))
                .updateAliasByLinkId(anyLong(), anyString());
    }

    @DisplayName("Контроллер - изменение alias ссылки по id - ошибка")
    @Test
    void test_updateAliasByLinkId_LinkNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/link/update/{id}/alias", 1)
                        .param("alias", "getAll"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());

        verify(linkRepository, times(1))
                .updateAlias(anyLong(), anyString(), anyString());
        verify(linkService, times(1))
                .updateAliasByLinkId(anyLong(), anyString());
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
                        .param("alias", ""))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/link/update/{id}/alias", 1)
                        .param("alias", "***getAll***o"))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());

        verify(linkRepository, times(0))
                .updateAlias(anyLong(), anyString(), anyString());
        verify(linkService, times(0))
                .updateAliasByLinkId(anyLong(), anyString());
    }

    @DisplayName("Контроллер - изменение ttl ссылки по id - успешно")
    @Test
    void test_updateTtlByLinkId_success() throws Exception {
        when(linkRepository.updateTtl(anyLong(), any(LocalDate.class)))
                .thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/link/update/{id}/ttl", 1)
                        .param("ttl", LocalDate.now().plusDays(10).toString()))
                .andExpect(status().isOk());

        verify(linkRepository, times(1))
                .updateTtl(anyLong(), any(LocalDate.class));
        verify(linkService, times(1))
                .updateTtlByLinkId(anyLong(), any(LocalDate.class));
    }

    @DisplayName("Контроллер - изменение ttl ссылки по id - ошибка")
    @Test
    void test_updateTtlByLinkId_LinkNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/link/update/{id}/ttl", 1)
                        .param("ttl", LocalDate.now().plusDays(10).toString()))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());

        verify(linkRepository, times(1))
                .updateTtl(anyLong(), any(LocalDate.class));
        verify(linkService, times(1))
                .updateTtlByLinkId(anyLong(), any(LocalDate.class));
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

        verify(linkRepository, times(0))
                .updateTtl(anyLong(), any(LocalDate.class));
        verify(linkService, times(0))
                .updateTtlByLinkId(anyLong(), any(LocalDate.class));
    }

    @DisplayName("Контроллер - удаление ссылки по id - успешно")
    @Test
    void test_deleteLinkById_success() throws Exception {
        when(linkRepository.deleteLinkById(anyLong()))
                .thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/link/delete/{id}", 1))
                .andExpect(status().isOk());

        verify(linkRepository, times(1))
                .deleteLinkById(anyLong());
        verify(linkService, times(1))
                .deleteLinkById(anyLong());
    }

    @DisplayName("Контроллер - удаление ссылки по id - ошибка")
    @Test
    void test_deleteLinkById_LinkNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/link/delete/{id}", 1))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());

        verify(linkRepository, times(1))
                .deleteLinkById(anyLong());
        verify(linkService, times(1))
                .deleteLinkById(anyLong());
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

        verify(linkRepository, times(0))
                .deleteLinkById(anyLong());
        verify(linkService, times(0))
                .deleteLinkById(anyLong());
    }

    @DisplayName("Контроллер - удаление ссылки по alias - успешно")
    @Test
    void test_deleteLinkByAlias_success() throws Exception {
        when(linkRepository.deleteLinkByAlias(anyString()))
                .thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/link/delete/alias")
                        .param("alias", "ga"))
                .andExpect(status().isOk());

        verify(linkRepository, times(1))
                .deleteLinkByAlias(anyString());
        verify(linkService, times(1))
                .deleteLinkByAlias(anyString());
    }

    @DisplayName("Контроллер - удаление ссылки по alias - ошибка")
    @Test
    void test_deleteLinkByAlias_LinkNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/link/delete/alias")
                        .param("alias", "ga"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.message").isNotEmpty());

        verify(linkRepository, times(1))
                .deleteLinkByAlias(anyString());
        verify(linkService, times(1))
                .deleteLinkByAlias(anyString());
    }

    @DisplayName("Контроллер - удаление ссылки по alias - ошибка")
    @Test
    void test_deleteLinkByAlias_ConstraintViolationException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/link/delete/alias")
                        .param("alias", ""))
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

        verify(linkRepository, times(0))
                .deleteLinkByAlias(anyString());
        verify(linkService, times(0))
                .deleteLinkByAlias(anyString());
    }

    @DisplayName("Контроллер - удаление всех ссылок - успешно")
    @Test
    void test_deleteAllLinks_success() throws Exception {
        doNothing().when(linkRepository)
                .deleteAll();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/link/delete/all"))
                .andExpect(status().isOk());

        verify(linkRepository, times(1))
                .deleteAll();
        verify(linkService, times(1))
                .deleteAllLinks();
    }

    @DisplayName("Контроллер - удаление всех ссылок с истекши ttl - успешно")
    @Test
    void test_deleteAllLinksByTtl_success() throws Exception {
        doNothing().when(linkRepository)
                .deleteAllLinksByTtlBefore(LocalDate.now());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/link/delete/all/ttl"))
                .andExpect(status().isOk());

        verify(linkRepository, times(1))
                .deleteAllLinksByTtlBefore(LocalDate.now());
        verify(linkService, times(1))
                .deleteAllLinksByTtl();
    }
}