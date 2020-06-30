package de.meyssam.saft.localsystem;

public class Server {

    private long Id;
    private boolean registered;
    private boolean cmd;
    private boolean voice;

    public Server(long id, boolean registered, boolean cmd, boolean voice) {
        this.Id = id;
        this.registered = registered;
        this.cmd = cmd;
        this.voice = voice;
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
}
