package de.hubsau.ploraxteamspeakbot.verify;
/*Class erstellt von Hubsau


20:54 2019 07.05.2019
Wochentag : Dienstag


*/


import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import de.hubsau.ploraxteamspeakbot.bungee.PloraxBot;
import de.hubsau.ploraxteamspeakbot.database.VerifyData;
import de.hubsau.ploraxteamspeakbot.util.Data;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class VerifyManager {

    public HashMap<String, String> waitingVerify;
    public HashMap<String, String> acceptVerify;
    private VerifyData verifyData;
    private PloraxBot ploraxBot;

    public VerifyManager(PloraxBot plugin) {
        waitingVerify = new HashMap<>();
        acceptVerify = new HashMap<>();

        ploraxBot  = plugin;
        verifyData = plugin.getVerifyData();

    }

    public boolean isVerifiedbyUUID(String uuid){
        return verifyData.isVerifiedByUUID(uuid);
    }
    public boolean isVerifiedbyUID(String uid){
        return verifyData.isVerifiedbyUID(uid);
    }

    public void verify(UUID uuid, String uid, String rang){

        verifyData.verify(uid, uuid, rang);
    }

    public void setRang(String group, int dbid, String uid){


        TS3ApiAsync ts3APi = ploraxBot.getTs3ApiAsync();
        VerifyData verifyData = ploraxBot.getVerifyData();


        switch (group.toLowerCase()){


            case "srdeveloper":  case "vip":
                ts3APi.addClientToServerGroup(Data.VIP_GROUP_ID, dbid);
                verifyData.setRang(uid, Data.VIP_GROUP_ID, group);


                break;

            case "jrvip":
                ts3APi.addClientToServerGroup(Data.JRVIP_GROUP_ID, dbid);
                verifyData.setRang(uid, Data.JRVIP_GROUP_ID, group);

                break;

            case "king":
                ts3APi.addClientToServerGroup(Data.KING_GROUP_ID, dbid);
                verifyData.setRang(uid, Data.KING_GROUP_ID, group);

                break;

            case "rax":
                ts3APi.addClientToServerGroup(Data.RAX_GROUP_ID, dbid);
                verifyData.setRang(uid, Data.RAX_GROUP_ID, group );

                break;


            case "raxplus":
                ts3APi.addClientToServerGroup(Data.RAXP_GROUP_ID, dbid);
                verifyData.setRang(uid, Data.RAXP_GROUP_ID, group);

                break;

            case  "default":
                ts3APi.addClientToServerGroup(Data.GAST_GROUP_ID, dbid);
                verifyData.setRang(uid, Data.GAST_GROUP_ID, group);

                break;

            default:break;


        }

    }


    public void addToWaitingVerify(String ingameName, String invokerUniqueId){

        waitingVerify.put(ingameName, invokerUniqueId);
    }

    public HashMap<String, String> getAcceptVerify() {
        return acceptVerify;
    }

    public boolean isWaitingVerify(String ingameName){

        return getWaitingVerify().containsKey(ingameName);
    }
    public HashMap<String, String> getWaitingVerify() {
        return waitingVerify;
    }
}
