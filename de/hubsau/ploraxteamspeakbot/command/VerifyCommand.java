package de.hubsau.ploraxteamspeakbot.command;
/*Class erstellt von Hubsau


21:41 2019 10.05.2019
Wochentag : Freitag


*/


import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.permission.PermissionPool;
import de.hubsau.ploraxteamspeakbot.bungee.PloraxBot;
import de.hubsau.ploraxteamspeakbot.util.Data;
import de.hubsau.ploraxteamspeakbot.verify.VerifyManager;
import jdk.internal.util.xml.impl.Input;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class VerifyCommand extends Command {


    private PloraxBot ploraxBot;
    final TS3ApiAsync ts3Api;

    public VerifyCommand(PloraxBot ploraxBot) {
        super("verify");
        this.ploraxBot = ploraxBot;
        ts3Api = ploraxBot.getTs3ApiAsync();

    }

    @Override
    public void execute(CommandSender sender, String[] args){


        if(sender instanceof ProxiedPlayer) {
            final ProxiedPlayer player = (ProxiedPlayer)sender;
            final String name = player.getName();
            final VerifyManager manager = ploraxBot.getManager();
            if (manager.isWaitingVerify(name)){

                if(args.length == 1){

                    String option = args[0];

                    switch (option){

                        case "yes":

                            if(manager.isWaitingVerify(name)) {
                                Client client = ts3Api.getClientByUId(manager.getWaitingVerify().get(name))
                                        .getUninterruptibly();
                                ts3Api.sendPrivateMessage(client.getId(), "[B]Möchtest du wirklich mit dem Namen [color=green]" + name + "[/color] verifizieren?[/B] (Nutze !ja oder !nein)");

                                player.sendMessage(Data.PREFIX+ "§7Bitte bestätige deine Anfrage erneut in Teamspeak");
                                manager.getAcceptVerify().put(client.getUniqueIdentifier(), CloudAPI.getInstance().getPlayerUniqueId(name).toString());

                            }else player.sendMessage(Data.PREFIX+"§cUngültige Angabe");
                            break;
                        case "no":
                            if(manager.isWaitingVerify(name)) {

                                player.sendMessage(Data.PREFIX + "§7Verifizierung abgebrochen");
                            }else player.sendMessage(Data.PREFIX+"§cUngültige Angabe");
                            break;



                        default:
                            player.sendMessage(Data.PREFIX+ "§7Verifiziere dich in Teamspeak, indem du dem Bot mit !verify anschreibst");
                            break;
                    }


                }else

                    player.sendMessage(Data.PREFIX+ "§7Verifiziere dich in Teamspeak, indem du dem Bot mit !verify anschreibst");

            }else player.sendMessage(Data.PREFIX+ "§7Verifiziere dich in Teamspeak, indem du dem Bot mit !verify anschreibst");


        }
    }
}
