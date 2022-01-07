package com.example.nasa_telegram_spring.Service;

import com.example.nasa_telegram_spring.Model.Entity.IAmToken;
import com.example.nasa_telegram_spring.Model.Entity.TranslationBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class YandexTranslateService {

    private final String yandexPassportOauthToken = "AQAAAABbflW8AATuwYidJZ2WT0HYjNSXH3ObPIM";
    private final String iAmToken_URL = "https://iam.api.cloud.yandex.net/iam/v1/tokens";
    private final String TRANSLATE_URL = "https://translate.api.cloud.yandex.net/translate/v2/translate";

    private String getIAmToken() {
        RestTemplate restTemplate = new RestTemplate();
        JSONObject iAmTokenBody = new JSONObject();
        iAmTokenBody.put("yandexPassportOauthToken", yandexPassportOauthToken);
        try {
            IAmToken token = restTemplate.postForObject(iAmToken_URL, iAmTokenBody.toString(), IAmToken.class);
            return token.iamToken;
        } catch (RestClientException e) {
            return null;
        }
    }

    public String translateFromEnToRu(String text) {
        RestTemplate restTemplate = new RestTemplate();
        JSONObject translateBody = new JSONObject();
        translateBody.put("sourceLanguageCode", "en");
        translateBody.put("targetLanguageCode", "ru");
        translateBody.put("format", "PLAIN_TEXT");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + getIAmToken());
        JSONArray texts = new JSONArray();
        texts.put(text);
        translateBody.put("texts", texts);
        translateBody.put("folderId", "b1g4llgv1nh7evfmthra");
        HttpEntity<String> request =
                new HttpEntity<String>(translateBody.toString(), headers);
        try {
            TranslationBody translationBody = restTemplate.postForEntity(TRANSLATE_URL, request, TranslationBody.class).getBody();
            return translationBody.translations.get(0).text;
        } catch (RestClientException e) {
            return null;
        }
    }
}
