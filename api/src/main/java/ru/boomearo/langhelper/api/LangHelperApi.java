package ru.boomearo.langhelper.api;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LangHelperApi {

    private static TranslateManager translateManager;

    public static void set(@NonNull TranslateManager translateManager) {
        LangHelperApi.translateManager = translateManager;
    }

    public static void unset() {
        LangHelperApi.translateManager = null;
    }

    @NonNull
    public static TranslateManager getTranslateManager() {
        Preconditions.checkNotNull(translateManager, "API is not initialized");
        return translateManager;
    }

}
