package ru.boomearo.langhelper.versions;

import lombok.NonNull;

public record TranslationVersionWrapper(@NonNull String version,
                                        @NonNull String className) {

}
