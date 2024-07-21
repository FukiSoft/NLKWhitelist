package com.leitingsd.plugins.nlkwhitelist.listener;

import com.leitingsd.plugins.nlkwhitelist.manager.WhitelistManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

public class WhitelistListener {

    private final WhitelistManager whitelistManager;
    private final Logger logger;

    public WhitelistListener(WhitelistManager whitelistManager, Logger logger) {
        this.whitelistManager = whitelistManager;
        this.logger = logger;
    }

    @Subscribe
    public void onPlayerLogin(LoginEvent event) {
        Player player = event.getPlayer();
        whitelistManager.isPlayerWhitelisted(player.getUsername()).thenAccept(isWhitelisted -> {
            if (!isWhitelisted) {
                logger.info("Player " + player.getUsername() + " was denied access (not whitelisted).");
                player.disconnect(Component.text("你不在白名单中，无法加入服务器。"));
            } else {
                logger.info("Player " + player.getUsername() + " was allowed access (whitelisted).");
                event.setResult(LoginEvent.ComponentResult.allowed());
            }
        }).exceptionally(throwable -> {
            logger.error("Error checking whitelist for player " + player.getUsername(), throwable);
            player.disconnect(Component.text("发生了一个内部错误。"));
            return null;
        });
    }
}
