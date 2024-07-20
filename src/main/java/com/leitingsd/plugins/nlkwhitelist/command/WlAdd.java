package com.leitingsd.plugins.nlkwhitelist.command;

import com.leitingsd.plugins.nlkwhitelist.NLKWhitelist;
import com.leitingsd.plugins.nlkwhitelist.manager.WhitelistManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

public class WlAdd implements SimpleCommand {
    private final NLKWhitelist plugin;
    private final WhitelistManager whitelistManager;

    public WlAdd(NLKWhitelist plugin, WhitelistManager whitelistManager) {
        this.plugin = plugin;
        this.whitelistManager = whitelistManager;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length < 4) {
            source.sendMessage(Component.text(plugin.getMessage("wladd-bad-arguments")));
            return;
        }

        String player = args[0];
        String guarantor = args[1];
        String train = args[2];
        String description = args[3];
        String operator;

        if (source instanceof Player) {
            operator = ((Player) source).getUsername();
        } else {
            operator = "Console";
        }

        whitelistManager.addPlayerToWhitelist(player, operator, guarantor, train, description).thenRun(() ->
                source.sendMessage(Component.text(plugin.getMessage("wladd-success", player, operator)))
        ).exceptionally(ex -> {
            ex.printStackTrace();
            source.sendMessage(Component.text(plugin.getMessage("internal-error", ex.getMessage())));
            return null;
        });
    }
}
