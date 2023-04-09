package org.example;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Config cfg = ConfigFactory.parseFile(new File("application.conf"));
        Bot bot = new Bot(cfg.getString("telegram.bot.token"));
        bot.setParam("parse_mode=MarkdownV2");
        bot.sendMessage("Title! *Part two", "*Hello There!*", cfg.getString("telegram.chatId"));
    }
}
