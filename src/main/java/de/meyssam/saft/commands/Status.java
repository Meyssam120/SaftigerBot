package de.meyssam.saft.commands;

import de.meyssam.saft.Main;
import de.meyssam.saft.localsystem.Server;
import de.meyssam.saft.utils.Messages;
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
            return Messages.statusCommandActivated(server);
        }
        return Messages.deactivated(server);
    }

    private String getVoice() {
        if (server.isVoice()) {
            return Messages.statusVoiceActivated(server);
        }
        return Messages.deactivated(server);
    }

    private void footer(EmbedBuilder out) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        if(calendar.get(Calendar.HOUR_OF_DAY) < 10) hour = "0" + hour;
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        if(calendar.get(Calendar.MINUTE) < 10) minute = "0" + minute;
        String second = String.valueOf(calendar.get(Calendar.SECOND));
        if(calendar.get(Calendar.SECOND) < 10) second = "0" + second;
        out.setFooter(Messages.statusFooter(server, hour, minute, second, member.getEffectiveName()), member.getUser().getAvatarUrl());
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

        out.setDescription(Messages.statusDescription(server));

        out.addField(Messages.statusSprache(server), Main.getServer(guild).getLanguage().getName(), true);
        out.addField(Messages.statusNutzer(server), Messages.statusNutzerCount(server, members, bot), true);
        out.addField(Messages.statusOwner(server), guild.getOwner().getAsMention(), true);
        out.addField("Commands (!cmd):", getCommand(), true);
        out.addField(Messages.statusChannel(server), getVoice(), true);

        out.addBlankField(false);

        footer(out);

        return out;
    }
}
