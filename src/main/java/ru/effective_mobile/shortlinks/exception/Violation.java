package ru.effective_mobile.shortlinks.exception;

import java.time.LocalDateTime;

public record Violation(LocalDateTime timestamp,
                        String message) {
}