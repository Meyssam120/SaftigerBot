package de.meyssam.saft.localsystem;

import de.meyssam.saft.Main;
import de.meyssam.saft.utils.FileManager;
import de.meyssam.saft.utils.Messages;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;

public class LocalTables implements FileSystem {



    public void saveAsString(ArrayList<Server> list) {
        StringBuilder builder = new StringBuilder();
        for(Server server : list) {
            builder.append(server.getId()).append(" ");
            builder.append(server.isRegistered()).append(" ");
            builder.append(server.isCmd()).append(" ");
            builder.append(server.isVoice()).append(".");
        }
        builder.deleteCharAt(builder.length()-1);
        FileManager.write("array", builder);
    }

    public ArrayList<Server> getAsArray() {
        ArrayList<Server> out = new ArrayList<>();
        String a = FileManager.readFile("array");
        assert a != null;
        String[] server = a.split("\\.");
        for (String s : server) {
            String[] temp = s.split(" ");
            out.add(new Server(Long.parseLong(temp[0]), Boolean.parseBoolean(temp[1]), Boolean.parseBoolean(temp[2]), Boolean.parseBoolean(temp[3])));
        }
        return out;
    }

    @Override
    public void setServer(Guild guild) {
        //if(!isRegistered(guild)) {
        //  FileManager.write(guild, "registered", "true");
        //  FileManager.write(guild, "cmd", "true");
        //  FileManager.write(guild, "voice", "true");
        //}
        //FileManager.addServer(guild);
        if(isRegistered(guild)) return;
        Main.serverlist.add(new Server(guild.getIdLong(), true, true, true));
        Main.tables.saveAsString(Main.serverlist);
        guild.getDefaultChannel().sendMessage(Messages.welcome).queue();
        System.out.println("Join " + guild.getJDA().getGuilds().size());
    }

    @Override
    public void removeServer(Guild guild, String id) {
        if(isRegistered(guild)) {
            //FileManager.write(guild, "registered", "false");
            Main.serverlist.remove(getServer(guild));
            Main.tables.saveAsString(Main.serverlist);
            System.out.println("Leave " + guild.getJDA().getGuilds().size());
        }
    }

    @Override
    public boolean isCommand(Guild guild) {
        if(getServer(guild).isCmd()) return true;
        //if(FileManager.readFile(guild, "cmd").equalsIgnoreCase("true")) return true;
        return false;
    }

    @Override
    public boolean isVoice(Guild guild) {
        if(getServer(guild).isVoice()) return true;
        //if(FileManager.readFile(guild, "voice").equalsIgnoreCase("true")) return true;
        return false;
    }

    @Override
    public void setVoice(Guild guild) {
        if(isVoice(guild)) getServer(guild).setVoice(false);
        else if(!isVoice(guild)) getServer(guild).setVoice(true);
        Main.tables.saveAsString(Main.serverlist);
    }

    @Override
    public void setCommand(Guild guild) {
        if(isCommand(guild)) getServer(guild).setCmd(false);
        else if(!isCommand(guild)) getServer(guild).setCmd(true);
        Main.tables.saveAsString(Main.serverlist);
    }

    @Override
    public boolean isRegistered(Guild guild) {
        if(getServer(guild) == null) return false;
        return true;
    }

    private Server getServer(Guild guild) {
        for(Server server : Main.serverlist) {
            if(server.getId() == guild.getIdLong()) return server;
        }
        return null;
    }


    @Override
    public List<String> allServers() {
        return null;
    }
}
