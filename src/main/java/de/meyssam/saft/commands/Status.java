package de.meyssam.saft.commands;

import de.meyssam.saft.Main;
import de.meyssam.saft.localsystem.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.util.Calendar;
import java.util.TimeZone;

public class Status {

    private final Server server;
    private final Guild guild;
    private final Member member;

    public Status(Guild guild, Member member) {
        server = getServer(guild);
        this.guild = guild;
        this.member = member;
    }

    private Server getServer(Guild guild) {
        for(Server server : Main.serverlist) {
            if(server.getId() == guild.getIdLong()) return server;
        }
        return null;
    }

    private String getCommand() {
        if (server.isCmd()) {
            return "**Aktiviert!** Alle Commands sind aktiv und nutzbar. Du kannst dir eine Übersicht mit `!help` ausgeben lassen.";
        }
        return "Deaktiviert";
    }

    private String getVoice() {
        if (server.isVoice()) {
            return "**Aktiviert!** Wenn alle Voicechannel in einer Kategorie belegt sind, wird ein neuer erstellt (und danach wieder gelöscht).";
        }
        return "Deaktiviert";
    }

    private void footer(EmbedBuilder out) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        if(calendar.get(Calendar.HOUR_OF_DAY) < 10) hour = "0" + hour;
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        if(calendar.get(Calendar.MINUTE) < 10) minute = "0" + minute;
        String second = String.valueOf(calendar.get(Calendar.SECOND));
        if(calendar.get(Calendar.SECOND) < 10) second = "0" + second;
        out.setFooter("Gesendet um " + hour + ":" + minute + ":" + second + ". Angefragt von " + member.getEffectiveName(), member.getUser().getAvatarUrl());
    }

    public EmbedBuilder getStatus() {
        int members = 0;
        int bot = 0;
        for(Member member : guild.getMembers()) {
            if(member.getUser().isBot()) bot++;
            else members++;
        }
        EmbedBuilder out = new EmbedBuilder();
        out.setTitle(guild.getName());
        out.setColor(new Color(0x059D0A));
        out.setThumbnail(guild.getIconUrl());

        out.setDescription("Im Folgenden findest du die Informationen und Präferenzen dieses Servers.");

        out.addField("Bot registriert:", "Ja", true);
        out.addField("Nutzer:", members + " Nutzer + " + bot + " Bots", true);
        out.addField("Inhaber:", guild.getOwner().getAsMention(), true);
        out.addField("Commands (!cmd):", getCommand(), true);
        out.addField("Channelverwaltung (!voice):", getVoice(), true);

        out.addBlankField(false);

        footer(out);

        return out;
    }
}
