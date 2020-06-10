package de.meyssam.saft;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import de.meyssam.saft.events.Commands;
import de.meyssam.saft.events.Events;
import de.meyssam.saft.utils.Messages;
import de.meyssam.saft.utils.MySQL;
import de.meyssam.saft.utils.Tables;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Meyssam Saghiri on Apr 28, 2020
 */
public class Main extends ListenerAdapter {

    public static String changelog = Messages.changelog110;
    public static EventWaiter waiter;

    public static void main(String[] args) throws LoginException {
        waiter = new EventWaiter();
        JDA builder = JDABuilder.createDefault(Private.token).setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.listening("'!help'")).addEventListeners(new Main(), new Commands(waiter), new Events(), waiter).build();
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
                //FileManager.write(g, "webhook", "null");
            }
        }
        for(Guild guild : e.getJDA().getGuilds()) {
            if(!Tables.isRegistered(guild)) {
                guild.getDefaultChannel().sendMessage(Messages.welcome).queue();
                System.out.println("Join " + e.getJDA().getGuilds().size());
            }
            Tables.setServer(guild);
            if(Tables.isCommand(guild)) {
                guild.getDefaultChannel().sendMessage("BOT ONLINE").queue(message -> message.delete().queueAfter(10, TimeUnit.SECONDS));
            }
        }
        System.out.println("ready");
        System.out.println("Active on " + Tables.allServers().size());
        System.out.println(e.getJDA().getGuilds().size());
        e.getJDA().getGuildById(695933469641146428L).getAudioManager().openAudioConnection(e.getJDA().getGuildById(695933469641146428L).getVoiceChannelById(695953357059260467L));
    }



}
