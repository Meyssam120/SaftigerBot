import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
            String cond = weather.getCondition().replace("Overcast", "bedeckt").replace("Rain Showers", "regnerisch").replace("Clear Skybedeckt", "klar");
            if(cond == "") cond = "sonnig";
            String out = "Das Wetter in " + weather.city + ":\nDie Temperaturen liegen zwischen " + minTemp + " und " + maxTemp + ".\n" +
                    "Zurzeit ist es " + cond;
            return out;
        } catch (IOException e) {

        } catch (SAXException e) {

        } catch (TransformerException e) {

        } catch (ParserConfigurationException e) {

        }
        return "Fehler (404): Beachte bitte, dass die Städtenamen englisch sein müssen.";
    }

    public static void deleteCommandMSG(Message message){
        message.delete().queue();
    }

    public static void printHelp(MessageChannel channel) {
        String out = "Willkommen bei Meyssams saftigem Bot! :warning: Folgende Commands gibt es:\n" +
                "```!bug [<Nachricht>] = Sendet einen Bugreport an einen Botadministrator, welcher sich um das Problem kümmern wird```" +
                "```!case <Nachricht> = Sendet in deinem Namen eine interessante Nachricht in den Chat```" +
                "```!changelog = Sendet die neusten Änderungen des Bots in den Channel```" +
                "```!clearchat [<Zahl>] = Leert entweder den ganzen Channel oder eine Anzahl an Nachrichten*```" +
                "```!cmd = (De-)Aktiviert alle nicht-essenziellen Commands (mit * markiert)```"+
                "```!history [<@ oder User-ID>] = Zeigt wann ein Nutzer Discord beigetreten ist*```" +
                "```hmm = Guck was passiert*```" +
                "```stonks = Guck was passiert*```" +
                "```!voice = (De-)Aktiviert automatische Channels```"+
                "```!wetter <Stadt> = Zeigt das Wetter der Stadt an (Stadt muss englisch sein: Düsseldorf = Dusseldorf; Köln = Cologne)*```" +
                "Einige Commands benötigen die passende Berechtigung zum Benutzen.\n" +
                ":warning: Achtung: Der Voicealgorithmus ist nur für Channel gedacht, die in eine Kategorie eingeordnet sind.\"";
        channel.sendMessage(out).queue();
    }

    public static void printHistory(User user, GuildMessageReceivedEvent e) {
        int day = user.getTimeCreated().getDayOfMonth();
        int month = user.getTimeCreated().getMonth().getValue();
        int year = user.getTimeCreated().getYear();
        if(day<10) e.getChannel().sendMessage("Discord beigetreten am: 0" + day + "." + month + "." + year).queue();
        else e.getChannel().sendMessage("Discord beigetreten am: "+day + "." + month + "." + year).queue();
        if(e.getGuild().getMember(user) != null) {
            int dayj = e.getGuild().getMember(user).getTimeJoined().getDayOfMonth();
            int monthj = e.getGuild().getMember(user).getTimeJoined().getMonth().getValue();
            int yearj = e.getGuild().getMember(user).getTimeJoined().getYear();
            if(dayj<10) e.getChannel().sendMessage("Diesem Server beigetreten am: 0" + dayj + "." + monthj + "." + yearj).queue();
            else e.getChannel().sendMessage("Diesem Server beigetreten am: " +dayj +"."+ monthj + "." + yearj).queue();
        }
    }

    public static void clearChat(Guild guild, MessageChannel channel, int i) {
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
                channel.sendMessage(Main.changelog).queue();
            });
            if(Tables.isCommand(guild)) {
                System.out.println(guild.getName());
                guild.getSystemChannel().sendMessage(Main.changelog).queue();
            }
        }


    }

    public static void sendBug(GuildMessageReceivedEvent e, String[] args) {
        //if (!e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) return;

        Invite i = e.getChannel().createInvite().setMaxUses(1).complete();
        if(args.length > 1) {
            e.getJDA().getUserById(Private.msmID).openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator()
                    + ": " + e.getMessage().getContentDisplay().replace("!bug ", "")).queue());
        }
        e.getJDA().getUserById(Private.msmID).openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(i.getUrl()).queue());
        Utils.deleteCommandMSG(e.getMessage());
        e.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Fehler gesendet, ein Admin wird sich bald bei dir melden! Sollte sich innerhalb 24 Stunden niemand bei dir melden, wiederhole '!bug'").queue());
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
                if(Tables.isCommand(guild)) guild.getDefaultChannel().sendMessage(message).queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
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
}
