package ru.effective_mobile.shortlinks.exception;

public class LinkNotFoundException extends RuntimeException {

    public LinkNotFoundException() {
        super("Link not found");
    }

    public LinkNotFoundException(String message) {
        super(message);
    }
}