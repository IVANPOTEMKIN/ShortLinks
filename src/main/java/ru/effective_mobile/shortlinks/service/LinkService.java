package ru.effective_mobile.shortlinks.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import ru.effective_mobile.shortlinks.dto.LinkRequest;
import ru.effective_mobile.shortlinks.dto.LinkResponse;
import ru.effective_mobile.shortlinks.exception.LinkNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface LinkService {

    /**
     * Создает новую сокращенную ссылку.
     *
     * @param request запрос {@link LinkRequest}, содержащий данные для создания новой ссылки.
     * @return alias или сгенерированный номер.
     */
    String createShortLink(@Valid LinkRequest request);

    /**
     * Получает сокращенную ссылку по ее идентификатору.
     *
     * @param id идентификатор ссылки.
     * @return {@link LinkResponse}, содержащий информацию о ссылке.
     * @throws LinkNotFoundException если ссылка с данным id не найдена.
     */
    LinkResponse getLinkById(@Positive Long id) throws LinkNotFoundException;

    /**
     * Получает сокращенную ссылку по ее alias.
     *
     * @param alias alias ссылки.
     * @return {@link LinkResponse}, содержащий информацию о ссылке.
     * @throws LinkNotFoundException если ссылка с данным alias не найдена.
     */
    LinkResponse getLinkByAlias(@Pattern(regexp = "^[a-zA-Z0-9]{1,10}$") String alias) throws LinkNotFoundException;

    /**
     * Получает список всех сокращенных ссылок.
     *
     * @return список {@link LinkResponse}, содержащих информацию обо всех ссылках.
     */
    List<LinkResponse> getAllLinks();

    /**
     * Получает список всех сокращенных ссылок по неполному alias.
     *
     * @return список {@link LinkResponse}, содержащих информацию обо всех полученных ссылках.
     */
    List<LinkResponse> getAllLinksByAlias(@Pattern(regexp = "^[a-zA-Z0-9]{1,10}$") String alias);

    /**
     * Получает список всех сокращенных ссылок по ttl.
     *
     * @return список {@link LinkResponse}, содержащих информацию обо всех полученных ссылках.
     */
    List<LinkResponse> getAllLinksByTtl(LocalDate ttl);

    /**
     * Получает полную ссылку по ее alias.
     *
     * @param alias alias ссылки.
     * @return полная ссылка.
     * @throws LinkNotFoundException если ссылка с данным alias не найдена.
     */
    String getOriginalLink(@Pattern(regexp = "^[a-zA-Z0-9]{1,10}$") String alias) throws LinkNotFoundException;

    /**
     * Изменяет alias ссылки по ее идентификатору.
     *
     * @param id    идентификатор ссылки.
     * @param alias новый alias ссылки.
     * @throws LinkNotFoundException если ссылка с данным id не найдена.
     */
    void updateAliasByLinkId(@Positive Long id,
                             @Pattern(regexp = "^[a-zA-Z0-9]{1,10}$") String alias) throws LinkNotFoundException;

    /**
     * Изменяет ttl ссылки по ее идентификатору.
     *
     * @param id  идентификатор ссылки.
     * @param ttl новый ttl ссылки.
     * @throws LinkNotFoundException если ссылка с данным id не найдена.
     */
    void updateTtlByLinkId(@Positive Long id,
                           @FutureOrPresent LocalDate ttl) throws LinkNotFoundException;

    /**
     * Удаляет ссылку по ее идентификатору.
     *
     * @param id идентификатор ссылки.
     * @throws LinkNotFoundException если ссылка с данным id не найдена.
     */
    void deleteLinkById(@Positive Long id) throws LinkNotFoundException;

    /**
     * Удаляет ссылку по ее идентификатору.
     *
     * @param alias alias ссылки.
     * @throws LinkNotFoundException если ссылка с данным id не найдена.
     */
    void deleteLinkByAlias(@Pattern(regexp = "^[a-zA-Z0-9]{1,10}$") String alias) throws LinkNotFoundException;

    /**
     * Удаляет все ссылки.
     */
    void deleteAllLinks();

    /**
     * Удаляет все ссылки с истекшим ttl.
     */
    void deleteAllLinksByTtl();
}