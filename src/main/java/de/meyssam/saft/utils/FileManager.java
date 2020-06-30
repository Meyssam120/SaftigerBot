package de.meyssam.saft.utils;

import net.dv8tion.jda.api.entities.Guild;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
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

    public static void addServer(Guild guild) {
        try {
            String id = guild.getId();
            Properties props = new Properties();
            props.setProperty(id + ".registered", "true");
            props.setProperty(id + ".cmd", "true");
            props.setProperty(id + ".voice", "true");
            FileWriter writer = new FileWriter("config.properties");
            props.store(writer, "Server config");
            writer.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Datei fehlt");
        } catch (IOException ex) {
            // I/O error
        }
    }


    public static List<String> getEntries() throws IOException {
        List<String> out = new ArrayList<>();
        FileReader reader = new FileReader(configFile);
        Properties props = new Properties();
        props.load(reader);
        System.out.println(props.keySet());
        return null;
    }

}
