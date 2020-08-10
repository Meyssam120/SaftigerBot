package de.meyssam.saft.utils;

import de.meyssam.saft.Main;
import de.meyssam.saft.Private;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Meyssam Saghiri on Apr 28, 2020
 */
public class Utils {

    private static Weather weather;

    public static String[] args(String text) {
        return text.split(" ");
    }

    public static void channelLeaveAlg(GuildVoiceLeaveEvent event) {
        int own = 0;
        int avail = 0;
        for(VoiceChannel channel : event.getChannelLeft().getParent().getVoiceChannels()) {
            //System.out.println(channel.getName() + " " + channel.getMembers().size());
            if(channel.getName().contains("Voice ")) {
                own++;
            }
            if(channel.getMembers().size() == 0) {
                avail++;
            }
        }
        if(own != 1 && avail == 1) {
            return;
        }
        for(VoiceChannel channel : event.getChannelLeft().getParent().getVoiceChannels()) {
            if(avail > 1 && own > 0) {
                if(channel.getName().contains("Voice ")) {
                    if(channel.getMembers().size() == 0) {
                        channel.delete().queue();
                        own--;
                        avail--;
                    }
                }
            }
        }
    }

    public static void channelMoveAlg(GuildVoiceMoveEvent event) {
        int own = 0;
        int avail = 0;
        for(VoiceChannel channel : event.getChannelLeft().getParent().getVoiceChannels()) {
            //System.out.println(channel.getName() + " " + channel.getMembers().size());
            if(channel.getName().contains("Voice ")) {
                own++;
            }
            if(channel.getMembers().size() == 0) {
                avail++;
            }
        }
        if(avail == 0) {
            event.getChannelLeft().createCopy().setName("Voice " + (own+1)).queue();
            return;
        }
        //if(own != 1 && avail == 1) {
        //
        //}
        for(VoiceChannel channel : event.getChannelLeft().getParent().getVoiceChannels()) {
            if(avail > 1 && own > 0) {
                if(channel.getName().contains("Voice ")) {
                    if(channel.getMembers().size() == 0) {
                        channel.delete().queue();
                        own--;
                        avail--;
                    }
                }
            }
        }
        //2
        own = 0;
        avail = 0;
        for(VoiceChannel channel : event.getChannelJoined().getParent().getVoiceChannels()) {
            //System.out.println(channel.getName() + " " + channel.getMembers().size());
            if(channel.getName().contains("Voice ")) {
                own++;
            }
            if(channel.getMembers().size() == 0) {
                avail++;
            }
        }
        if(avail == 0) {
            event.getChannelJoined().createCopy().setName("Voice " + (own+1)).queue();
            return;
        }
        //if(own != 1 && avail == 1) {
        //
        //}
        for(VoiceChannel channel : event.getChannelJoined().getParent().getVoiceChannels()) {
            if(avail > 1 && own > 0) {
                if(channel.getName().contains("Voice ")) {
                    if(channel.getMembers().size() == 0) {
                        channel.delete().queue();
                        own--;
                        avail--;
                    }
                }
            }
        }
    }

    public static void channelJoinAlg(GuildVoiceJoinEvent e) {
        int own = 0;
        int avail = 0;
        for(VoiceChannel channel : e.getChannelJoined().getParent().getVoiceChannels()) {
            //System.out.println(channel.getName() + " " + channel.getMembers().size());
            if (channel.getName().contains("Voice ")) {
                own++;
            }
            if (channel.getMembers().size() == 0) {
                avail++;
            }
        }
        if(avail == 0) {
            e.getChannelJoined().createCopy().setName("Voice " + (own+1)).queue();
        }
    }

    public static String getWeather(Guild guild, String city) {
        try {
            weather = new Weather(city, 'C');
            String minTemp = weather.getMinTemp();
            String maxTemp = weather.getMaxTemp();
            //String rain = "es wird nicht regnen";
            //if(weather.willItRain()) {
            //    rain = "es wird regnen";
            //}
            String cond = weather.getCondition();//.replace("Overcast", "bedeckt").replace("Rain Showers", "regnerisch").replace("Clear Skybedeckt", "klar");
            if(cond == "") cond = "clear/sunny";
            String out = "The weather in " + weather.city + ":\nThe temperatures are between " + minTemp + " and " + maxTemp + ".\n" +
                    "Current condition " + cond;
            return out;
        } catch (IOException e) {

        } catch (SAXException e) {

        } catch (TransformerException e) {

        } catch (ParserConfigurationException e) {

        }
        return Messages.weatherError(guild);
    }

    public static void printHelp(MessageChannel channel, Guild guild) {
        channel.sendMessage(Messages.help(guild)).queue();
    }

