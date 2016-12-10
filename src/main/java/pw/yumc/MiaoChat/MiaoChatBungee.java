package pw.yumc.MiaoChat;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import pw.yumc.MiaoChat.bungee.FileConfig;

public class MiaoChatBungee extends Plugin implements Listener {
    private Map<InetSocketAddress, List<ServerInfo>> group;
    private FileConfig config;
    @EventHandler
    public void handle(final PluginMessageEvent event) {
        if (event.getTag().equals(MiaoMessage.CHANNEL) || event.getTag().equals(MiaoMessage.NORMALCHANNEL)) {
            InetSocketAddress origin = event.getSender().getAddress();
            for (ServerInfo server : getProxy().getServers().values()) {
                if (!server.getAddress().equals(origin) && server.getPlayers().size() > 0) {
                    server.sendData(event.getTag(), event.getData());
                }
            }
        }
    }

    @Override
    public void onLoad() {
        config = new FileConfig(this, "group.yml");
    }

    @Override
    public void onEnable() {
        getProxy().registerChannel(MiaoMessage.CHANNEL);
        getProxy().registerChannel(MiaoMessage.NORMALCHANNEL);
        getProxy().getPluginManager().registerListener(this, this);
        getLogger().info("注意: 通过BC转发的聊天信息将不会在控制台显示 仅客户端可见!");
    }
}
