package com.example.nasa_telegram_spring.Service;

import com.example.nasa_telegram_spring.Model.Entity.Nasa.ImageOfADay;
import com.example.nasa_telegram_spring.Repository.ImageOfADayRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageOfADayService {

    private final ImageOfADayRepository imageOfADayRepository;
    private final String NASA_TOKEN = "TfVQEzsdBvQX5M9O75ozXFavFkdimQ24i4RJISY0";
    private final String IMAGE_OF_A_DAY_URL = "https://api.nasa.gov/planetary/apod?api_key=";

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void saveImageOfADay() {
        RestTemplate restTemplate = new RestTemplate();
        ImageOfADay image = restTemplate.getForObject(IMAGE_OF_A_DAY_URL + NASA_TOKEN, ImageOfADay.class);
        imageOfADayRepository.save(image);
    }

    public ImageOfADay getImageOfADay() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formatDateTime = now.format(formatter);
        ImageOfADay image = imageOfADayRepository.findByDate(formatDateTime);
        if (image == null) {
            image = imageOfADayRepository.getFirstByDateOrderByDate();
        }
        return image;
    }

    @SneakyThrows
    @Scheduled(fixedRate = 1000 * 60 * 5)
    private void pingSite() {
        boolean reachable = InetAddress.getByName("java.sun.com").isReachable(100);
        log.info("google ping :" + reachable);
    }
}
