package de.hubsau.ploraxteamspeakbot.bungee;
/*Class erstellt von Hubsau


21:19 2019 07.05.2019
Wochentag : Dienstag


*/


import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import de.hubsau.ploraxteamspeakbot.command.VerifyCommand;
import de.hubsau.ploraxteamspeakbot.database.MongoManager;
import de.hubsau.ploraxteamspeakbot.database.VerifyData;
import de.hubsau.ploraxteamspeakbot.listener.EventListener;
import de.hubsau.ploraxteamspeakbot.util.Data;
import de.hubsau.ploraxteamspeakbot.verify.VerifyManager;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Scanner;
import java.util.logging.Level;

public class PloraxBot extends Plugin {

    private static PloraxBot instance;
    private TS3ApiAsync ts3ApiAsync;
    private VerifyManager manager;
    private MongoManager mongoManager;
    private VerifyData verifyData;

    @Override
    public void onEnable() {
        initBot();
        mongoManager = new MongoManager(this);
        mongoManager.connectToDatabase();
        verifyData = new VerifyData(this);
        manager = new VerifyManager(this);

        getProxy().getPluginManager().registerCommand(this, new VerifyCommand(this));
    }

    private void initBot(){

      getLogger().log(Level.INFO, "Starte Teamspeak³ Bot..");

        System.out.println("\n" +
                "_____  _                      _   _ ______ _______ \n" +
                "|  __ \\| |                    | \\ | |  ____|__   __|\n" +
                "| |__) | | ___  _ __ __ ___  _|  \\| | |__     | |   \n" +
                "|  ___/| |/ _ \\| '__/ _` \\ \\/ / . ` |  __|    | |   \n" +
                "| |    | | (_) | | | (_| |>  <| |\\  | |____   | |   \n" +
                "|_|    |_|\\___/|_|  \\__,_/_/\\_\\_| \\_|______|  |_|   \n" +
                "Plorax Teamspeak Bot by Hubsau");

        final TS3Config config = new TS3Config();
        config.setHost("localhost");

        //config.setEnableCommunicationsLogging(true);
        final TS3Query query = new TS3Query(config);
        query.connect();


        ts3ApiAsync = query.getAsyncApi();
        ts3ApiAsync.login(Data.USERNAME, Data.PASSWORD);
        ts3ApiAsync.selectVirtualServerById(1);
        ts3ApiAsync.setNickname(Data.NICKNAME);
        ts3ApiAsync.registerAllEvents();
        ts3ApiAsync.addTS3Listeners(new EventListener(this));






        /*

        while (true){

            typeIn = scanner.nextLine();
            command = typeIn.split(" ")[0];
            args = dropFirstString(typeIn.split(" "));

            if(command.equalsIgnoreCase("ts3stop")){
                getLogger().log(Level.INFO, "Teamspeak³ Bot wird heruntergefahren");

                System.out.println("\n" +
                        "_____  _                      _   _ ______ _______ \n" +
                        "|  __ \\| |                    | \\ | |  ____|__   __|\n" +
                        "| |__) | | ___  _ __ __ ___  _|  \\| | |__     | |   \n" +
                        "|  ___/| |/ _ \\| '__/ _` \\ \\/ / . ` |  __|    | |   \n" +
                        "| |    | | (_) | | | (_| |>  <| |\\  | |____   | |   \n" +
                        "|_|    |_|\\___/|_|  \\__,_/_/\\_\\_| \\_|______|  |_|   \n" +
                        "Plorax Teamspeak Bot by Hubsau");
                System.exit(1);

            }
        }
         */


    }

    public TS3ApiAsync getTs3ApiAsync() {
        return ts3ApiAsync;
    }

    public static PloraxBot getInstance() {
        return instance;
    }


    public VerifyManager getManager() {
        return manager;
    }

    public MongoManager getMongoManager() {
        return mongoManager;
    }

    public VerifyData getVerifyData() {
        return verifyData;
    }






}
