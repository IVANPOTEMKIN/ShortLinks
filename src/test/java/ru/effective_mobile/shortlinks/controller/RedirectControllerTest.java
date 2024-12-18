package ru.effective_mobile.shortlinks.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.effective_mobile.shortlinks.repository.LinkRepository;
import ru.effective_mobile.shortlinks.service.impl.LinkServiceImpl;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RedirectController.class)
public class RedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LinkRepository linkRepository;

    @SpyBean
    private LinkServiceImpl linkService;

    @DisplayName("Контроллер - перенаправление метода - успешно")
    @Test
    public void test_redirect_success() throws Exception {
        when(linkRepository.findOriginalUrl(anyString()))
                .thenReturn(Optional.of("http://localhost:8080/link/get/all"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/redirect/ga"))
                .andExpect(status().isFound());

        verify(linkRepository, times(1))
                .findOriginalUrl(anyString());
        verify(linkService, times(1))
                .getOriginalLink(anyString());
    }
}