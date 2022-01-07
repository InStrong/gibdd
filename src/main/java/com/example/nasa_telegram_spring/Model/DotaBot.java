package com.example.nasa_telegram_spring.Model;

import com.example.nasa_telegram_spring.Model.Entity.Dota.CurrentQuestion;
import com.example.nasa_telegram_spring.Model.Entity.Dota.Question;
import com.example.nasa_telegram_spring.Repository.CurrentQuestionRepository;
import com.example.nasa_telegram_spring.Service.DotaService;
import com.example.nasa_telegram_spring.Service.ImageOfADayService;
import com.example.nasa_telegram_spring.Service.YandexTranslateService;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DotaBot extends TelegramLongPollingBot {

    private final DotaService dotaService;
    private final CurrentQuestionRepository currentQuestionRepository;
    private final String OPEN_DOTA_URL = "https://api.opendota.com";
    private final static String CORRECT = ":white_check_mark:";
    private final static String WRONG = ":no_entry:";

    @Override
    public String getBotUsername() {
        return "Dota quiz";
    }

    @Override
    public String getBotToken() {
        return "5091575826:AAFl595WrZRPfnlNHaYix7jhEJtEPA7z6vQ";
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            CurrentQuestion currentQuestion = currentQuestionRepository.findCorrectByChatId(update.getCallbackQuery().getFrom().getId().toString());
            if (currentQuestion.getCorrectAnswer().equals(update.getCallbackQuery().getData())) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId((update.getCallbackQuery().getFrom().getId().toString()));
                sendMessage.setText(EmojiParser.parseToUnicode(CORRECT));
                execute(sendMessage);
                sendQuestion(update);
            }
            else {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId((update.getCallbackQuery().getFrom().getId().toString()));
                sendMessage.setText(EmojiParser.parseToUnicode(WRONG));
                execute(sendMessage);
                sendQuestion(update);
            }
        }
        else if (update.getMessage().getText().equals("/start")){
            sendQuestion(update);
        }

    }

    @SneakyThrows
    private void sendQuestion(Update update) {
        Question question = getQuestionForUser(update);
        SendPhoto photo = new SendPhoto();
        InputFile file = new InputFile();
        file.setMedia(OPEN_DOTA_URL + question.getImage());
        photo.setPhoto(file);
        if (!update.hasCallbackQuery()) {
            photo.setChatId(update.getMessage().getChatId().toString());
        }
        else {
            photo.setChatId(update.getCallbackQuery().getFrom().getId().toString());
        }
        execute(photo);
        sendInlineKeyboard(update, question);
    }
    private Question getQuestionForUser(Update update) {
        Question question = dotaService.getQuestion();
        String correctAnswer = "";
        for (Map.Entry<String, Boolean> entry : question.getAnswers().entrySet()) {
            if (entry.getValue().equals(true)) {
                correctAnswer = entry.getKey();
            }
        }
        CurrentQuestion currentQuestion;
        if (!update.hasCallbackQuery()) {
            currentQuestion = currentQuestionRepository.findCorrectByChatId(update.getMessage().getChatId().toString());
        }
        else {
            currentQuestion = currentQuestionRepository.findCorrectByChatId(update.getCallbackQuery().getFrom().getId().toString());
        }
        if (currentQuestion == null) {
            currentQuestionRepository.save(new CurrentQuestion(update.getMessage().getChatId().toString(), correctAnswer));
        }
        else {
            currentQuestion.setCorrectAnswer(correctAnswer);
            currentQuestionRepository.save(currentQuestion);
        }
        return question;
    }

    @SneakyThrows
    private void sendInlineKeyboard(Update update, Question question) {
        SendMessage message = new SendMessage();
        if (!update.hasCallbackQuery()) {
            message.setChatId(update.getMessage().getChatId().toString());
        }
        else {
            message.setChatId(update.getCallbackQuery().getFrom().getId().toString());
        }
        message.setText("What's the name of this ability?");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        InlineKeyboardButton answer1 = new InlineKeyboardButton();
        InlineKeyboardButton answer2 = new InlineKeyboardButton();
        InlineKeyboardButton answer3 = new InlineKeyboardButton();
        InlineKeyboardButton answer4 = new InlineKeyboardButton();

        List<String> answers = new ArrayList<>(question.getAnswers().keySet());
        answer1.setText(answers.get(0));
        answer2.setText(answers.get(1));
        answer3.setText(answers.get(2));
        answer4.setText(answers.get(3));
        answer1.setCallbackData(answers.get(0));
        answer2.setCallbackData(answers.get(1));
        answer3.setCallbackData(answers.get(2));
        answer4.setCallbackData(answers.get(3));

        rowInline1.add(answer1);
        rowInline1.add(answer2);
        rowInline2.add(answer3);
        rowInline2.add(answer4);
        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        execute(message);
    }
}
