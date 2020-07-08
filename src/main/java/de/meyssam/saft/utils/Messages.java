package de.meyssam.saft.utils;

/**
 * Created by Meyssam Saghiri on Mai 25, 2020
 */
public class Messages {

    public static String welcome = ":kissing_smiling_eyes: Hi, dieser Bot läuft derzeit noch auf meinem privaten Rechner, weswegen er nicht 24 Stunden gestartet sein kann." +
            "\nHilfe gibts mit '!help' und Bugreports und Verbesserungsvorschläge an mich mit '!bug <Nachricht>'" +
            "\n:warning: Achtung: Der Voicealgorithmus ist nur für Channel gedacht, die in eine Kategorie eingeordnet sind.";

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
    public static String changelog113 = ":loudspeaker: Changelog Version 1.13-BETA:" +
            "\n:new: Mit '!mastermind' Kannst du mit den Bot Mastermind spielen" +
            "\nFIX: Helpmessage verbessert'" +
            "\nFIX: Changelogs werden nicht mehr standardmäßig an alle Server gesendet (nur noch über Webhooks mit '!updates')";

    public static String help = "Willkommen bei Meyssams saftigem Bot! :warning: Folgende Commands gibt es:\n" +
            "```!bug [<Nachricht>] = Sendet einen Bugreport an einen Botadministrator, welcher sich um das Problem kümmern wird```" +
            "```!case <Nachricht> = Sendet in deinem Namen eine interessante Nachricht in den Chat*```" +
            "```!changelog = Sendet die neusten Änderungen des Bots in den Channel```" +
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
            "```!wetter <Stadt> = Zeigt das Wetter der Stadt an (Stadt muss englisch sein: Düsseldorf = Dusseldorf; Köln = Cologne)*```" +
            "Einige Commands benötigen die passende Berechtigung.\n" +
            ":warning: Die Channelverwaltung ist nur für Channel gedacht, die in eine Kategorie eingeordnet sind. Für mehr Infos `!status`\n" +
            "Legende: [] = optional";

}
