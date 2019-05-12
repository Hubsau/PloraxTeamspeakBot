package de.hubsau.ploraxteamspeakbot.database;
/*Class erstellt von Hubsau


21:30 2019 08.05.2019
Wochentag : Mittwoch


*/


import de.hubsau.ploraxteamspeakbot.bungee.PloraxBot;
import org.bson.Document;

import java.util.UUID;

public class VerifyData {
    private MongoManager mongoManager;
    private PloraxBot plugin;

    public VerifyData(PloraxBot plugin) {
        this.plugin = plugin;
        mongoManager = plugin.getMongoManager();
    }

    public boolean isVerifiedbyUID(String uid){


        Document playerData = new Document("uid", uid);
        return mongoManager.getVerifyData().find(playerData).first() != null;
    }

    public boolean isVerifiedByUUID(String uuid){


        Document playerData = new Document("uuid", uuid);
        return mongoManager.getVerifyData().find(playerData).first() != null;
    }



    public void verify(String uid, UUID ingame, String rang){

        Document document = new Document();
        document.append("uid", uid);
        document.append("uuid", ingame.toString());
        document.append("rang", rang);
        mongoManager.getVerifyData().insertOne(document);
    }

    public UUID getIngameUUID(String uid){

        System.out.println("uid "+uid);
        Document document = mongoManager.getVerifyData().find(new Document("uid", uid)).first();
        return UUID.fromString(document.getString("uuid"));
    }

    public String getRang(String uid){

        Document document = mongoManager.getVerifyData().find(new Document("uid", uid)).first();
        return document.getString("rang");
    }
    
    public void updateRang(String uuid, String currentRang){
        Document document = mongoManager.getVerifyData().find(new Document("uuid", uuid)).first();

        mongoManager.getVerifyData().updateOne(document, new Document("$set", new Document("rang", currentRang)));

    }

    public void removeVerifyByUUID(String uuid){

        Document document = mongoManager.getVerifyData().find(new Document("uuid", uuid)).first();
        mongoManager.getVerifyData().deleteOne(document);

    }

    public void removeVerifyByUID(String uid){

        Document document = mongoManager.getVerifyData().find(new Document("uid", uid)).first();
        mongoManager.getVerifyData().deleteOne(document);

    }






}
