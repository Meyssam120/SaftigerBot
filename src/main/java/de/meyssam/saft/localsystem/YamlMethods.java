package de.meyssam.saft.localsystem;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class YamlMethods {

    public void makeFileIfNotExists() {
        File file = new File("F:\\Discord\\BotJava\\build\\libs\\servers.yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Server.java bei CreateNewFile - Exception");
            }
        }
    }

    public ArrayList<Server> serverList() {
        try {
            InputStream input = new FileInputStream(new File("F:\\Discord\\BotJava\\build\\libs\\servers.yml"));
            Yaml yaml = new Yaml();
            List<String> list = yaml.load(input);
            System.out.println(list);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
