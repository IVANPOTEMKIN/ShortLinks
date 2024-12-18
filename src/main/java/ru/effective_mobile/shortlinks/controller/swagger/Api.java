package ru.effective_mobile.shortlinks.controller.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Short Link System Api",
                description = "SHORT LINK", version = "1.0.0",
                contact = @Contact(
                        name = "POTEMKIN IVAN",
                        url = "https://github.com/IVANPOTEMKIN"
                )
        )
)
public class Api {
}