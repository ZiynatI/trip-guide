package org.example1;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.example.DataGetter;
import org.example.Train;
import org.telegram.telegrambots.meta.TelegramBotsApi;
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
        Config cfg = ConfigFactory.parseFile(new File("application.conf"));
        String chatId = cfg.getString("telegram.chatId");
        String botToken = cfg.getString("telegram.bot.token");
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        Bot bot = new Bot(botToken, "ZisTripGuideBot");
        bot.setButtonsLabels(cities());
        botsApi.registerBot(bot);
        bot.sendText(chatId, "If you want to find tickets, tap on \"/choose_station\" command");
        while (true) {
            if (!bot.getDeparture().equals("not_selected") && !bot.getArrival().equals("not_selected") && !bot.getDate().equals("not_selected")) {
                break;
            }
            System.out.println("while");
        }
        bot.sendText(chatId, "Write date when you going to leave\nFormat: 'dd.mm.yyyy';\nExample: '01.01.2024'");
        DataGetter dataGetter = new DataGetter(bot.getDate(), getStationsCode(bot.getDeparture()),
                getStationsCode(bot.getArrival()));
        String message = trainListToMessage(dataGetter.jsonToTrainsList(dataGetter.makeRequestGetResponse()));
        bot.sendText(chatId, "Tickets from Tashkent to Samarkand for " + bot.getDate() + message + "\n");
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

    private static int getStationsCode(String station) {
        Map<String, Integer> stationsCode = new HashMap<>();
        stationsCode.put("TASHKENT", 2900000);
        stationsCode.put("TASHKENT NORTH", 2900001);
        stationsCode.put("TASHKENT SOUTH", 2900002);
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
        return stationsCode.get(station);
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
