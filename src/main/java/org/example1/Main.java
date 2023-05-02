package org.example1;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.example.DataGetter;
import org.example.Train;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws TelegramApiException, IOException {
        findTickets();
    }

    public static void findTickets() throws TelegramApiException, IOException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        Config cfg = ConfigFactory.parseFile(new File("application.conf"));
        Bot bot = new Bot(cfg.getString("telegram.bot.token"), cfg.getString("telegram.bot.username"));
        botsApi.registerBot(bot);
        bot.sendText(cfg.getString("telegram.chatId"), "Hello. Do you want to find ticket?");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> stationButtons = new ArrayList<>();
        Map<String, Integer> stationCode = getStationsCode();
        for (Map.Entry<String, Integer> station : stationCode.entrySet()) {
            stationButtons.add(
                    Arrays.asList(
                            InlineKeyboardButton.builder().text(station.getKey()).callbackData("departure:" + station.getKey()).build(),
                            InlineKeyboardButton.builder().text(station.getKey()).callbackData("arrival:" + station.getKey()).build()));

        }
        bot.setStationButtons(stationButtons);
        bot.sendText(cfg.getString("telegram.bot.token"), "please,choose arrival date");
        while (true) {
            if (!bot.getDate().equals("not_selected") && !bot.getDeparture().equals("not_selected") && !bot.getArrival().equals("not_selected")) {
                DataGetter dataGetter = new DataGetter(bot.getDate(), stationCode.get(bot.getDeparture()),
                        stationCode.get(bot.getArrival()));
                String message = trainListToMessage(dataGetter.jsonToTrainsList(dataGetter.makeRequestGetResponse()));
                bot.sendText(cfg.getString("telegram.chatId"), "Tickets from Tashkent to Samarkand for " + bot.getDate() + message);
                break;
            }
        }
    }


    private static String repeatUntilChosenRight
            (Bot bot, String chatId, String request, InlineKeyboardMarkup markupInLine) {
        String s = "";
        boolean isValidRequest = false;
        bot.sendText(request, chatId);
        while (!isValidRequest) {
            s = bot.getFirstMessage();
//            isValidRequest = validate.apply(s);
        }
        bot.clearMessages();
        return s;
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

    private static Map<String, Integer> getStationsCode() {
        Map<String, Integer> stationsCode = new HashMap<>();
        stationsCode.put("TASHKENT", 2900000);
        stationsCode.put("SAMARKAND", 2900700);
        stationsCode.put("BUKHARA", 2900800);
        stationsCode.put("URGENCH", 2900790);
        stationsCode.put("NUKUS", 2900970);
        stationsCode.put("NAVOI", 2900930);
        stationsCode.put("ANDIJAN", 2900680);
        stationsCode.put("KARSHI", 2900750);
        stationsCode.put("DJIZAKH", 2900720);
        stationsCode.put("TERMEZ", 2900255);
        stationsCode.put("GULISTAN", 2900850);
        stationsCode.put("KOKAND", 2900880);
        stationsCode.put("MARGILAN", 2900920);
        stationsCode.put("PAP", 2900693);
        stationsCode.put("NAMANGAN", 2900940);
        stationsCode.put("SHYMKENT", 2700770);
        stationsCode.put("ALMATY", 2900000);
        return stationsCode;
    }

    private static String trainListToMessage(List<Train> trains) {
        StringBuilder sb = new StringBuilder();
        for (Train train : trains) {
            if (train.countFreeSeats() != 0) {
                sb.append(train);
            }
        }
        return sb.toString();
    }
}
