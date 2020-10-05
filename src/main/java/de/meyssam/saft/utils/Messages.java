package de.meyssam.saft.utils;

import de.meyssam.saft.Language;
import de.meyssam.saft.Main;
import de.meyssam.saft.localsystem.Server;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;

/**
 * Created by Meyssam Saghiri on Mai 25, 2020
 */
public class Messages {

    @Deprecated
    public static String welcomeOld = ":kissing_smiling_eyes: Hi, dieser Bot läuft derzeit noch auf meinem privaten Rechner, weswegen er nicht 24 Stunden gestartet sein kann." +
            "\nHilfe gibts mit '!help' und Bugreports und Verbesserungsvorschläge an mich mit '!bug <Nachricht>'" +
            "\n:warning: Achtung: Der Voicealgorithmus ist nur für Channel gedacht, die in eine Kategorie eingeordnet sind.";

    public static String welcome = ":kissing_smiling_eyes: Hi, this bot is currently running on my personal computer which is why it can not be online 247." +
            "\nRequest help with '!help' and send bugreports and feature requests with '!bug <Message>'" +
            "\n:warning: Caution: Automatic channels are only working if the others are in a category. For more information go to '!status'." +
            "\n**Switch language with '!lang'**";


    public static String help(Guild guild) {
        Language language = Main.getServer(guild).getLanguage();
        if(language == Language.DE) return "Willkommen bei Meyssams saftigem Bot! :warning: Folgende Commands gibt es:\n" +
                "```!bug [<Nachricht>] = Sendet einen Bugreport an einen Botadministrator, welcher sich um das Problem kümmern wird```" +
                "```!case <Nachricht> = Sendet in deinem Namen eine interessante Nachricht in den Chat*```" +
                "```!clearchat [<Zahl>] = Leert entweder den ganzen Channel oder eine Anzahl an Nachrichten```" +
                "```!cmd = (De-)Aktiviert alle nicht-essenziellen Commands (mit * markiert)```"+
                "```!history [<@ oder User-ID>] = Zeigt wann ein Nutzer Discord beigetreten ist*```" +
                "```hmm = Guck was passiert*```" +
                "```!join = Lässt den Bot in deinen akutellen Channel joinen*```" +
                "```!mastermind <vierstellige Zahl> = Spiele mit dem Bot das Spiel **Mastermind***```" +
                "```!status = Sendet dir den Status und die Präferenzen des Servers```" +
                "```stonks = Guck was passiert*```" +
                "```!updates = Wird ab sofort in diesen Channel die Entwicklungsfortschritte des Bots broadcasten```" +
                "```!voice = (De-)Aktiviert automatische Channels```"+
                "```!weather <Stadt> = Zeigt das Wetter der Stadt an (Stadt muss englisch sein: Düsseldorf = Dusseldorf; Köln = Cologne)*```" +
                "Einige Commands benötigen die passende Berechtigung.\n" +
                ":warning: Die Channelverwaltung ist nur für Channel gedacht, die in eine Kategorie eingeordnet sind. Für mehr Infos `!status`\n" +
                "Legende: [] = optional";
        return "Welcome to Meyssam's 'juicy' Bot! :warning: The following commands are available for you:\n" +
                "```!bug [<Message>] = Sends a bugreport to an bot admin, who will contact you soon```" +
                "```!case <Message> = Sends the input message with random case letters*```" +
                "```!clearchat [<Number>] = Clears the whole text channel or a specific count of messages```" +
                "```!cmd = (De-)Activates all unnecessary commands (tagged with *)```"+
                "```!history [<@ or User-ID>] = When did a user join Discord?*```" +
                "```hmm = Meme*```" +
                "```!join = Makes the bot join in your current voice channel*```" +
                "```!mastermind <Number (4 digits)> = Play **Mastermind** against the bot*```" +
                "```!status = Prints out the server status and preferences of the bot```" +
                "```stonks = Meme*```" +
                "```!updates = Subscribes to changelogs which will be sent to this channel```" +
                "```!voice = (De-)Activates automatic channels```"+
                "```!weather <City> = Retrieves the weather*```" +
                "You need specific permissions for some commands.\n" +
                ":warning: Automatic channels only work for channels in a category. For more information `!status`\n" +
                "Legend: [] = optional";
    }

    public static String noPermissionUser(Guild guild, Permission permission) {
        Language language = Main.getServer(guild).getLanguage();
        if(language == Language.DE) return "Hierfür fehlen dir die nötigen Rechte! " + permission.toString();
        return "You lack permissions to do that! " + permission.toString();
    }

