package ru.boomearo.langhelper.versions.exceptions;
public class LangParseException extends LangException {

    public LangParseException(String msg) {
        super(msg);
    }

    public LangParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
