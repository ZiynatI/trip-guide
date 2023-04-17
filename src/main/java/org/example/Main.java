package org.example;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) throws IOException {
        findTickets();
//        Config cfg = ConfigFactory.parseFile(new File("application.conf"));
//        Bot bot = new Bot(cfg.getString("telegram.bot.token"));
//        DataGetter dataGetter = new DataGetter();
//        bot.sendMessage("Tickets from Tashkent to Samarkand for May13:\n",
//                trainListToMessage(dataGetter.jsonToTrainsList(dataGetter.makeRequestGetResponse())),
//                cfg.getString("telegram.chatId"));
    }

    public static void findTickets() throws IOException {
        Config cfg = ConfigFactory.parseFile(new File("application.conf"));
        Bot bot = new Bot(cfg.getString("telegram.bot.token"));
        String cityFromNumInList = repeatUntilChosenRight(bot, cfg.getString("telegram.chatId"), "Select the number of city you are leaving from", citiesToString(), x -> ((Integer.parseInt(x) > 0 && Integer.parseInt(x) <= cities().size())));
        String cityToNumInList = repeatUntilChosenRight(bot, cfg.getString("telegram.chatId"), "Select the city you are going to", citiesToString(), x -> ((Integer.parseInt(x) > 0 && Integer.parseInt(x) <= cities().size())));
        String date = repeatUntilChosenRight(bot, cfg.getString("telegram.chatId"), "Write date when you going to leave\n", "Format: 'dd.mm.yyyy';\nExample: '01.01.2024'", x -> (x.equals("13.05.2023")));
//        bot.sendMessage("", "Do you want to specify return date? Write \"YES\" or \"NO\"", cfg.getString("telegram.chatId"));
//        boolean withBackward = false;
//        String backward = bot.getBotMessage();
//        if (backward.equalsIgnoreCase("YES")) {
//            withBackward = true;
//        } else if (backward.equalsIgnoreCase("NO")) {
//            withBackward = false;
//        }
//        String backDate;
//        if (withBackward) {
//            bot.sendMessage("Write date when you going to come back", citiesToString(), cfg.getString("telegram.chatId"));
//            backDate = bot.getBotMessage();
//        }
        DataGetter dataGetter = new DataGetter(date, getStationsCode(cities().get(Integer.parseInt(cityFromNumInList))), getStationsCode(cities().get(Integer.parseInt(cityToNumInList))));
        String message = trainListToMessage(dataGetter.jsonToTrainsList(dataGetter.makeRequestGetResponse()));
        bot.sendMessage("Tickets from Tashkent to Samarkand for " + date, message, cfg.getString("telegram.chatId"));
    }

    private static String repeatUntilChosenRight(Bot bot, String chatId, String requestTitle, String request, Function<String, Boolean> f) throws IOException {
        String s = "";
        boolean isValidRequest = false;
        while (!isValidRequest) {
            bot.sendMessage(requestTitle, request, chatId);
            s = bot.getBotMessage();
            isValidRequest = f.apply(s);
        }
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

    private static String citiesToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= cities().size(); i++) {
            sb.append(i).append(". ").append(cities().get(i - 1)).append("\n");
        }
        return sb.toString();
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