    public static String noPermissionBot(Guild guild, Permission permission) {
        Language language = Main.getServer(guild).getLanguage();
        if(language == Language.DE) return "Dem Bot fehlt die nötige Berechtigung: " + permission.toString();
        return "The bot needs a permission to do that: " + permission.toString();
    }

    public static String wrongSyntax(Guild guild, String syntax) {
        Language language = Main.getServer(guild).getLanguage();
        if(language == Language.DE) return "Das hat nicht geklappt. Nutze folgende Syntax: **" + syntax + "**.";
        return "Oops... that didn't work. Stick to syntax: **" + syntax + "**";
    }

    public static String changeCommand(Guild guild) {
        Language language = Main.getServer(guild).getLanguage();
        if(language == Language.DE) {
            if (Main.tables.isCommand(guild)) return "Commands sind jetzt auf diesem Server aktiviert.";
            else return "Commands sind nun auf diesem Server deaktiviert.";
        } else {
            if (Main.tables.isCommand(guild)) return "Commands are now activated on this server.";
            else return "Commands are now deactivated on this server.";
        }
    }

    /*
    MASTERMIND
     */

    public static String mastermindBegin(Guild guild) {
        Language language = Main.getServer(guild).getLanguage();
        if(language == Language.DE) return " Du spielst jetzt **Mastermind**. Zum Beenden, errate entweder die Zahl, oder schreibe **stop**";
        else return " You're now playing **Mastermind**. To end the game either guess the number by typing it or with **stop**.";
    }

    public static String mastermindAbort(Guild guild) {
        Language language = Main.getServer(guild).getLanguage();
        if(language == Language.DE) return " Mastermind wurde **abgebrochen**.";
        else return " Mastermind has been **stopped**.";
    }

    public static String mastermindDigits(Guild guild) {
        Language language = Main.getServer(guild).getLanguage();
        if(language == Language.DE) return " Deine Zahl muss **4 Stellen** haben, versuche es erneut!";
        else return " Your number has to have **4 digits**, try again!";
    }

    public static String mastermindCorrect(Guild guild, int zaehler) {
        Language language = Main.getServer(guild).getLanguage();
        if(language == Language.DE) return " Herzlichen Glückwunsch, deine Zahl ist richtig! Gebrauchte Versuche: **" + zaehler + "**";
        else return " Congratulations, you've guessed right! Needed tries: **" + zaehler + "**";
    }

    /*
    STATUS
     */

    public static String deactivated(Server guild) {
        Language language = guild.getLanguage();
        if(language == Language.DE) return "Deaktiviert";
        else return "Deactivated";
    }

    public static String statusDescription(Server guild) {
        Language language = guild.getLanguage();
        if(language == Language.DE) return "Im Folgenden findest du die Informationen und Präferenzen dieses Servers.";
        else return "You're now seeing some information and the preferences of this server.";
    }

    public static String statusFooter(Server guild, String hour, String minute, String second, String name) {
        Language language = guild.getLanguage();
        if(language == Language.DE) return "Gesendet um " + hour + ":" + minute + ":" + second + ". Angefragt von " + name;
        else return "Sent at " + hour + ":" + minute + ":" + second + ". Requested by " + name;
    }

    public static String statusOwner(Server guild) {
        if(guild.getLanguage() == Language.DE) return "Besitzer: ";
        else return "Owner: ";
    }

    public static String statusNutzerCount(Server guild, int members, int bot) {
        Language language = guild.getLanguage();
        if(language == Language.DE) return members + " Nutzer + " + bot + " Bots";
        else return members + " user + " + bot + " bots";
    }

    public static String statusNutzer(Server guild) {
        Language language = guild.getLanguage();
        if(language == Language.DE) return "Nutzer:";
        else return "User count:";
    }

    public static String statusSprache(Server guild) {
        Language language = guild.getLanguage();
        if(language == Language.DE) return "Sprache:";
        else return "Language:";
    }

    public static String statusChannel(Server guild) {
        Language language = guild.getLanguage();
        if(language == Language.DE) return "Channelverwaltung (!voice):";
        else return "Channel management (!voice):";
    }

    public static String statusCommandActivated(Server guild) {
        if(guild.getLanguage() == Language.DE) return "**Aktiviert!** Alle Commands sind aktiv und nutzbar. Du kannst dir eine Übersicht mit `!help` ausgeben lassen.";
        else return "**Activated!** All commands are active. You can print an overview with `!help`";
    }

