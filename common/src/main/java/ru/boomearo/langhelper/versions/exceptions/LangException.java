package ru.boomearo.langhelper.versions.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LangException extends Exception {

    public LangException(String msg) {
        super(msg);
    }

    public LangException(String message, Throwable cause) {
        super(message, cause);
    }
}
