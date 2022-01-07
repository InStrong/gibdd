package com.example.nasa_telegram_spring.Model;

import com.example.nasa_telegram_spring.Model.Entity.Nasa.ImageOfADay;
import com.example.nasa_telegram_spring.Service.ImageOfADayService;
import com.example.nasa_telegram_spring.Service.YandexTranslateService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class NasaBot extends TelegramLongPollingBot {

    private final ImageOfADayService imageOfADayService;
    private final YandexTranslateService yandexTranslateService;

    @Override
    public String getBotUsername() {
        return "Nasa";
    }

    @Override
    public String getBotToken() {
        return "5024807259:AAHKZdxvXdJOgXi2K_JlnwLz7O4Dfq2M3ts";
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage().getText().equals("/imageofaday")) {
            ImageOfADay image = imageOfADayService.getImageOfADay();
            if (image != null) {
                SendPhoto photo = new SendPhoto();
                InputFile file = new InputFile();
                file.setMedia(image.getUrl());
                photo.setPhoto(file);
                photo.setChatId(update.getMessage().getChatId().toString());
                photo.setCaption(image.getTitle() + " (" + image.getDate() + ") " + "\n\n" + image.getExplanation());
                execute(photo);
            }
            else {
                execute(SendMessage.builder().text("error").chatId(update.getMessage().getChatId().toString()).build());
            }
        }
        else if (update.getMessage().getText().equals("/imageofadayru")) {
            ImageOfADay image = imageOfADayService.getImageOfADay();
            if (image != null) {
                SendPhoto photo = new SendPhoto();
                InputFile file = new InputFile();
                file.setMedia(image.getUrl());
                photo.setPhoto(file);
                photo.setChatId(update.getMessage().getChatId().toString());
                photo.setCaption(yandexTranslateService.translateFromEnToRu(image.getTitle())
                        + " (" + image.getDate() + ") "
                        + "\n\n"
                        + yandexTranslateService.translateFromEnToRu(image.getExplanation())
                        + "\nПереведено с помощью Yandex.Translate");
                execute(photo);
            }
            else {
                execute(SendMessage.builder().text("error").chatId(update.getMessage().getChatId().toString()).build());
            }
        }
    }
}
