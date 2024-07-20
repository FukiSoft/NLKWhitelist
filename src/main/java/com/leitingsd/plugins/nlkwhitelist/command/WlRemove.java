package com.leitingsd.plugins.nlkwhitelist.command;

import com.leitingsd.plugins.nlkwhitelist.NLKWhitelist;
import com.leitingsd.plugins.nlkwhitelist.manager.WhitelistManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

public class WlRemove implements SimpleCommand {
    private final NLKWhitelist plugin;
    private final WhitelistManager whitelistManager;

    public WlRemove(NLKWhitelist plugin, WhitelistManager whitelistManager) {
        this.plugin = plugin;
        this.whitelistManager = whitelistManager;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length < 2) {
            source.sendMessage(Component.text(plugin.getMessage("wlremove-bad-arguments")));
            return;
        }

        String player = args[0];
        String reason = args[1];
        String operator;

        if (source instanceof Player) {
            operator = ((Player) source).getUsername();
        } else {
            operator = "Console";
        }

        whitelistManager.removePlayerFromWhitelist(player, reason, operator).thenRun(() ->
                source.sendMessage(Component.text(plugin.getMessage("wlremove-success", player, operator)))
        ).exceptionally(ex -> {
            ex.printStackTrace();
            source.sendMessage(Component.text(plugin.getMessage("internal-error", ex.getMessage())));
            return null;
        });
    }
}
