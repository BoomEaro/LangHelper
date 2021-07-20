package ru.boomearo.langhelper.versions.cached;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.boomearo.langhelper.utils.JsonUtils;
import ru.boomearo.langhelper.versions.exceptions.LangParseException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

//Данный класс служит неким кешем, чтобы меньше обращаться к моджангу.
public class UrlManifestManager {

    private static final String VERSION_MANIFEST_URL = "https://launchermeta.mojang.com/mc/game/version_manifest.json";

    private ConcurrentMap<String, CachedVersionData> versionManifest = null;

    private void loadManifest() throws LangParseException {
        if (this.versionManifest != null) {
            return;
        }
        ConcurrentMap<String, CachedVersionData> tmp = new ConcurrentHashMap<>();

        //Получаем обьект MANIFEST который имеет список всех существующих версий игры
        JSONObject jsonManifest = JsonUtils.connectNormal(VERSION_MANIFEST_URL);
        if (jsonManifest == null) {
            throw new LangParseException("Не удалось спарсить json обьект для MANIFEST");
        }

        //Далее ниже все что мы делаем, это убеждаемся что он существует а потом извлекаем из него ссылку на ресурс этой версии
        JSONArray versionArray = JsonUtils.getJsonArrayObject(jsonManifest.get("versions"));
        if (versionArray == null) {
            throw new LangParseException("Не удалось спарсить json array обьект для MANIFEST");
        }

        for (Object arrO : versionArray) {
            JSONObject versionJson = JsonUtils.getJsonObject(arrO);
            if (versionJson == null) {
                continue;
            }

            String versionId = JsonUtils.getStringObject(versionJson.get("id"));
            if (versionId == null) {
                continue;
            }

            String versionUrl = JsonUtils.getStringObject(versionJson.get("url"));
            if (versionUrl == null) {
                continue;
            }

            tmp.put(versionId, new CachedVersionData(versionId, versionUrl));
        }

        this.versionManifest = tmp;
    }

    public String getLanguageHash(String version, String language) throws LangParseException {
        loadManifest();

        CachedVersionData cvd = this.versionManifest.get(version);
        if (cvd == null) {
            throw new LangParseException("Не удалось найти кешированную информацию версии " + version);
        }

        String hash = cvd.getCachedLanguageHash(language);
        if (hash == null) {
            throw new LangParseException("Не удалось найти кешированный хеш в языке " + language + " версии " + version);
        }

        return hash;
    }


    @Override
    public String toString() {
        return "manifest:" + (this.versionManifest != null ? this.versionManifest.toString() : "null");
    }

}
