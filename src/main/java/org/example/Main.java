package org.example;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Config cfg = ConfigFactory.parseFile(new File("application.conf"));
        Bot bot = new Bot(cfg.getString("telegram.bot.token"));
        DataGetter dataGetter = new DataGetter();
        bot.sendMessage("Tickets from Tashkent to Samarkand for May13:\n",
                trainListToMessage(DataGetter.jsonToTrainsList(dataGetter.makeRequestGetResponse())),
                cfg.getString("telegram.chatId"));
    }

    public static void findTickets() throws IOException {
        Config cfg = ConfigFactory.parseFile(new File("application.conf"));
        Bot bot = new Bot(cfg.getString("telegram.bot.token"));
        bot.sendMessage("Select the number of city you are leaving from", citiesToString(), cfg.getString("telegram.chatId"));
        int cityFrom = Integer.parseInt(bot.getBotMessage());
        bot.sendMessage("Select the city you are going to", citiesToString(), cfg.getString("telegram.chatId"));
        int cityTo = Integer.parseInt(bot.getBotMessage());
        bot.sendMessage("Do you want to specify return date? Write \"YES\" or \"NO\"", citiesToString(), cfg.getString("telegram.chatId"));
        boolean withBackward = false;
        String backward = bot.getBotMessage();
        if (backward.equalsIgnoreCase("YES")) {
            withBackward = true;
        } else if (backward.equalsIgnoreCase("NO")) {
            withBackward = false;
        }
        bot.sendMessage("Write date when you going to leave", citiesToString(), cfg.getString("telegram.chatId"));
        String leavingDate = bot.getBotMessage();
        String backDate;
        if (withBackward) {
            bot.sendMessage("Write date when you going to come back", citiesToString(), cfg.getString("telegram.chatId"));
            backDate = bot.getBotMessage();
        }
        new DataGetter().makeRequestGetResponse();
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
        return list;
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
