package ru.effective_mobile.shortlinks.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import static ru.effective_mobile.shortlinks.controller.swagger.Constants.*;

@Tag(name = "Redirect", description = "Контроллер для перенаправления ссылок по alias")
public interface RedirectControllerDoc {

    @Operation(
            summary = "Перенаправление ссылки по alias",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = MESSAGE_200
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = MESSAGE_400,
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = CODE_404,
                            description = MESSAGE_404,
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = CODE_500,
                            description = MESSAGE_500,
                            content = @Content()
                    )
            }
    )
    ResponseEntity<Void> redirect(@Parameter(example = "ga") String alias);
}