    public static void printHistory(User user, GuildMessageReceivedEvent e) {
        int day = user.getTimeCreated().getDayOfMonth();
        int month = user.getTimeCreated().getMonth().getValue();
        int year = user.getTimeCreated().getYear();
        if(day<10) e.getChannel().sendMessage(Messages.discordJoined(e.getGuild()) + "0" + day + "." + month + "." + year).queue();
        else e.getChannel().sendMessage(Messages.discordJoined(e.getGuild()) + day + "." + month + "." + year).queue();
        if(e.getGuild().isMember(user)) {
            int dayj = e.getGuild().getMember(user).getTimeJoined().getDayOfMonth();
            int monthj = e.getGuild().getMember(user).getTimeJoined().getMonth().getValue();
            int yearj = e.getGuild().getMember(user).getTimeJoined().getYear();
            if(dayj<10) e.getChannel().sendMessage(Messages.serverJoined(e.getGuild()) + "0" + dayj + "." + monthj + "." + yearj).queue();
            else e.getChannel().sendMessage(Messages.serverJoined(e.getGuild()) + dayj +"."+ monthj + "." + yearj).queue();
        }
    }

    public static void clearChat(MessageChannel channel, int i) {
        for(Message message : channel.getIterableHistory()) {
            if(i > 0) {
                message.delete().queueAfter(10, TimeUnit.MILLISECONDS);
                i--;
            }
        }
        channel.sendMessage("Channel geleert! :zipper_mouth:").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
    }

    public static void clearChat(Guild guild, MessageChannel channel) {
        for (Message message : channel.getIterableHistory()) {
            message.delete().queueAfter(10, TimeUnit.MILLISECONDS);
        }
        channel.sendMessage("Channel geleert!  :zipper_mouth:").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
    }

    public static void sendChangelog(GuildMessageReceivedEvent e) {
        for (Guild guild : e.getJDA().getGuilds()) {
            guild.getOwner().getUser().openPrivateChannel().queue((channel) ->
            {
                channel.sendMessage("Du erhälst diese Nachricht, weil du ein Servereigentümer bist:").queue();
                //channel.sendMessage(Main.changelog).queue();
            });
            if(Main.tables.isCommand(guild)) {
                System.out.println(guild.getName());
                //guild.getSystemChannel().sendMessage(Main.changelog).queue();
            }
        }


    }

    public static void sendBug(GuildMessageReceivedEvent e, String[] args) {
        //if (!e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) return;

        e.getChannel().createInvite().setMaxUses(1).queue(invite -> {
            if(args.length > 1) {
                e.getJDA().getUserById(Private.msmID).openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator()
                        + ": " + e.getMessage().getContentDisplay().replace("!bug ", "")).queue());
            }
            e.getJDA().getUserById(Private.msmID).openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(invite.getUrl()).queue());
            e.getMessage().delete().queue();
            e.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(Messages.bugSent(e.getGuild())).queue());
        }, throwable -> System.out.println("Invite fehlgeschlagen"));
    }

    public static void printMessage(GuildMessageReceivedEvent e) {
        int hour = e.getMessage().getTimeCreated().getHour()+2;
        int min = e.getMessage().getTimeCreated().getMinute();
        if(min < 10) System.out.println(hour + ":0" + min + ": " + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator() + " -> " + e.getMessage().getContentRaw());
        else System.out.println(hour + ":" + min + ": " + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator() + " -> " + e.getMessage().getContentRaw());
        for(Message.Attachment attachment : e.getMessage().getAttachments()) {
            File file = new File("F:\\Discord\\BotJava\\build\\libs\\attachments\\" + e.getMessage().getId() + " " + attachment.getFileName());
            attachment.downloadToFile(file);
        }
    }

    public static void broadcastMessage(Event messageEvent, String message, boolean ignoreCommand, boolean disappear) {
        if(ignoreCommand) {
            for(Guild guild : messageEvent.getJDA().getGuilds()) {
                guild.getDefaultChannel().sendMessage(message).queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
            }
        } else {
            for(Guild guild : messageEvent.getJDA().getGuilds()) {
                if(Main.tables.isCommand(guild)) guild.getDefaultChannel().sendMessage(message).queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
            }
        }
    }

    public static String randomCase(String s) {
        Random random = new Random();
        String out = "";
        for(Character ch : s.toCharArray()) {
            if(random.nextBoolean()) {
                out += ch.toString().toUpperCase();
            } else {
                out += ch.toString().toLowerCase();
            }
        }
        return out;
    }

    public static void errorToAdmin(Guild guild, String cmd, Permission permission) {
        String perm = permission.toString();
        guild.getOwner().getUser().openPrivateChannel().queue(channel -> channel.sendMessage(Messages.adminError(guild, cmd, perm)).queue());
        System.out.println("Error -> Guild: " + guild.getIdLong() + " Command: " + cmd + " Permission: " + perm);
    }
}
