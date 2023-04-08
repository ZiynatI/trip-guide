package org.example;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Config cfg = ConfigFactory.parseFile(new File("application.conf"));
        new Bot(cfg.getString("telegram.bot.token")).sendMessage("Hi)", cfg.getString("telegram.chatId"));
    }
}
