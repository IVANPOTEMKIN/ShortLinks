package ru.effective_mobile.shortlinks.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.effective_mobile.shortlinks.dto.LinkRequest;
import ru.effective_mobile.shortlinks.dto.LinkResponse;
import ru.effective_mobile.shortlinks.entity.Link;
import ru.effective_mobile.shortlinks.exception.LinkNotFoundException;
import ru.effective_mobile.shortlinks.repository.LinkRepository;
import ru.effective_mobile.shortlinks.service.LinkService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static ru.effective_mobile.shortlinks.mapper.LinkMapper.INSTANCE;

@Service
@Validated
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;

    @Value(value = "${base.url}")
    private String baseUrl;

    @Transactional
    @Override
    public String createShortLink(LinkRequest request) {
        var link = INSTANCE.linkFromRequest(request);
        getOrGenerateAlias(request, link);
        linkRepository.save(link);
        return link.getAlias();
    }

    @Transactional(readOnly = true)
    @Override
    public LinkResponse getLinkById(Long id) throws LinkNotFoundException {
        var link = linkRepository
                .findById(id)
                .orElseThrow(LinkNotFoundException::new);

        return INSTANCE.linkToResponse(link);
    }

    @Transactional(readOnly = true)
    @Override
    public LinkResponse getLinkByAlias(String alias) throws LinkNotFoundException {
        var link = linkRepository
                .findByAlias(alias)
                .orElseThrow(LinkNotFoundException::new);

        return INSTANCE.linkToResponse(link);
    }

    @Transactional(readOnly = true)
    @Override
    public List<LinkResponse> getAllLinks() {
        return linkRepository
                .findAll()
                .stream()
                .map(INSTANCE::linkToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<LinkResponse> getAllLinksByAlias(String alias) {
        return linkRepository
                .findAllByAliasIsContainingIgnoreCaseOrderByAlias(alias)
                .stream()
                .map(INSTANCE::linkToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<LinkResponse> getAllLinksByTtl(LocalDate ttl) {
        return linkRepository
                .findAllByTtlLessThanEqualOrderByTtl(ttl)
                .stream()
                .map(INSTANCE::linkToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public String getOriginalLink(String alias) throws LinkNotFoundException {
        return linkRepository
                .findOriginalUrl(alias)
                .orElseThrow(LinkNotFoundException::new);
    }

    @Transactional
    @Override
    public void updateAliasByLinkId(Long id, String alias) throws LinkNotFoundException {
        var shortLink = baseUrl + alias;
        var result = linkRepository.updateAlias(id, alias, shortLink);
        checkResultUpdate(result);
    }

    @Transactional
    @Override
    public void updateTtlByLinkId(Long id, LocalDate ttl) throws LinkNotFoundException {
        var result = linkRepository.updateTtl(id, ttl);
        checkResultUpdate(result);
    }

    @Transactional
    @Override
    public void deleteLinkById(Long id) throws LinkNotFoundException {
        var result = linkRepository.deleteLinkById(id);
        checkResultUpdate(result);
    }

    @Transactional
    @Override
    public void deleteLinkByAlias(String alias) throws LinkNotFoundException {
        var result = linkRepository.deleteLinkByAlias(alias);
        checkResultUpdate(result);
    }

    @Transactional
    @Override
    public void deleteAllLinks() {
        linkRepository.deleteAll();
    }

    @Transactional
    @Scheduled(cron = "${cron.cleanup}")
    @Override
    public void deleteAllLinksByTtl() {
        linkRepository.deleteAllLinksByTtlBefore(LocalDate.now());
    }

    /**
     * Возвращает или генерирует случайный alias для ссылки.
     *
     * @param request запрос {@link LinkRequest}, содержащий данные для создания ссылки.
     * @param link    объект {@link Link}, для которого устанавливается alias и сокращенная ссылка.
     */
    private void getOrGenerateAlias(LinkRequest request, Link link) {
        String alias;

        if (request.alias() == null || request.alias().isBlank()) {
            alias = generateRandomAlias();
            link.setAlias(alias);

        } else {
            alias = request.alias();
        }

        link.setShortUrl(baseUrl + alias);
    }

    /**
     * Генерирует случайный alias для ссылки, используя первые 8 символов UUID.
     *
     * @return случайно сгенерированный alias.
     */
    private String generateRandomAlias() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Проверяет результат обновления ссылки. Если результат равен 0, выбрасывает исключение {@link LinkNotFoundException}.
     * Это исключение может быть выброшено, если ссылка не была найдена.
     *
     * @param result результат операции обновления (количество измененных строк в базе данных).
     * @throws LinkNotFoundException если результат операции равен 0, то есть ссылка не была найдена.
     */
    private static void checkResultUpdate(int result) throws LinkNotFoundException {
        if (result == 0) {
            throw new LinkNotFoundException();
        }
    }
}