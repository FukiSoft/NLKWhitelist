package com.leitingsd.plugins.nlkwhitelist.listener;

import com.leitingsd.plugins.nlkwhitelist.NLKWhitelist;
import com.leitingsd.plugins.nlkwhitelist.manager.WhitelistManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

public class WhitelistListener {

    private final NLKWhitelist plugin;
    private final WhitelistManager whitelistManager;

    public WhitelistListener(NLKWhitelist plugin, WhitelistManager whitelistManager) {
        this.plugin = plugin;
        this.whitelistManager = whitelistManager;
    }

    @Subscribe
    public void onPlayerLogin(LoginEvent event) {
        Player player = event.getPlayer();
        if (!whitelistManager.isPlayerWhitelisted(player.getUsername())) {
            player.disconnect(Component.text("你不在白名单中，无法加入服务器。"));
        }
    }
}
