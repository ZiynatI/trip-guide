package org.example1;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.util.*;

public class Bot extends TelegramLongPollingBot {
    public static void main(String[] args) throws TelegramApiException {
        Config cfg = ConfigFactory.parseFile(new File("application.conf"));
        String chatId = cfg.getString("telegram.chatId");
        String botToken = cfg.getString("telegram.bot.token");
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        Bot bot = new Bot(botToken, "ZisTripGuideBot");
        bot.setButtonsLabels(cities());
        botsApi.registerBot(bot);

    }

    private final String botToken;
    private final String botUserName;
    private String arrival = "not_selected";
    private String departure = "not_selected";
    private String date = "not_selected";
    private List<String> buttonsLabels;

    public Bot(String botToken, String botUserName) {
        this.botToken = botToken;
        this.botUserName = botUserName;
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
            try {
                handleCallBackQuery(update.getCallbackQuery());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else if (update.hasMessage()) {
            try {
                handleMessage(update.getMessage());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
        var msg = update.getMessage();
        var user = msg.getFrom();

        System.out.println(user.getFirstName() + " wrote " + msg.getText());
    }

    public void handleMessage(Message message) throws TelegramApiException {
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity = message.getEntities().stream().filter(e -> ("bot_command".equals(e.getType()))).findFirst();
            if (commandEntity.isPresent()) {
                String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                switch (command) {
                    case "/choose_station":
                        execute(
                                SendMessage.builder()
                                        .text("Please, choose departure and arrival stations")
                                        .chatId(message.getChatId().toString())
                                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(listToButtons()).build())
                                        .build());
                }
            }
        } else if (message.hasText()) {
            if (message.getText().matches("^([0-2][0-9]|(3)[0-1])(\\.)(((0)[0-9])|((1)[0-2]))(\\.)\\d{4}$")) {
                date = message.getText();
            }
        }
    }

    private String getStationButton(boolean isDeparture, String station) {
        if (isDeparture) {
            return departure.equals(station) ? station + "✔️" : station;
        } else {
            return arrival.equals(station) ? station + "✔️" : station;
        }
    }

    public void handleCallBackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Message message = callbackQuery.getMessage();
        String[] param = callbackQuery.getData().split(":");
        String action = param[0];
        String station = param[1];
        switch (action) {
            case "departure":
                departure = station;
                break;
            case "arrival":
                arrival = station;
                break;
        }
        execute(EditMessageReplyMarkup.builder().chatId(message.getChatId().toString()).messageId(message.getMessageId()).replyMarkup(InlineKeyboardMarkup.builder().keyboard(listToButtons()).build()).build());
    }

    public void sendText(String chatId, String text) {
        SendMessage sm = SendMessage.builder()
                .chatId(chatId)
                .text(text).build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private List<List<InlineKeyboardButton>> listToButtons() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (String buttonLabel : buttonsLabels) {
            buttons.add(
                    Arrays.asList(
                            InlineKeyboardButton.builder()
                                    .text(getStationButton(true, buttonLabel))
                                    .callbackData("departure:" + buttonLabel)
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text(getStationButton(false, buttonLabel))
                                    .callbackData("arrival:" + buttonLabel)
                                    .build()));
        }
        return buttons;
    }

    public void setButtonsLabels(List buttonsLabels) {
        this.buttonsLabels = buttonsLabels;
    }

    private static List<String> cities() {
        List<String> list = new LinkedList<>();
        list.add("TASHKENT");
        list.add("SAMARKAND");
        list.add("BUKHARA");
        list.add("KHIVA");
        list.add("URGENCH");
        list.add("NUKUS");
        list.add("NAVOI");
        list.add("ANDIJAN");
        list.add("KARSHI");
        list.add("DJIZAKH");
        list.add("TERMEZ");
        list.add("GULISTAN");
        list.add("KOKAND");
        list.add("MARGILAN");
        list.add("PAP");
        list.add("NAMANGAN");
        list.add("SHYMKENT");
        list.add("ALMATY");
        return list;
    }

    public String getArrival() {
        return arrival;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDate() {
        return date;
    }
}