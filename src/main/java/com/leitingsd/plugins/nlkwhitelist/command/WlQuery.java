package com.leitingsd.plugins.nlkwhitelist.command;

import com.leitingsd.plugins.nlkwhitelist.NLKWhitelist;
import com.leitingsd.plugins.nlkwhitelist.manager.WhitelistManager;
import com.leitingsd.plugins.nlkwhitelist.manager.WhitelistRecord;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;

public class WlQuery implements SimpleCommand {
    private final NLKWhitelist plugin;

    public WlQuery(NLKWhitelist plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length < 1) {
            source.sendMessage(Component.text("用法: /wlquery <玩家>"));
            return;
        }

        String player = args[0];
        WhitelistRecord result = WhitelistManager.queryWhitelist(player);

        if (result != null) {
            source.sendMessage(Component.text("白名单条目: 玩家=" + player + ", 担保人=" + result.getGuarantor() + ", 审核批次号=" + result.getTrain() + ", 备注=" + result.getDescription()));
        } else {
            source.sendMessage(Component.text("未找到 " + player + " 的白名单"));
        }
    }
}
