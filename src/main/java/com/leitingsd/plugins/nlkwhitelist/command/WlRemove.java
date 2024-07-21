package com.leitingsd.plugins.nlkwhitelist.command;

import com.leitingsd.plugins.nlkwhitelist.NLKWhitelist;
import com.leitingsd.plugins.nlkwhitelist.manager.WhitelistManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;

public class WlRemove implements SimpleCommand {
    private final NLKWhitelist plugin;

    public WlRemove(NLKWhitelist plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length < 2) {
            source.sendMessage(Component.text("用法: /wlremove <玩家> <原因>"));
            return;
        }

        String player = args[0];
        String reason = args[1];
        boolean success = WhitelistManager.removeWhitelist(player, reason);

        if (success) {
            source.sendMessage(Component.text("已将 " + player + " 从白名单中移除，原因: " + reason));
        } else {
            source.sendMessage(Component.text("未找到 " + player + " 的白名单"));
        }
    }
}
