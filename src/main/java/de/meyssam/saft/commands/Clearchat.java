package de.meyssam.saft.commands;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import de.meyssam.saft.utils.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.concurrent.TimeUnit;

public class Clearchat {

    private GuildMessageReceivedEvent e;
    private EventWaiter waiter;
    private String[] args;

    public Clearchat(GuildMessageReceivedEvent e, EventWaiter waiter, String[] args) {
        this.e = e;
        this.waiter = waiter;
        this.args = args;
    }

    public void clearChat() {
        e.getChannel().sendTyping().queue();
        if(!e.getMember().getPermissions().contains(Permission.MESSAGE_MANAGE)) {
            e.getMessage().delete().queueAfter(3, TimeUnit.SECONDS);
            e.getChannel().sendMessage(e.getAuthor().getAsMention() + " Du hat nicht die Berechtigung MESSAGE_MANAGE").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
            return;
        }
        if(!e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            Utils.errorToAdmin(e.getGuild(), "!clearchat", Permission.MESSAGE_MANAGE);
            return;
        }
        if(!e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_ADD_REACTION)) {
            Utils.errorToAdmin(e.getGuild(), "!clearchat", Permission.MESSAGE_ADD_REACTION);
            return;
        }
        if(args.length == 1) {
            e.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
            e.getChannel().sendMessage("Willst du wirklich alle Nachrichten aus dem Channel löschen?").queue(message -> {
                message.addReaction("\u2705").queue();
                message.addReaction("\u274C").queue();
            });
            waiter.waitForEvent(GuildMessageReactionAddEvent.class, event -> event.getMember() == e.getMember(), evt -> {
                if(!evt.getReactionEmote().getAsCodepoints().equals("U+2705")) {
                    e.getChannel().deleteMessageById(evt.getMessageId()).queueAfter(2, TimeUnit.SECONDS);
                    return;
                }
                Utils.clearChat(e.getGuild(), e.getChannel());
            });
        } else if(args.length == 2) {
            int i = Integer.parseInt(args[1]) + 1;
            Utils.clearChat(e.getChannel(), i);
        }
    }

}
