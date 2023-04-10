package org.example;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Config cfg = ConfigFactory.parseFile(new File("application.conf"));
        Bot bot = new Bot(cfg.getString("telegram.bot.token"));
//        bot.sendMessage("Title! *Part two", "*Hello There!*", cfg.getString("telegram.chatId"));
        bot.sendMessage("Select the number of city you are leaving from", citiesToString(), cfg.getString("telegram.chatId"));
        Thread.sleep(10000);
        int cityFrom = bot.getCityNum();
        bot.sendMessage("Select the city you are going to", citiesToString(), cfg.getString("telegram.chatId"));
        Thread.sleep(10000);
        int cityTo = bot.getCityNum();
        bot.sendRequest();
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
}
