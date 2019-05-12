package de.hubsau.ploraxteamspeakbot.verify;
/*Class erstellt von Hubsau


20:54 2019 07.05.2019
Wochentag : Dienstag


*/


import de.hubsau.ploraxteamspeakbot.bungee.PloraxBot;
import de.hubsau.ploraxteamspeakbot.database.VerifyData;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class VerifyManager {

    public HashMap<String, String> waitingVerify;
    private VerifyData verifyData;

    public VerifyManager(PloraxBot plugin) {
        waitingVerify = new HashMap<>();
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


    public void addToWaitingVerify(String ingameName, String invokerUniqueId){

        waitingVerify.put(ingameName, invokerUniqueId);
    }

    public boolean isWaitingVerify(String ingameName){

        return getWaitingVerify().containsKey(ingameName);
    }
    public HashMap<String, String> getWaitingVerify() {
        return waitingVerify;
    }
}
