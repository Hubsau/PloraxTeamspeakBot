package de.hubsau.ploraxteamspeakbot.listener;
/*Class erstellt von Hubsau


20:52 2019 07.05.2019
Wochentag : Dienstag


*/


import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import de.dytanic.cloudnet.lib.player.OfflinePlayer;
import de.dytanic.cloudnet.lib.player.permission.PermissionPool;
import de.hubsau.ploraxteamspeakbot.bungee.PloraxBot;
import de.hubsau.ploraxteamspeakbot.database.VerifyData;
import de.hubsau.ploraxteamspeakbot.util.Data;
import de.hubsau.ploraxteamspeakbot.verify.VerifyManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.UUID;


public class EventListener implements TS3Listener {

    final private PloraxBot plugin;
    final SupportReason supportReason;
    final TS3ApiAsync ts3Api;


    public EventListener(PloraxBot plugin) {
        this.plugin = plugin;
        supportReason = new SupportReason();
        ts3Api = plugin.getTs3ApiAsync();


    }




    @Override
    public void onTextMessage(TextMessageEvent event) {




        String message = event.getMessage();
        Client invoker = ts3Api.getClientByUId(event.getInvokerUniqueId()).getUninterruptibly();

        final VerifyManager manager = plugin.getManager();
        if(manager.getAcceptVerify().containsKey(event.getInvokerUniqueId())){

            if(event.getMessage().equalsIgnoreCase("!ja")){


                final UUID uuid = UUID.fromString(manager.getAcceptVerify().get(event.getInvokerUniqueId()));
                CloudPlayer player = CloudAPI.getInstance().getOnlinePlayer(uuid);

                final String uid = event.getInvokerUniqueId();
                final PermissionPool pool = CloudAPI.getInstance().getPermissionPool();
                final String group = player.getPermissionEntity().getHighestPermissionGroup(pool).getName();
                manager.verify(uuid, uid, group);

                final Client client = ts3Api.getClientByUId(uid).getUninterruptibly();
                final int dbid = client.getDatabaseId();

                manager.setRang(group, dbid, uid );

                if(BungeeCord.getInstance().getPlayer(uuid) != null) {
                    ProxiedPlayer player1 = BungeeCord.getInstance().getPlayer(uuid);
                    player1.sendMessage(Data.PREFIX + "§7Du hast dich erfolgreich verifiziert");
                    ts3Api.sendPrivateMessage(client.getId(),
                            "[B]Du hast dich erfolgreich mit dem Namen [color=green]" + player.getName() + " [/color]verifiziert[/B]");

                    try {
                        URL url = new URL("https://visage.surgeplay.com/face/128/" + uuid.toString());
                        System.out.println(url);
                        InputStream in = new BufferedInputStream(url.openStream());

                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        byte[] buf = new byte[128];
                        int n = 0;
                        while (-1 != (n = in.read(buf))) {
                            out.write(buf, 0, n);
                        }
                        out.close();
                        in.close();
                        byte[] response = out.toByteArray();
                        long value = ts3Api.uploadIconDirect(response).getUninterruptibly();
                        ts3Api.addClientPermission(dbid, "i_icon_id", (int) value, false);
                        manager.getAcceptVerify().remove(uid);
                        manager.getWaitingVerify().remove(uid);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }else if(event.getMessage().equalsIgnoreCase("!nein"))
                ts3Api.sendPrivateMessage(event.getInvokerId(), "[B]Verifizierung abgebrochen[/B]");
        }


        switch (message.toLowerCase()){

            case "1": case "2": case "3": case "4": case "5":



                if(invoker.getChannelId() != Data.SUPPORTCHANNELID)
                    return;

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if(plugin.getManager().isVerifiedbyUID(event.getInvokerUniqueId())){

                            int channel;
                            channel = ts3Api.createChannel(event.getInvokerId()+"", Collections.singletonMap(ChannelProperty.CHANNEL_ORDER, Data.SUPPORTCHANNELID.toString())).getUninterruptibly();

                            ts3Api.moveClient(event.getInvokerId(), channel);
                            OfflinePlayer offlinePlayer = CloudAPI.getInstance().getOfflinePlayer(plugin.getVerifyData().getIngameUUID(event.getInvokerUniqueId()));
                            ts3Api.editChannel(channel, ChannelProperty.CHANNEL_NAME,
                                    offlinePlayer.getName()+" ("+ message+ ")");
                            ts3Api.moveQuery(Data.JOINCHANNELID);
                            ts3Api.editChannel(channel, ChannelProperty.CHANNEL_DESCRIPTION, "[center]\n" +
                                    "\n" +
                                    "[img]https://visage.surgeplay.com/bust/128/"+offlinePlayer.getUniqueId().toString().replaceAll("-", "")+"\n" +
                                    "[/img]\n" +
                                    "\n" +
                                    "[SIZE=16][COLOR=#55ff00]"+offlinePlayer.getPermissionEntity().getHighestPermissionGroup(CloudAPI.getInstance().getPermissionPool()).getName()+"[/COLOR] [COLOR=GRAY]\n" +
                                    "\n" +
                                    "[SIZE=13][COLOR=GRAY]Benötigt Support im Bereich[/COLOR]\n" +
                                    "[COLOR=#55ff00]"+supportReason.getReasons().get(message)+"[/COLOR]\n");

                            ts3Api.addChannelPermission(channel, "i_channel_needed_join_power", Data.SUPPORT_CHANNEL_JOIN_POWER).getUninterruptibly();
                            ts3Api.addChannelPermission(channel, "i_channel_needed_subscribe_power", Data.SUPPORT_CHANNEL_SUBSCRIBE_POWER).getUninterruptibly();
                            ts3Api.addChannelPermission(channel, "i_channel_needed_modify_power", Data.SUPPORT_CHANNEL_MODIFY_POWER).getUninterruptibly();
                            ts3Api.addChannelPermission(channel, "i_channel_needed_delete_power", Data.SUPPORT_CHANNEL_DELETE_POWER).getUninterruptibly();
                            ts3Api.addChannelPermission(channel, "i_ft_needed_file_rename_power", Data.SUPPORT_CHANNEL_RENAME_POWER).getUninterruptibly();


                            ts3Api.getClients().getUninterruptibly().forEach(client -> {



                                if(client.isInServerGroup(Data.SUPPORT_POKE_GROUP_ID)){
                                    ts3Api.pokeClient(client.getId(), "[B]Der Spieler [color=green]"+event.getInvokerName()+"[/color] wartet im Support[/B]").getUninterruptibly();
                                }

                                if(client.isInServerGroup(Data.SUPPORT_CHAT_GROUP_ID)){
                                    ts3Api.sendPrivateMessage(client.getId(), "[B]Der Spieler [color=green]"+event.getInvokerName()+"[/color] wartet im Support[/B]").getUninterruptibly();

                                }
                            });
                        }else
                            ts3Api.sendPrivateMessage(event.getInvokerId(), "Bitte verifiziere dich zuerst! (schreib mir !verify)");


                    }
                });
                thread.start();

                break;

            case "!verify":

                if(plugin.getManager().isVerifiedbyUID(event.getInvokerUniqueId())){
                    ts3Api.sendPrivateMessage(event.getInvokerId(), "Du hast dich bereits verifiziert! Melde dich bei Problemen im Support").getUninterruptibly();
                    return;
                }else sendVerifyReqest(invoker.getIp(), event.getInvokerId(), event.getInvokerName(), event.getInvokerUniqueId());
                break;


                default:break;
        }
            return;

        }





        private void sendVerifyReqest(String ipadress, int invokerID, String invokerName, String uid){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean[] online = new boolean[1];
                online[0] = false;
                BungeeCord.getInstance().getPlayers().forEach(player -> {
                    if (player.getAddress().getAddress().toString().endsWith(ipadress)) {


                        ts3Api.sendPrivateMessage(invokerID, "Bitte bestätige deine Anfrage Ingame").getUninterruptibly();

                        TextComponent msg = new TextComponent(Data.PREFIX + "" +
                                "Möchtest du in Teamspeak §8(§e"+invokerName+"§8) §7verifizieren? ");

                        TextComponent yes = new TextComponent("§a§l§nJa");
                        yes.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/verify yes"));
                        yes.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Klicke um dich im §6§lTeam§e§lSpeak §7zu verifizieren").create()));


                        TextComponent no = new TextComponent("§r §c§l§nNein");
                        no.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/verify no"));
                        no.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Klicke um dich §c§l§nnicht§r§7 zu verifizieren").create()));


                        yes.addExtra(no);
                        msg.addExtra(yes);

                        player.sendMessage(msg);
                        online[0] = true;
                        plugin.getManager().addToWaitingVerify( player.getName(),uid);
                        return;


                    }


                });

                if(!online[0])
                    ts3Api.sendPrivateMessage(invokerID, "Du konntest dicht auf dem Server gefunden werden.").getUninterruptibly();

            }
        });
        thread.start();



        }
    @Override
    public void onClientJoin(ClientJoinEvent event) {


        String uid = ts3Api.getClientInfo(event.getClientId()).getUninterruptibly().getUniqueIdentifier();

        final VerifyManager manager = plugin.getManager();
        if(!manager.isVerifiedbyUID(uid)){
            ts3Api.sendPrivateMessage(event.getClientId(), "Nutze !verify um dich mit deinen Ingame-Rang zu verifizieren.");

        }else{



            UUID uuid = plugin.getVerifyData().getIngameUUID(uid);
            final String group = CloudAPI.getInstance().getOfflinePlayer(uuid).getPermissionEntity().
                    getHighestPermissionGroup(CloudAPI.getInstance().getPermissionPool())
                    .getName();
            String dbGroup = plugin.getVerifyData().getRang(uid);


            if(group.equals(dbGroup)) return;


            final Client client = ts3Api.getClientByUId(uid).getUninterruptibly();
            final int dbid = client.getDatabaseId();

            ts3Api.removeClientFromServerGroup(plugin.getVerifyData().getGroupID(uid), dbid);
            manager.setRang(group, dbid, uid);
        }




    }


    @Override
    public void onClientMoved(ClientMovedEvent event) {


                if(event.getTargetChannelId() == Data.SUPPORTCHANNELID){

                    final VerifyManager manager = plugin.getManager();

                    if (manager.isVerifiedbyUID(ts3Api.getClientInfo(event.getClientId()).getUninterruptibly().getUniqueIdentifier())) {
                        ts3Api.sendPrivateMessage(event.getClientId(), "[B][COLOR=#0984d1]Willkommen[/COLOR] im Support[/B]\n" +
                                "[B][/B]\n[B][COLOR=#00aa00]1[/COLOR][/B] Allgemeine Frage\n" +
                                "[B][COLOR=#00aa00]2[/COLOR][/B] Bewerbungen\n" +
                                "[B][COLOR=#00aa00]3[/COLOR][/B] Report / Entbannung\n" +
                                "[B][COLOR=#00aa00]4[/COLOR][/B] Bug melden\n" +
                                "[B][COLOR=#00aa00]5[/COLOR][/B] Beschwerde / Administrationsgespräch").getUninterruptibly();


                    }else ts3Api.sendPrivateMessage(event.getClientId(), "Bitte verifiziere dich zuerst! (schreib mir !verify)").getUninterruptibly();


                }



    }

    @Override
    public void onChannelCreate(ChannelCreateEvent channelCreateEvent) {

    }

    @Override
    public void onChannelDeleted(ChannelDeletedEvent channelDeletedEvent) {

    }

    @Override
    public void onChannelMoved(ChannelMovedEvent channelMovedEvent) {

    }

    @Override
    public void onChannelPasswordChanged(ChannelPasswordChangedEvent channelPasswordChangedEvent) {

    }

    @Override
    public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent privilegeKeyUsedEvent) {

    }

    @Override
    public void onClientLeave(ClientLeaveEvent clientLeaveEvent) {

    }

    @Override
    public void onServerEdit(ServerEditedEvent serverEditedEvent) {

    }

    @Override
    public void onChannelEdit(ChannelEditedEvent channelEditedEvent) {

    }

    @Override
    public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent channelDescriptionEditedEvent) {

    }

}
