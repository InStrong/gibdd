package com.example.nasa_telegram_spring.Service;

import com.example.nasa_telegram_spring.Model.Entity.Gibdd.Fine;
import com.example.nasa_telegram_spring.Model.Entity.Gibdd.Gibdd;
import com.example.nasa_telegram_spring.Model.Entity.Gibdd.Photo;
import com.example.nasa_telegram_spring.Model.Entity.Gibdd.PhotoResponse;
import com.example.nasa_telegram_spring.Repository.FineRepository;
import com.example.nasa_telegram_spring.Repository.PhotoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;

import org.apache.http.util.EntityUtils;
import org.hibernate.engine.jdbc.ClobProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GibddService {

    private final FineRepository fineRepository;
    private final PhotoRepository photoRepository;
    @Value("${regnum}")
    String regnum;
    @Value("${regreg}")
    String regreg;
    @Value("${stsnum}")
    String stsnum;
    private final String FINES_URL = "https://xn--b1afk4ade.xn--90adear.xn--p1ai/proxy/check/fines?";
    private final String PHOTOS_URL = "https://xn--b1afk4ade.xn--90adear.xn--p1ai/proxy/check/fines/pics?";

    @SneakyThrows
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
    public void parseFines() {
        log.info("started");
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(generateUrl());
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String retSrc = EntityUtils.toString(entity);
            ObjectMapper mapper = new ObjectMapper();
            List<Fine> parsedFines = mapper.readValue(retSrc, Gibdd.class).getFines();
            log.info(parsedFines.toString());
            fineRepository.deleteAll();
            fineRepository.saveAll(parsedFines);
            parsePhotos(parsedFines);
        }
    }

    @SneakyThrows
    public void parsePhotos(List<Fine> fines) {
        photoRepository.deleteAll();
        HttpClient httpclient = HttpClients.createDefault();
        List<Photo> parsedPhotos = new ArrayList<>();
        for (Fine fine : fines) {
            HttpPost httppost = new HttpPost(generatePhotoUrl(fine));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String retSrc = EntityUtils.toString(entity);
                ObjectMapper mapper = new ObjectMapper();
                parsedPhotos = mapper.readValue(retSrc, PhotoResponse.class).getPhotos();
                parsedPhotos.forEach(p -> {
                    p.setNumPost(fine.getNumPost());
                    p.setImageFile(ArrayUtils.toObject(Base64.getDecoder().decode(p.getBase64Value())));
                    p.setBase64Value("test");
                });
            }
            photoRepository.saveAll(parsedPhotos);
        }
    }

    private String generateUrl() {
        return FINES_URL + "regnum=" + regnum + "&regreg=" + regreg + "&stsnum=" + stsnum;
    }

    private String generatePhotoUrl(Fine fine) {
        return PHOTOS_URL + "post=" + fine.getNumPost() + "&regnum=" + regnum + "76";
    }

    public List<Fine> getFines() {
        List<Fine> fines = fineRepository.findAll();
        if (fines.size() == 0) {
            return Collections.emptyList();
        }
        else {
            return fines;
        }
    }

    public Fine getFineByNumPost(String numPost) {
        return fineRepository.findByNumPost(numPost);
    }

    public List<Photo> getPhotosByNumPost(String numPost) {
        List<Photo> photos = photoRepository.findAllByNumPost(numPost);
        return photos;
    }

}
