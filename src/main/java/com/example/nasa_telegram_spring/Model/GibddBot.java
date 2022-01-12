package com.example.nasa_telegram_spring.Model;

import com.example.nasa_telegram_spring.Model.Entity.Gibdd.Fine;
import com.example.nasa_telegram_spring.Model.Entity.Gibdd.Photo;
import com.example.nasa_telegram_spring.Service.GibddService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GibddBot extends TelegramLongPollingBot {

    private final GibddService gibddService;

    @Value("${gibdd.dota.token}")
    String token;

    @Override
    public String getBotUsername() {
        return "Гибдд штрафы";
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            StringBuilder sb = new StringBuilder();
            CallbackQuery callback = update.getCallbackQuery();
            Fine fine = gibddService.getFineByNumPost(callback.getData());
            sb.append("Дата нарушения: ").append(fine.getDateDecis()).append("\n");
            sb.append(fine.getKoAPcode()).append(" ").append(fine.getKoAPtext()).append("\n");
            sb.append("Сумма штрафа: ").append(fine.getSumma()).append("\n");
            sendMessage(callback.getFrom().getId().toString(), sb.toString());
            log.info("e");
            List<Photo> photos = gibddService.getPhotosByNumPost(callback.getData());
            sendPhotos(callback.getFrom().getId().toString(), photos);
        }
        else if (update.getMessage().getText().equals("/getFines")) {
            List<Fine> fines = gibddService.getFines();
            if (fines.size() == 0) {
                sendMessage(update.getMessage().getChatId().toString(), "Штрафов нет");
            }
            else {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Список штрафов:");
                sendMessage.setChatId(update.getMessage().getChatId().toString());
                sendMessage.setReplyMarkup(sendInlineKeyboard(fines));
                execute(sendMessage);
            }
        }
    }

    @SneakyThrows
    private void sendPhotos(String chatId, List<Photo> photos) {
        for (Photo photo : photos) {
            SendPhoto sendPhoto = new SendPhoto();
            File file = new File("image");
            FileUtils.writeByteArrayToFile(file, ArrayUtils.toPrimitive(photo.imageFile));
            sendPhoto.setPhoto(new InputFile(file));
            sendPhoto.setChatId(chatId);
            execute(sendPhoto);
        }
    }

    @SneakyThrows
    private void sendMessage(String chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);
        execute(sendMessage);
    }

        private InlineKeyboardMarkup sendInlineKeyboard(List<Fine> fines) {
            int count = fines.size();

            List<List<InlineKeyboardButton>> buttons = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                int currentIndex = i;
                buttons.add(new ArrayList<InlineKeyboardButton>(1) {{
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(fines.get(currentIndex).getDateDecis() + " >>> " + fines.get(currentIndex).getSumma() + "p.");
                    button.setCallbackData(fines.get(currentIndex).getNumPost());
                    add(button);
                }});
            }
            return new InlineKeyboardMarkup(buttons);
    }
}
