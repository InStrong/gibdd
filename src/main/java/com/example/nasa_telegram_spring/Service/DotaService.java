package com.example.nasa_telegram_spring.Service;

import com.example.nasa_telegram_spring.Model.Entity.Dota.Ability;
import com.example.nasa_telegram_spring.Model.Entity.Dota.Question;
import com.example.nasa_telegram_spring.Repository.DotaAbilityRepository;
import com.example.nasa_telegram_spring.Utils.JsonReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class DotaService {

    private final DotaAbilityRepository dotaAbilityRepository;
    private final String OPEN_DOTA_URL = "https://api.opendota.com";
    private final String API_KEY = "?api_key=2e96e00b-14f2-4480-9874-27b2259cf6ff";



    public Question getQuestion() {
        Question question = new Question();
        Map<String, Boolean> answers = new HashMap<>();
        Ability ability = dotaAbilityRepository.getRandomAbilty();
        answers.put(ability.getDname(), true);
        List<String> wrongAnswers = dotaAbilityRepository.get3RandomAnswers(ability.getDname());
        answers.put(wrongAnswers.get(0), false);
        answers.put(wrongAnswers.get(1), false);
        answers.put(wrongAnswers.get(2), false);
        question.setImage(ability.getImg());
        question.setAnswers(answers);
        return question;
    }

    @SneakyThrows
    public void fillingDatabase() {
        JSONObject abilities = JsonReader.readJsonFromUrl("https://api.opendota.com/api/constants/abilities");
        List<JSONObject> filtered = new ArrayList<>();
        Iterator<String> keys = abilities.keys();
        List<Ability> allAbilities = new ArrayList<>();
        while(keys.hasNext()) {
            String key = keys.next();
            if (abilities.getJSONObject(key).has("dname") && abilities.getJSONObject(key).has("img") && abilities.getJSONObject(key).has("desc")) {
                filtered.add(abilities.getJSONObject(key));
            }
        }
        for (JSONObject array : filtered) {
            allAbilities.add(new Ability(array.getString("dname"), array.getString("img")));
        }
        dotaAbilityRepository.saveAll(allAbilities);
    }

    @SneakyThrows
    public void deleteNotWorkingFiles() {
        List<Ability> allAbilities = dotaAbilityRepository.findAllReverse();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(2000).build();
        HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        for (Ability ability : allAbilities) {
            HttpResponse response = client.execute(new HttpGet(OPEN_DOTA_URL + ability.img + API_KEY));
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.OK.value()) {
                dotaAbilityRepository.delete(ability);
            }
            else {
                log.info("passed");
            }
            response.getEntity().getContent().close();
        }
        log.info("finished");
    }
}
