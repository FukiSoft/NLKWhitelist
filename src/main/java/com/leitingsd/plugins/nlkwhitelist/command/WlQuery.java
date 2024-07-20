package com.leitingsd.plugins.nlkwhitelist.command;

import com.leitingsd.plugins.nlkwhitelist.NLKWhitelist;
import com.leitingsd.plugins.nlkwhitelist.manager.WhitelistManager;
import com.leitingsd.plugins.nlkwhitelist.manager.WhitelistRecord;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;

public class WlQuery implements SimpleCommand {
    private final NLKWhitelist plugin;
    private final WhitelistManager whitelistManager;

    public WlQuery(NLKWhitelist plugin, WhitelistManager whitelistManager) {
        this.plugin = plugin;
        this.whitelistManager = whitelistManager;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length < 1) {
            source.sendMessage(Component.text(plugin.getMessage("wlquery-bad-arguments")));
            return;
        }

        String player = args[0];
        whitelistManager.queryWhitelist(player).thenAccept(record -> {
            if (record != null) {
                source.sendMessage(Component.text(plugin.getMessage("wlquery-entry", record.getId(), record.getTime(), record.getPlayer(), record.getGuarantor(), record.getOperator(), record.getTrain(), record.getDescription(), record.getDeleteAt(), record.getDeleteOperator(), record.getDeleteReason())));
            } else {
                source.sendMessage(Component.text(plugin.getMessage("wlquery-no-data")));
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            source.sendMessage(Component.text(plugin.getMessage("internal-error", ex.getMessage())));
            return null;
        });
    }
}
