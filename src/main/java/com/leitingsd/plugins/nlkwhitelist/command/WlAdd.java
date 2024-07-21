package com.leitingsd.plugins.nlkwhitelist.command;

import com.leitingsd.plugins.nlkwhitelist.NLKWhitelist;
import com.leitingsd.plugins.nlkwhitelist.manager.WhitelistManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

public class WlAdd implements SimpleCommand {
    private final NLKWhitelist plugin;

    public WlAdd(NLKWhitelist plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length < 3) {
            source.sendMessage(Component.text("用法: /wladd <玩家> <担保人> <审核批次号> [备注]"));
            return;
        }

        String player = args[0];
        String guarantor = args[1];
        String train = args[2];
        String note = args.length > 3 ? args[3] : "";

        WhitelistManager.addWhitelist(player, guarantor, train, note);

        source.sendMessage(Component.text("已将 " + player + " 添加到白名单"));

        if (source instanceof Player) {
            Player playerSource = (Player) source;
            playerSource.sendMessage(Component.text("你已将 " + player + " 添加到白名单"));
        } else {
            source.sendMessage(Component.text("控制台已将 " + player + " 添加到白名单"));
        }
    }
}
