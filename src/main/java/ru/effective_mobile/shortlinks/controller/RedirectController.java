package ru.effective_mobile.shortlinks.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.effective_mobile.shortlinks.controller.swagger.RedirectControllerDoc;
import ru.effective_mobile.shortlinks.service.LinkService;

import java.net.URI;

@RestController
@RequestMapping("/redirect")
@RequiredArgsConstructor
public class RedirectController implements RedirectControllerDoc {

    private final LinkService linkService;

//    @RequestMapping(value = "/{alias}/{id}", method = {GET, DELETE})
//    public ResponseEntity<Void> redirect(@PathVariable String alias,
//                                         @PathVariable Long id) {
//
//        var originalUrl = linkService.getOriginalLink(alias);
//        var uri = URI.create(originalUrl + id);
//
//        return ResponseEntity.status(HttpStatus.FOUND)
//                .location(uri)
//                .build();
//    }
//
//    @GetMapping(value = "/{alias}")
//    public ResponseEntity<Void> redirect(@PathVariable String alias,
//                                         @RequestParam(required = false) String paramName,
//                                         @RequestParam(required = false) String paramValue) {
//
//        var originalUrl = linkService.getOriginalLink(alias);
//        URI uri;
//
//        if ((paramName == null || paramName.isBlank())
//            && (paramValue == null || paramValue.isBlank())) {
//
//            uri = URI.create(originalUrl);
//
//        } else {
//            var query = String.format("%s?%s=%s", originalUrl, paramName, paramValue);
//            uri = URI.create(query);
//        }
//
//        return ResponseEntity.status(HttpStatus.FOUND)
//                .location(uri)
//                .build();
//    }
//
//    @PatchMapping(value = "/{alias}/{id}")
//    public ResponseEntity<Void> redirect(@PathVariable String alias,
//                                         @PathVariable Long id,
//                                         @RequestParam String paramName,
//                                         @RequestParam String paramValue) {
//
//        var originalUrl = linkService.getOriginalLink(alias);
//        var query = String.format("%s%s/%s?%s=%s", originalUrl, id, paramName, paramName, paramValue);
//        var uri = URI.create(query);
//
//        return ResponseEntity.status(HttpStatus.FOUND)
//                .location(uri)
//                .build();
//    }
//
//    @DeleteMapping(value = "/{alias}")
//    public ResponseEntity<Void> redirect(@PathVariable String alias) {
//        var originalUrl = linkService.getOriginalLink(alias);
//        var uri = URI.create(originalUrl);
//
//        return ResponseEntity.status(HttpStatus.FOUND)
//                .location(uri)
//                .build();
//    }

    @GetMapping(value = "/{alias}")
    @Override
    public ResponseEntity<Void> redirect(@PathVariable String alias) {
        var originalUrl = linkService.getOriginalLink(alias);
        var uri = URI.create(originalUrl);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(uri)
                .build();
    }
}