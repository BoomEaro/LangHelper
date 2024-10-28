package ru.boomearo.langhelper.versions.cached;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.json.simple.JSONObject;
import ru.boomearo.langhelper.utils.JsonUtils;
import ru.boomearo.langhelper.versions.exceptions.LangParseException;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@ToString
public class CachedVersionData {

    private final String version;
    private final URL url;

    private Map<String, String> languagesHash = null;

    private void loadResources() throws LangParseException {
        if (this.languagesHash != null) {
            return;
        }

        Map<String, String> tmp = new HashMap<>();

        // Получив успешно объект ресурсов этой версии, ищем далее в ней требуемый язык
        JSONObject versionJson;
        try {
            versionJson = JsonUtils.connectNormal(this.url);
        } catch (IOException e) {
            throw new LangParseException("Failed to connect to get resources", e);
        }

        if (versionJson == null) {
            throw new LangParseException("Failed to parse json object on url '" + this.url + "'");
        }

        JSONObject jsonAssetIndex = JsonUtils.getJsonObject(versionJson.get("assetIndex"));
        if (jsonAssetIndex == null) {
            throw new LangParseException("Failed to parse json object assetIndex");
        }

        String langUrl = JsonUtils.getStringObject(jsonAssetIndex.get("url"));
        if (langUrl == null) {
            throw new LangParseException("Failed to find url string in assetIndex");
        }

        JSONObject jsonLang;
        try {
            jsonLang = JsonUtils.connectNormal(new URL(langUrl));
        } catch (IOException e) {
            throw new LangParseException("Failed to connect to get object", e);
        }

        if (jsonLang == null) {
            throw new LangParseException("Failed to parse json object on url '" + langUrl + "'");
        }

        JSONObject langObjects = JsonUtils.getJsonObject(jsonLang.get("objects"));
        if (langObjects == null) {
            throw new LangParseException("Failed to find objects object");
        }

        String languagePattern = "minecraft/lang/";
        for (Object o : langObjects.entrySet()) {
            Map.Entry<String, JSONObject> entry = (Map.Entry<String, JSONObject>) o;

            String lang = entry.getKey();

            // Убеждаемся что из всех ресурсов нам попадается именно языки
            if (lang.startsWith(languagePattern)) {
                // Вырезаем из имени этого ресурса ненужное. А именно расширение и путь
                String langName = lang.substring(languagePattern.length()).replace(".json", "").replace(".lang", "");

                // Получаем у этого ресурса хэш, а затем добавляем в кэш
                String hash = JsonUtils.getStringObject(entry.getValue().get("hash"));
                if (hash == null) {
                    continue;
                }

                tmp.put(langName, hash);
            }
        }

        this.languagesHash = Collections.unmodifiableMap(tmp);
    }

    public String getCachedLanguageHash(String language) throws LangParseException {
        loadResources();

        return this.languagesHash.get(language);
    }

}
