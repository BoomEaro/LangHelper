package ru.boomearo.langhelper.versions;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.boomearo.langhelper.api.LangType;
import ru.boomearo.langhelper.api.TranslatedMessages;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class TranslatedMessagesImpl implements TranslatedMessages {

    @Getter
    private final LangType langType;
    private final Map<String, String> translate;

    @Nullable
    @Override
    public String getTranslate(String name) {
        return this.translate.get(name);
    }

    @NonNull
    @Override
    public Collection<String> getAllTranslate() {
        return this.translate.values();
    }

}
