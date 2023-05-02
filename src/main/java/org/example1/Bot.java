package org.example1;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Bot extends TelegramLongPollingBot {

    private final String botToken;
    private final String botUserName;
    private LinkedList<String> messages;
    private String arrival = "not_selected";
    private String departure = "not_selected";
    private String date = "not_selected";
    private List<List<InlineKeyboardButton>> stationButtons;

    public Bot(String botToken, String botUserName) {
        this.botToken = botToken;
        this.botUserName = botUserName;
        messages = new LinkedList<>();
    }

    @Override
    public String getBotUsername() {
        return this.botUserName;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
        }
        if (update.hasMessage()) {
            try {
                handleMassage(update.getMessage());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
//        var msg = update.getMessage();
//        var user = msg.getFrom();
//        messages.add(msg.getText());
//        System.out.println(user.getFirstName() + " wrote :\"" + msg.getText() + '"');
    }

    private void handleCallback(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String[] param = callbackQuery.getData().split(":");
        String action = param[0];
        String newTemp = param[1];
        switch (action) {
            case "departure":
                departure = newTemp;
            case "arrival":
                arrival = newTemp;
        }
    }

    private void handleMassage(Message message) throws TelegramApiException {
        if (message.getText().matches("^([0-2][0-9]|(3)[0-1])(\\.)(((0)[0-9])|((1)[0-2]))(\\.)\\d{4}$")) {
            date = message.getText();
        }
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity = message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                markupInline.setKeyboard(stationButtons);
                execute(SendMessage.builder().text("Choose station to leave from").chatId(message.getChatId()).replyMarkup(InlineKeyboardMarkup.builder().keyboard(stationButtons).build()).build());

            }
        }
    }

    public void sendText(String chatId, String message) {
        SendMessage sm = SendMessage.builder()
                .chatId(chatId)
                .text(message).build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFirstMessage() {
        return messages.remove();
    }

    public void clearMessages() {
        messages.clear();
    }

    public List<List<InlineKeyboardButton>> getStationButtons() {
        return stationButtons;
    }

    public void setStationButtons(List<List<InlineKeyboardButton>> stationButtons) {
        this.stationButtons = stationButtons;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}