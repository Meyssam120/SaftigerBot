package de.meyssam.saft.utils;

import net.dv8tion.jda.api.entities.Guild;

import java.io.*;
import java.util.Properties;

/**
 * Created by Meyssam Saghiri on Apr 29, 2020
 */
public class FileManager {

    static File configFile = new File("F:\\Discord\\BotJava\\build\\libs\\config.properties");

    public static String readFile(Guild guild, String path) {
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            String read = props.getProperty(guild.getId() + "." + path);
            reader.close();
            return read;
        } catch (FileNotFoundException ex) {
            System.out.println("Datei fehlt");
        } catch (IOException ex) {
            System.out.println("EX");
        }
        return null;
    }

    public static String readFile(String path) {
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            String read = props.getProperty(path);
            reader.close();
            return read;
        } catch (FileNotFoundException ex) {
            System.out.println("Datei fehlt");
        } catch (IOException ex) {
            System.out.println("EX");
        }
        return null;
    }

    public static void write(Guild guild, String path, String content) {
        try {
            String id = guild.getId();
            Properties props = new Properties();
            props.setProperty(id + "x" + path, content);
            FileWriter writer = new FileWriter(configFile);
            props.store(writer, "Server config");
            writer.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Datei fehlt");
        } catch (IOException ex) {
            // I/O error
        }
    }

    public static void write(String path, StringBuilder obj) {
        try {
            Properties props = new Properties();
            props.setProperty(path, obj.toString());
            FileWriter writer = new FileWriter(configFile);
            props.store(writer, "Server config");
            writer.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Datei fehlt");
        } catch (IOException ex) {
            // I/O error
        }
    }

    public static void addWebhook(Guild guild, String link) {
        File pfile = new File("F:\\Discord\\BotJava\\build\\libs\\" + guild.getId() + ".properties");
        String out = link + "/github";
        try {
            if(!pfile.exists()) pfile.createNewFile();
            Properties props = new Properties();
            props.setProperty("webhook", out);
            FileWriter writer = new FileWriter(pfile);
            props.store(writer, "Webhook");
            writer.close();
        } catch (IOException e) {

        }
    }

}
