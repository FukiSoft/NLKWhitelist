package com.leitingsd.plugins.nlkwhitelist.listener;

import com.leitingsd.plugins.nlkwhitelist.NLKWhitelist;
import com.leitingsd.plugins.nlkwhitelist.manager.WhitelistManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

public class WhitelistListener {
    private final NLKWhitelist plugin;

    public WhitelistListener(NLKWhitelist plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onPlayerLogin(LoginEvent event) {
        Player player = event.getPlayer();
        String playerId = player.getUsername();
        if (!WhitelistManager.isPlayerWhitelisted(playerId)) {
            event.setResult(LoginEvent.ComponentResult.denied(Component.text("You are not whitelisted on this server.")));
        }
    }
}
