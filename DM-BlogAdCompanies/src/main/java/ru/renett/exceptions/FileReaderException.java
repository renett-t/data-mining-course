package ru.renett.exceptions;

public class FileReaderException extends RuntimeException {
    public FileReaderException() {
    }

    public FileReaderException(String message) {
        super(message);
    }

    public FileReaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileReaderException(Throwable cause) {
        super(cause);
    }

    public FileReaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
