package ru.effective_mobile.shortlinks.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.effective_mobile.shortlinks.dto.LinkRequest;
import ru.effective_mobile.shortlinks.dto.LinkResponse;

import java.time.LocalDate;
import java.util.List;

import static ru.effective_mobile.shortlinks.controller.swagger.Constants.*;

@Tag(name = "Link", description = "Контроллер для управления ссылками")
public interface LinkControllerDoc {

    @Operation(
            summary = "Создание короткой ссылки",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_201,
                            description = MESSAGE_201,
                            content = @Content(
                                    mediaType = MediaType.ALL_VALUE,
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject("ga")
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = MESSAGE_400,
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = CODE_500,
                            description = MESSAGE_500,
                            content = @Content()
                    )
            }
    )
    ResponseEntity<String> createShortLink(LinkRequest request);

    @Operation(
            summary = "Получение информации о ссылке по ID",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = MESSAGE_200,
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = LinkResponse.class)
                            )
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
    LinkResponse getLinkById(@Parameter(example = "1") Long id);

    @Operation(
            summary = "Получение информации о ссылке по alias",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = MESSAGE_200,
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = LinkResponse.class)
                            )
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
    LinkResponse getLinkByAlias(@Parameter(example = "ga") String alias);

    @Operation(
            summary = "Получение информации обо всех ссылках",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = MESSAGE_200,
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = LinkResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_500,
                            description = MESSAGE_500,
                            content = @Content()
                    )
            }
    )
    List<LinkResponse> getAllLinks();

    @Operation(
            summary = "Получение информации обо всех ссылках по неполному alias",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = MESSAGE_200,
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = LinkResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_400,
                            description = MESSAGE_400,
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = CODE_500,
                            description = MESSAGE_500,
                            content = @Content()
                    )
            }
    )
    List<LinkResponse> getAllLinksByAlias(@Parameter(example = "ga") String alias);

    @Operation(
            summary = "Получение информации обо всех ссылках по ttl",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = MESSAGE_200,
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = LinkResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = CODE_500,
                            description = MESSAGE_500,
                            content = @Content()
                    )
            }
    )
    List<LinkResponse> getAllLinksByTtl(@Parameter(example = "2024-12-31") LocalDate ttl);

    @Operation(
            summary = "Изменение alias ссылки по ID",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = MESSAGE_200,
                            content = @Content()
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
    void updateAliasByLinkId(@Parameter(example = "1") Long id,
                             @Parameter(example = "ga") String alias);

    @Operation(
            summary = "Изменение ttl ссылки по ID",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = MESSAGE_200,
                            content = @Content()
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
    void updateTtlByLinkId(@Parameter(example = "1") Long id,
                           @Parameter(example = "2024-12-31") LocalDate ttl);

    @Operation(
            summary = "Удаление ссылки по ID",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = MESSAGE_200,
                            content = @Content()
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
    void deleteLinkById(@Parameter(example = "1") Long id);

    @Operation(
            summary = "Удаление ссылки по alias",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = MESSAGE_200,
                            content = @Content()
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
    void deleteLinkByAlias(@Parameter(example = "ga") String alias);

    @Operation(
            summary = "Удаление всех ссылок",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = MESSAGE_200,
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = CODE_500,
                            description = MESSAGE_500,
                            content = @Content()
                    )
            }
    )
    void deleteAllLinks();

    @Operation(
            summary = "Удаление всех ссылок с истекшим ttl",
            responses = {
                    @ApiResponse(
                            responseCode = CODE_200,
                            description = MESSAGE_200,
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = CODE_500,
                            description = MESSAGE_500,
                            content = @Content()
                    )
            }
    )
    void deleteAllLinksByTtl();
}