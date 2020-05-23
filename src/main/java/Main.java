import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Meyssam Saghiri on Apr 28, 2020
 */
public class Main extends ListenerAdapter {

    public static String changelog18 = ":loudspeaker: Changelog Version 1.8-SNAPSHOT:" +
            "\n- Neuer Command: '!bug [<Nachricht>]' - Weitere Informationen mit '!help'" +
            "\n- Neuer Command: '!cmd' - Für Admins: Deaktiviert oder aktiviert nicht essentielle Commands" +
            "\n- Verbesserter Channelalgorhytmus" +
            "\n- FIX: Commands funktionieren bei neuem Server direkt, ohne Neustart";
    public static String changelog19 = ":loudspeaker: Changelog Version 1.9-SNAPSHOT:" +
            "\n Verbesserung: !history zeigt nun auch, wann man dem Server beigetreten ist" +
            "\n Bald: Möglichkeit zum Deaktivieren der automatischen Channels";
    public static String changelog191 = ":loudspeaker: Changelog Version 1.9.1-HOTFIX:" +
            "\nNeues Feature: Mit '!voice' lassen sich ähnlich zu '!cmd' die automatischen Channel deaktivieren" +
            "\nFIX: Broadcast Nachrichten, welche verschwinden sollten, taten das nicht immer" +
            "\nFIX: '!help'-Nachricht wurde leicht angepasst";
    public static String changelog110 = ":loudspeaker: Changelog Version 1.10-BETA:" +
            "\n:new:: Mit 'stonks' lässt sich das Stonks Meme in den Channel senden" +
            "\n:new:: Mit '!case <Nachricht>' kannst du deinen Gegner verwirren" +
            "\n:new:: Mit '!changelog' lässt sich der neuste Changelog des Bots abrufen" +
            "\nFIX: Viele Performanceverbesserungen wurden gemacht";
    public static String changelog = changelog110;

    public static void main(String[] args) throws LoginException {
        //test
        JDA builder = JDABuilder.createDefault(Private.token).setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.listening("'!help'")).addEventListeners(new Main(), new Commands()).build();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if(e.getAuthor().isBot()) {
            return;
        }
        //Tables.setServer(e.getGuild());
        Utils.printMessage(e);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if(e.getChannelType() != ChannelType.PRIVATE) return;
        if(!e.getAuthor().getId().equalsIgnoreCase(Private.msmID)) return;
        String[] args = Utils.args(e.getMessage().getContentRaw());
        if(e.getMessage().getContentRaw().equalsIgnoreCase("stop now")) {
            MySQL.close();
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

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onReady(@NotNull ReadyEvent e) {
        MySQL.connect();
        MySQL.createTable();
        for(String s : Tables.allServers()) {
            Guild g = e.getJDA().getGuildById(s);
            if(!e.getJDA().getGuilds().contains(g)) {
                Tables.removeServer(g,s);
            }
        }
        for(Guild guild : e.getJDA().getGuilds()) {
            Tables.setServer(guild);
        }
        System.out.println("ready");
        System.out.println("Active on " + Tables.allServers().size());
        System.out.println(e.getJDA().getGuilds().size());
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    public void onGuildJoin(GuildJoinEvent e) {
        Tables.setServer(e.getGuild());
        e.getGuild().getDefaultChannel().sendMessage(":kissing_smiling_eyes: Hi, dieser Bot läuft derzeit noch auf meinem privaten Rechner, weswegen er nicht 24 Stunden gestartet sein kann." +
                "\nHilfe gibts mit '!help' und Bugreports und Verbesserungsvorschläge an mich mit '!bug <Nachricht>'" +
                "\n:warning: Achtung: Der Voicealgorithmus ist nur für Channel gedacht, die in eine Kategorie eingeordnet sind.").queue();
        System.out.println("Join " + e.getJDA().getGuilds().size());
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent e) {
        Tables.removeServer(e.getGuild(), e.getGuild().getId());
        System.out.println("Leave " + e.getJDA().getGuilds().size());
    }


}
