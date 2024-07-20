package com.leitingsd.plugins.nlkwhitelist;

import com.google.inject.Inject;
import com.leitingsd.plugins.nlkwhitelist.command.WlAdd;
import com.leitingsd.plugins.nlkwhitelist.command.WlQuery;
import com.leitingsd.plugins.nlkwhitelist.command.WlRemove;
import com.leitingsd.plugins.nlkwhitelist.database.DatabaseManager;
import com.leitingsd.plugins.nlkwhitelist.listener.WhitelistListener;
import com.leitingsd.plugins.nlkwhitelist.manager.WhitelistManager;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Plugin(
        id = "nlkwhitelist",
        name = "NLKWhitelist",
        version = "1.1-SNAPSHOT",
        description = "A whitelist plugin for Velocity",
        authors = {"Leitingsd"}
)
public class NLKWhitelist {

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private final DatabaseManager databaseManager;
    private final WhitelistManager whitelistManager;
    private Map<String, String> messages = new HashMap<>();

    @Inject
    public NLKWhitelist(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;

        this.databaseManager = new DatabaseManager(this);
        this.whitelistManager = new WhitelistManager(this.databaseManager);

        loadConfig();
        registerCommands();
        registerListeners();
    }

    private void loadConfig() {
        try {
            if (!Files.exists(dataDirectory)) {
                Files.createDirectories(dataDirectory);
            }
            Path configPath = dataDirectory.resolve("config.yml");
            if (!Files.exists(configPath)) {
                try (InputStream in = getClass().getResourceAsStream("/config.yml")) {
                    Files.copy(in, configPath);
                }
            }
            Yaml yaml = new Yaml();
            try (InputStream input = Files.newInputStream(configPath)) {
                Map<String, Object> config = yaml.load(input);
                databaseManager.init((Map<String, Object>) config.get("database"));
                messages = (Map<String, String>) config.get("messages");
            }
        } catch (IOException e) {
            logger.error("无法加载配置文件", e);
        }
    }

    private void registerCommands() {
        server.getCommandManager().register(server.getCommandManager().metaBuilder("wladd").build(), new WlAdd(this, whitelistManager));
        server.getCommandManager().register(server.getCommandManager().metaBuilder("wlquery").build(), new WlQuery(this, whitelistManager));
        server.getCommandManager().register(server.getCommandManager().metaBuilder("wlremove").build(), new WlRemove(this, whitelistManager));
    }

    private void registerListeners() {
        EventManager eventManager = server.getEventManager();
        Optional<PluginContainer> container = server.getPluginManager().fromInstance(this);
        container.ifPresent(pluginContainer -> eventManager.register(pluginContainer, new WhitelistListener(this, whitelistManager)));
    }

    public ProxyServer getServer() {
        return server;
    }

    public Logger getLogger() {
        return logger;
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public WhitelistManager getWhitelistManager() {
        return whitelistManager;
    }

    public String getMessage(String key, Object... args) {
        String message = messages.getOrDefault(key, "未知消息键: " + key);
        return MessageFormat.format(message, args);
    }
}
