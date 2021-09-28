package ru.boomearo.langhelper.versions.cached;

import org.json.simple.JSONObject;
import ru.boomearo.langhelper.utils.JsonUtils;
import ru.boomearo.langhelper.versions.exceptions.LangParseException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CachedVersionData {

    private final String version;
    private final String url;

    private ConcurrentMap<String, String> languagesHash = null;

    public CachedVersionData(String version, String url) {
        this.version = version;
        this.url = url;
    }

    public String getVersion() {
        return this.version;
    }

    public String getUrl() {
        return this.url;
    }

    private void loadResources() throws LangParseException {
        if (this.languagesHash != null) {
            return;
        }

        ConcurrentMap<String, String> tmp = new ConcurrentHashMap<>();

        //Получив успешно объект ресурсов этой версии, ищем далее в ней требуемый язык
        JSONObject versionJson = JsonUtils.connectNormal(this.url);
        if (versionJson == null) {
            throw new LangParseException("Не удалось спарсить json объект по ссылке '" + this.url + "'");
        }

        JSONObject jsonAssetIndex = JsonUtils.getJsonObject(versionJson.get("assetIndex"));
        if (jsonAssetIndex == null) {
            throw new LangParseException("Не удалось спарсить json объект assetIndex");
        }

        String langUrl = JsonUtils.getStringObject(jsonAssetIndex.get("url"));
        if (langUrl == null) {
            throw new LangParseException("Не удалось найти url строку в assetIndex");
        }

        JSONObject jsonLang = JsonUtils.connectNormal(langUrl);
        if (jsonLang == null) {
            throw new LangParseException("Не удалось спарсить json объект по ссылке '" + langUrl + "'");
        }

        JSONObject langObjects = JsonUtils.getJsonObject(jsonLang.get("objects"));
        if (langObjects == null) {
            throw new LangParseException("Не удалось найти objects объект");
        }

        String languagePattern = "minecraft/lang/";

        for (Object o : langObjects.entrySet()) {
            Map.Entry<String, JSONObject> entry = (Map.Entry<String, JSONObject>) o;

            String lang = entry.getKey();

            //Убеждаемся что из всех ресурсов нам попадается именно языки
            if (lang.startsWith(languagePattern)) {
                //Вырезаем из имени этого ресурса ненужное. А именно расширение и путь
                String langName = lang.substring(languagePattern.length()).replace(".json", "").replace(".lang", "");

                //Получаем у этого ресурса хэш, а затем добавляем в кэш
                String hash = JsonUtils.getStringObject(entry.getValue().get("hash"));
                if (hash == null) {
                    continue;
                }

                tmp.put(langName, hash);
            }
        }

        this.languagesHash = tmp;
    }

    public String getCachedLanguageHash(String language) throws LangParseException {
        loadResources();

        return this.languagesHash.get(language);
    }

    @Override
    public String toString() {
        return "{V:" + this.version + " URL:" +  this.url + " data:" + (this.languagesHash != null ? this.languagesHash.toString() : "null") + "}";
    }

}
