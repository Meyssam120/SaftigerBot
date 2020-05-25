package de.meyssam.saft.utils;

import net.dv8tion.jda.api.entities.Guild;

import java.io.*;
import java.util.Properties;

/**
 * Created by Meyssam Saghiri on Apr 29, 2020
 */
public class FileManager {

    static File configFile = new File("config.properties");

    public static String readFile(Guild guild, String path) {
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            String read = props.getProperty(guild + "." + path);
            reader.close();
            return read;
        } catch (FileNotFoundException ex) {
            System.out.println("Datei fehlt");
        } catch (IOException ex) {
            System.out.println();
        }
        return null;
    }

    public static void write(Guild guild, String path, String content) {
        try {
            String id = guild.getId();
            Properties props = new Properties();
            props.setProperty(id + "." + path, content);
            FileWriter writer = new FileWriter(configFile);
            props.store(writer, "Server configs");
            writer.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Datei fehlt");
        } catch (IOException ex) {
            // I/O error
        }
    }

}
