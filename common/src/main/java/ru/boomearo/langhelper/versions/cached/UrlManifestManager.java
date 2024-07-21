package ru.boomearo.langhelper.versions.cached;

import lombok.ToString;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.boomearo.langhelper.utils.JsonUtils;
import ru.boomearo.langhelper.versions.exceptions.LangParseException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// Данный класс служит неким кешем, чтобы меньше обращаться к моджангу.
@ToString
public class UrlManifestManager {

    private static final URL VERSION_MANIFEST_URL;

    static {
        try {
            VERSION_MANIFEST_URL = new URL("https://launchermeta.mojang.com/mc/game/version_manifest.json");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, CachedVersionData> versionManifest = null;

    private void loadManifest() throws LangParseException {
        if (this.versionManifest != null) {
            return;
        }

        Map<String, CachedVersionData> tmp = new HashMap<>();

        // Получаем объект MANIFEST который имеет список всех существующих версий игры
        JSONObject jsonManifest;
        try {
            jsonManifest = JsonUtils.connectNormal(VERSION_MANIFEST_URL);
        } catch (IOException e) {
            throw new LangParseException("Failed to connect to get MANIFEST", e);
        }

        if (jsonManifest == null) {
            throw new LangParseException("Failed to parse json object for MANIFEST");
        }

        // Далее ниже все что мы делаем, это убеждаемся что он существует, а потом извлекаем из него ссылку на ресурс этой версии
        JSONArray versionArray = JsonUtils.getJsonArrayObject(jsonManifest.get("versions"));
        if (versionArray == null) {
            throw new LangParseException("Failed to parse json array object for MANIFEST");
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

            URL url;
            try {
                url = new URL(versionUrl);
            } catch (MalformedURLException e) {
                throw new LangParseException("Failed to parse url", e);
            }

            tmp.put(versionId, new CachedVersionData(versionId, url));
        }

        this.versionManifest = Collections.unmodifiableMap(tmp);
    }

    public String getLanguageHash(String version, String language) throws LangParseException {
        loadManifest();

        CachedVersionData cvd = this.versionManifest.get(version);
        if (cvd == null) {
            throw new LangParseException("Failed to find cached data for " + version);
        }

        String hash = cvd.getCachedLanguageHash(language);
        if (hash == null) {
            throw new LangParseException("Failed to find cached hash for language " + language + " and version " + version);
        }

        return hash;
    }

}
