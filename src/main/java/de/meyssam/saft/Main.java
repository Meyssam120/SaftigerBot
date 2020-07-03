package de.meyssam.saft;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import de.meyssam.saft.events.Commands;
import de.meyssam.saft.events.Events;
import de.meyssam.saft.localsystem.LocalTables;
import de.meyssam.saft.localsystem.Server;
import de.meyssam.saft.utils.Messages;
import de.meyssam.saft.utils.MySQL;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;

/**
 * Created by Meyssam Saghiri on Apr 28, 2020
 * TODO: Switch to local file system instead of mysql
 */
public class Main extends ListenerAdapter {

    public static String changelog = Messages.changelog113;
    public static EventWaiter waiter;
    public static LocalTables tables;
    public static ArrayList<Server> serverlist;

    public static void main(String[] args) throws LoginException {
        waiter = new EventWaiter();
        tables = new LocalTables();
        JDA builder = JDABuilder.createDefault(Private.token).setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.listening("'!help'")).addEventListeners(new Main(), new Commands(waiter), new Events(), waiter).build();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onReady(ReadyEvent e) {
        //new YamlMethods().makeFileIfNotExists();
        serverlist = new LocalTables().getAsArray();
        //tables.setServer(e.getJDA().getGuildById(695933469641146428L));
        addAndRemove(e);
        System.out.println("Active on " + serverlist.size());
        System.out.println("ready");
        System.out.println(e.getJDA().getGuilds().size());
        e.getJDA().getGuildById(695933469641146428L).getAudioManager().openAudioConnection(e.getJDA().getGuildById(695933469641146428L).getVoiceChannelById(695933469913645088L));
    }

    private void addAndRemove(ReadyEvent e) {
        for(Guild guild : e.getJDA().getGuilds()) {
            tables.setServer(guild);
        }

        //for(int i = 0; i < (e.getJDA().getGuilds().size() - 1); i++) {
        //    if(server.getId() == e.getJDA().getGuilds().get(i).getIdLong()) {
        //        break;
        //    }
        //    if(i == (e.getJDA().getGuilds().size() - 1)) {
        //        serverlist.remove(server);
        //        tables.saveAsString(serverlist);
        //    }
        //}
        serverlist.removeIf(server -> !e.getJDA().getGuilds().contains(e.getJDA().getGuildById(server.getId())));
        tables.saveAsString(serverlist);

    }


    public void startSQL(Event e) {
        MySQL.connect();
        MySQL.createTable();
        for(String s : tables.allServers()) {
            Guild g = e.getJDA().getGuildById(s);
            if(!e.getJDA().getGuilds().contains(g)) {
                tables.removeServer(g,s);
                //FileManager.write(g, "webhook", "null");
            }
        }
        for(Guild guild : e.getJDA().getGuilds()) {
            if(!tables.isRegistered(guild)) {
                guild.getDefaultChannel().sendMessage(Messages.welcome).queue();
                System.out.println("Join " + e.getJDA().getGuilds().size());
            }
            tables.setServer(guild);
            //if(Tables.isCommand(guild)) {
            //    guild.getDefaultChannel().sendMessage("BOT ONLINE").queue(message -> message.delete().queueAfter(10, TimeUnit.SECONDS));
            //}
        }
    }



}
