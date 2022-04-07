package ru.renett.exceptions;

public class WebCrawlerException extends RuntimeException {
    public WebCrawlerException() {
    }

    public WebCrawlerException(String message) {
        super(message);
    }

    public WebCrawlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebCrawlerException(Throwable cause) {
        super(cause);
    }

    public WebCrawlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
