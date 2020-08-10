package de.meyssam.saft.utils;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import de.meyssam.saft.Language;
import de.meyssam.saft.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Meyssam Saghiri on Jun 11, 2020
 */
public class Mastermind {

    public String createZahl() {
        Random random = new Random();
        StringBuilder out = new StringBuilder();
        for(int i = 0; i < 4; i++) {
            out.append(random.nextInt(10));
        }
        return out.toString();
    }

    public Integer checkZahlen(String zahl, String message) {
        HashMap<Character, Integer> characterIntegerHashMap = new HashMap<>();
        int richtig = 0;

        for(Character einzelZahl : zahl.toCharArray()) {
            if(!characterIntegerHashMap.containsKey(einzelZahl)) {
                characterIntegerHashMap.put(einzelZahl, 1);
            } else {
                characterIntegerHashMap.replace(einzelZahl, characterIntegerHashMap.get(einzelZahl)+1);
            }
        }

        for(Character einzelNachricht : message.toCharArray()) {
            if(characterIntegerHashMap.containsKey(einzelNachricht)) {
                richtig++;
                if(characterIntegerHashMap.get(einzelNachricht) == 1) {
                    characterIntegerHashMap.remove(einzelNachricht);
                } else {
                    characterIntegerHashMap.replace(einzelNachricht, characterIntegerHashMap.get(einzelNachricht)-1);
                }
            }
        }

        return richtig;
    }

    public Integer checkPositionen(String zahl, String message) {
        int richtig = 0;

        for (int i = 0; i < 4; i++) {
            if(zahl.charAt(i) == message.charAt(i)) {
                richtig++;
            }
        }

        return richtig;
    }

    public void playMastermind(EventWaiter waiter, GuildMessageReceivedEvent e) {
        String zahl = createZahl();
        System.out.println(zahl);
        e.getChannel().sendMessage(e.getAuthor().getAsMention() + Messages.mastermindBegin(e.getGuild())).queue();
        waitForMasterMind(waiter, e.getAuthor().getIdLong(), zahl, 0);
    }

    public void waitForMasterMind(EventWaiter waiter, long user, String zahl, int zaehler) {
        waiter.waitForEvent(GuildMessageReceivedEvent.class, guildM -> guildM.getAuthor().getIdLong() == user, event -> {
            User userr = event.getJDA().getUserById(user);
            String message = event.getMessage().getContentRaw().replace("!mastermind ", "");
            if(message.equalsIgnoreCase("stop")) {
                event.getChannel().sendMessage(userr.getAsMention() + Messages.mastermindAbort(event.getGuild())).queue();
                return;
            }
            if(message.toCharArray().length != 4) {
                event.getChannel().sendMessage(userr.getAsMention() + Messages.mastermindDigits(event.getGuild())).queue();
                waitForMasterMind(waiter, user, zahl, zaehler);
                return;
            }
            try {
                Integer.parseInt(message);
            }catch (NumberFormatException exception) {
                event.getChannel().sendMessage(userr.getAsMention() + Messages.mastermindDigits(event.getGuild())).queue();
                return;
            }

            if(!zahl.contains(message)) {
                event.getChannel().sendMessage(rueckmeldung(event.getGuild(), zahl, message)).queue();
                waitForMasterMind(waiter, user, zahl, zaehler+1);
            } else {
                event.getChannel().sendMessage(userr.getAsMention() + Messages.mastermindCorrect(event.getGuild(), zaehler)).queue();
            }
        });
    }

    private String rueckmeldung(Guild guild, String zahl, String message) {
        Language language = Main.getServer(guild).getLanguage();
        if(language == Language.DE) return "Du hast **" + checkZahlen(zahl, message) + " Zahl(en)** und **" + checkPositionen(zahl, message) + " Stelle(n)** richtig. Gib eine neue Zahl ein!";
        else return "You got **" + checkZahlen(zahl, message) + " number(s)** and **" + checkPositionen(zahl, message) + " position(s)** right.";
    }

}
