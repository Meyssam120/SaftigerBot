package de.meyssam.saft.events;

import de.meyssam.saft.Main;
import de.meyssam.saft.Private;
import de.meyssam.saft.utils.Utils;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Created by Meyssam Saghiri on Mai 25, 2020
 */
public class Events extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if(e.getAuthor().isBot()) {
            return;
        }
        //de.meyssam.saft.utils.Tables.setServer(e.getGuild());
        Utils.printMessage(e);
    }

    @Override
    public void onGuildJoin(GuildJoinEvent e) {
        Main.tables.setServer(e.getGuild());
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent e) {
        Main.tables.removeServer(e.getGuild(), e.getGuild().getId());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if(e.getChannelType() != ChannelType.PRIVATE) return;
        if(!e.getAuthor().getId().equalsIgnoreCase(Private.msmID)) return;
        String[] args = Utils.args(e.getMessage().getContentRaw());
        if(e.getMessage().getContentRaw().equalsIgnoreCase("stop now")) {
            Main.tables.saveAsString(Main.serverlist);
            //MySQL.close();
            e.getChannel().sendMessage("Stopping...").complete();
            System.out.println("Stopping...");
            e.getJDA().shutdown();
            System.out.println("Stopped!");
            return;
        }
        if(args[0].equalsIgnoreCase("!broadcast")) {
            if(!e.getAuthor().getId().equalsIgnoreCase(Private.msmID)) return;
            boolean disappear = true;
            boolean ignore = true;
            if(args[1].equalsIgnoreCase("false")) disappear = false;
            if(args[2].equalsIgnoreCase("false")) ignore = false;
            Utils.broadcastMessage(e, e.getMessage().getContentRaw().replace("!broadcast ", "").replace("true ", "").replace("false ", ""), ignore, disappear);
        }
    }
}
