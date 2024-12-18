package ru.effective_mobile.shortlinks.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.effective_mobile.shortlinks.controller.swagger.LinkControllerDoc;
import ru.effective_mobile.shortlinks.dto.LinkRequest;
import ru.effective_mobile.shortlinks.dto.LinkResponse;
import ru.effective_mobile.shortlinks.service.LinkService;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/link")
@RequiredArgsConstructor
public class LinkController implements LinkControllerDoc {

    private final LinkService linkService;

    @PostMapping("/create")
    @Override
    public ResponseEntity<String> createShortLink(@RequestBody LinkRequest request) {
        return ResponseEntity
                .status(CREATED)
                .body(linkService.createShortLink(request));
    }

    @GetMapping("/get/{id}")
    @Override
    public LinkResponse getLinkById(@PathVariable Long id) {
        return linkService.getLinkById(id);
    }

    @GetMapping("/get/alias")
    @Override
    public LinkResponse getLinkByAlias(@RequestParam String alias) {
        return linkService.getLinkByAlias(alias);
    }

    @GetMapping("/get/all")
    @Override
    public List<LinkResponse> getAllLinks() {
        return linkService.getAllLinks();
    }

    @GetMapping("/get/all/alias")
    @Override
    public List<LinkResponse> getAllLinksByAlias(@RequestParam String alias) {
        return linkService.getAllLinksByAlias(alias);
    }

    @GetMapping("/get/all/ttl")
    @Override
    public List<LinkResponse> getAllLinksByTtl(@RequestParam LocalDate ttl) {
        return linkService.getAllLinksByTtl(ttl);
    }

    @PatchMapping("/update/{id}/alias")
    @Override
    public void updateAliasByLinkId(@PathVariable Long id,
                                    @RequestParam String alias) {
        linkService.updateAliasByLinkId(id, alias);
    }

    @PatchMapping("/update/{id}/ttl")
    @Override
    public void updateTtlByLinkId(@PathVariable Long id,
                                  @RequestParam LocalDate ttl) {
        linkService.updateTtlByLinkId(id, ttl);
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public void deleteLinkById(@PathVariable Long id) {
        linkService.deleteLinkById(id);
    }

    @DeleteMapping("/delete/alias")
    @Override
    public void deleteLinkByAlias(@RequestParam String alias) {
        linkService.deleteLinkByAlias(alias);
    }

    @DeleteMapping("/delete/all")
    @Override
    public void deleteAllLinks() {
        linkService.deleteAllLinks();
    }

    @DeleteMapping("/delete/all/ttl")
    @Override
    public void deleteAllLinksByTtl() {
        linkService.deleteAllLinksByTtl();
    }
}