    public static String statusVoiceActivated(Server guild) {
        if(guild.getLanguage() == Language.DE) return "**Aktiviert!** Wenn alle Voicechannel in einer Kategorie belegt sind, wird ein neuer erstellt (und danach wieder gelöscht).";
        else return "**Activated!** As soon as all voice channels in a category are occupied, a new one will be created (and deleted if it's empty).";
    }

    /*
    LANGUAGE
     */

    public static String changeLanguageAsk(Guild guild) {
        Language language = Main.getServer(guild).getLanguage();
        if(language == Language.DE) return "In welche Sprache möchtest du den Bot ändern? (Klicke auf die Reaktionen unter dieser Nachricht)";
        else return "Which language do you desire? (Click on the reaction below this message)";
    }

    public static String changeLanguage(Guild guild) {
        Language language = Main.getServer(guild).getLanguage();
        if(language == Language.DE) return "Sprache auf **DEUTSCH** geändert.";
        return "Language switched to **ENGLISH**.";
    }

    /*
    WEBHOOKS
     */

    public static String webhookSuccess(Guild guild, String mention) {
        Language language = Main.getServer(guild).getLanguage();
        if(language == Language.DE) return mention + " Erfolgreich! Der Channel erhält ab jetzt Updatebenachrichtigungen.\nZum **Deaktivieren** einfach in den " +
                "Servereinstellungen das Webhook 'Saftbot' löschen.";
        else return mention + " Success! From now on this channel will receive update notifications.\nTo **deactivate** just delete the 'Saftbot' webhook in server settings.";
    }

    /*
    BUG
     */

    public static String bugSent(Guild guild) {
        Language language = Main.getServer(guild).getLanguage();
        if(language == Language.DE) return "Fehler gesendet, ein Admin wird sich bald bei dir melden! Sollte sich innerhalb 24 Stunden niemand bei dir melden, wiederhole '!bug'";
        else return "Bugreport sent successfully! An administrator will contact you soon! If nothing happens for more than 24 hours, file your bugreport again.";
    }

    public static String adminError(Guild guild, String cmd, String perm) {
        Language language = Main.getServer(guild).getLanguage();
        if(language == Language.DE) return "Ein Member deines Servers scheiterte beim Versuch den Command **" + cmd + "** auszuführen, da dem " +
                "Bot die Berechtigung **" + perm + "** fehlt. Bitte überprüfe dies in den Rolleneinstellungen deines Servers.";
        else return "A member of your server tried to use the command **" + cmd + "** unsuccessfully. The bot is missing the **" + perm + "**. Please check that in your server settings.";
    }

    /*
    VOICE
     */

    public static String activateVoice(Guild guild) {
        Language language = Main.getServer(guild).getLanguage();
        if(language == Language.DE) return "Automatische Channels sind jetzt auf diesem Server aktiviert!";
        else return "Channel management is now activated! For more information visit `!status`";
    }

    public static String deactivateVoice(Guild guild) {
        Language language = Main.getServer(guild).getLanguage();
        if(language == Language.DE) return "Automatische Channels sind jetzt auf diesem Server deaktiviert!";
        else return "Channel management is now deactivated!";
    }

    /*
    CLEARCHAT
     */

    public static String clearchat(Guild guild) {
        Language language = Main.getServer(guild).getLanguage();
        if(language == Language.DE) return "Willst du wirklich alle Nachrichten aus dem Channel löschen?";
        else return "Do you really want to delete all messages in this channel?";
    }

    /*
    WEATHER
     */

    public static String weatherError(Guild guild) {
        Language language = Main.getServer(guild).getLanguage();
        if(language == Language.DE) return "Fehler (404): Ort nicht gefunden. Beachte bitte, dass die Städtenamen englisch sein müssen.";
        else return "Error (404): City not found. It has to be in english spelling.";
    }

    /*
    HISTORY
     */

    public static String discordJoined(Guild guild) {
        Language language = Main.getServer(guild).getLanguage();
        if(language == Language.DE) return "Discord beigetreten am: ";
        else return "Joined Discord on: ";
    }

    public static String serverJoined(Guild guild) {
        Language language = Main.getServer(guild).getLanguage();
        if(language == Language.DE) return "Diesem Server beigetreten am: ";
        else return "Joined this server on: ";
    }
}
