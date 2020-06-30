package de.meyssam.saft.localsystem;

import net.dv8tion.jda.api.entities.Guild;

import java.util.List;

public interface FileSystem {

    void setServer(Guild guild);
    void removeServer(Guild guild, String id);
    boolean isCommand(Guild guild);
    boolean isVoice(Guild guild);
    void setVoice(Guild guild);
    void setCommand(Guild guild);
    boolean isRegistered(Guild guild);
    List<String> allServers();

    }
