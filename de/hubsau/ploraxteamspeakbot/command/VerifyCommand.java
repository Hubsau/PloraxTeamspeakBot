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
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

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
                            final UUID uuid = CloudAPI.getInstance().getPlayerUniqueId(name);
                            final String uid = manager.getWaitingVerify().get(name);
                            final PermissionPool pool = CloudAPI.getInstance().getPermissionPool();
                            final String group = CloudAPI.getInstance().getOnlinePlayer(uuid).getPermissionEntity().getHighestPermissionGroup(pool).getName();
                            manager.verify(uuid, uid, group);

                            System.out.println("uid "+ ts3Api);
                            final Client client = ts3Api.getClientByUId(uid).getUninterruptibly();
                            final int dbid = client.getDatabaseId();


                            System.out.println(group);
                            switch (group.toLowerCase()){

                                case "srdeveloper":  case "vip":
                                    ts3Api.addClientToServerGroup(Data.VIP_GROUP_ID, dbid);

                                    break;

                                case "jrvip":
                                   ts3Api.addClientToServerGroup(Data.JRVIP_GROUP_ID, dbid);
                                    

                                    break;

                                case "king":
                                   ts3Api.addClientToServerGroup(Data.KING_GROUP_ID, dbid);

                                    break;

                                case "rax":
                                   ts3Api.addClientToServerGroup(Data.RAX_GROUP_ID, dbid);
                                    break;


                                case "raxplus":
                                   ts3Api.addClientToServerGroup(Data.RAXP_GROUP_ID, dbid);
                                    break;

                                case  "default":
                                   ts3Api.addClientToServerGroup(Data.GAST_GROUP_ID, dbid);
                                    break;

                                    default:break;


                            }


                            player.sendMessage(Data.PREFIX+  "§7Du hast dich erfolgreich verifiziert");
                            ts3Api.sendPrivateMessage(client.getId(),
                                    "[B]Du hast dich erfolgreich mit dem Namen [color=green]"+name+" [/color]verifiziert[/B]");




                            break;
                        case "no":

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
