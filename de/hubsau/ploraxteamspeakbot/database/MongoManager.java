package de.hubsau.ploraxteamspeakbot.database;
/*Class erstellt von Hubsau


21:27 2019 07.05.2019
Wochentag : Dienstag


*/


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.hubsau.ploraxteamspeakbot.bungee.PloraxBot;
import net.md_5.bungee.api.plugin.Plugin;
import org.bson.Document;

public class MongoManager {
    private Plugin plugin;
    private MongoClient mongoClient;

    private MongoDatabase database;
    private MongoCollection<Document> verifyData;


    public MongoManager(PloraxBot plugin) {
      this.plugin = plugin;
        connectToDatabase();
    }
    public void connectToDatabase(){


        MongoClient client = new MongoClient("localhost", 27017);

        database = client.getDatabase("plorax");
        verifyData = database.getCollection("verifyData");
    }


    public MongoCollection<Document> getVerifyData() {
        return verifyData;
    }
}
