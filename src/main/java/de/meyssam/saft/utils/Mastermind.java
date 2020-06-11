package de.meyssam.saft.utils;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Meyssam Saghiri on Jun 11, 2020
 */
public class Mastermind {

    public static String createZahl() {
        Random random = new Random();
        StringBuilder out = new StringBuilder();
        for(int i = 0; i < 4; i++) {
            out.append(random.nextInt(10));
        }
        return out.toString();
    }

    public static Integer checkZahlen(String zahl, String message) {
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

    public static Integer checkPositionen(String zahl, String message) {
        int richtig = 0;

        for (int i = 0; i < 4; i++) {
            if(zahl.charAt(i) == message.charAt(i)) {
                richtig++;
            }
        }

        return richtig;
    }

    public static void playMastermind(EventWaiter waiter, GuildMessageReceivedEvent e) {
        String zahl = createZahl();
        System.out.println(zahl);
        e.getChannel().sendMessage(e.getAuthor().getAsMention() + " Du spielst jetzt **Mastermind**. Zum Beenden, errate entweder die Zahl, oder schreibe **stop**").queue();
        waitForMasterMind(waiter, e.getAuthor(), zahl, 0);
    }

    public static void waitForMasterMind(EventWaiter waiter, User user, String zahl, int zaehler) {
        waiter.waitForEvent(GuildMessageReceivedEvent.class, guildM -> guildM.getAuthor() == user, event -> {
            String message = event.getMessage().getContentRaw().replace("!mastermind ", "");
            if(message.equalsIgnoreCase("stop")) {
                event.getChannel().sendMessage(user.getAsMention() + " Mastermind wurde **abgebrochen**").queue();
                return;
            }
            if(message.toCharArray().length != 4) {
                event.getChannel().sendMessage(user.getAsMention() + " Deine Zahl muss **4 Stellen** haben, versuche es erneut!").queue();
                waitForMasterMind(waiter, user, zahl, zaehler);
                return;
            }
            try {
                Integer.parseInt(message);
            }catch (NumberFormatException exception) {
                event.getChannel().sendMessage(user.getAsMention() + " Du musst eine **vierstellige Zahl** verwenden").queue();
                return;
            }

            if(!zahl.equals(message)) {
                event.getChannel().sendMessage(rueckmeldung(zahl, message)).queue();
                waitForMasterMind(waiter, user, zahl, zaehler+1);
            } else {
                event.getChannel().sendMessage(user.getAsMention() + " Herzlichen Glückwunsch, deine Zahl ist richtig! Gebrauchte Versuche: **" + zaehler + "**").queue();
            }
        });
    }

    public static String rueckmeldung(String zahl, String message) {
        return "Du hast **" + checkZahlen(zahl, message) + " Zahlen** und **" + checkPositionen(zahl, message) + " Stellen** richtig. Gib eine neue Zahl ein!";
    }

}
