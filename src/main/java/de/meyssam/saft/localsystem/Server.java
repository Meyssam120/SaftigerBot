package de.meyssam.saft.localsystem;

import de.meyssam.saft.Language;

public class Server {

    private long Id;
    private boolean registered;
    private boolean cmd;
    private boolean voice;
    private Language language;

    public Server(long id, boolean registered, boolean cmd, boolean voice, Language language) {
        this.Id = id;
        this.registered = registered;
        this.cmd = cmd;
        this.voice = voice;
        this.language = language;
    }

    public long getId() {
        return Id;
    }

    public boolean isRegistered() {
        return registered;
    }

    public boolean isCmd() {
        return cmd;
    }

    public boolean isVoice() {
        return voice;
    }

    public Language getLanguage() {
        return language;
    }

    public void setId(long id) {
        Id = id;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public void setCmd(boolean cmd) {
        this.cmd = cmd;
    }

    public void setVoice(boolean voice) {
        this.voice = voice;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

}
