package com.leitingsd.plugins.nlkwhitelist;

import com.leitingsd.plugins.nlkwhitelist.command.WlAdd;
import com.leitingsd.plugins.nlkwhitelist.command.WlQuery;
import com.leitingsd.plugins.nlkwhitelist.command.WlRemove;
import com.leitingsd.plugins.nlkwhitelist.database.DatabaseManager;
import com.leitingsd.plugins.nlkwhitelist.listener.WhitelistListener;
import com.leitingsd.plugins.nlkwhitelist.manager.WhitelistManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.simpleyaml.configuration.file.FileConfiguration;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

@Plugin(id = "nlkwhitelist", name = "NLKWhitelist", version = "1.0", description = "A whitelist plugin for Velocity")
public class NLKWhitelist {
    private final ProxyServer server;
    private final Logger logger = LoggerFactory.getLogger(NLKWhitelist.class);
    private DatabaseManager databaseManager;
    private FileConfiguration config;

    @Inject
    public NLKWhitelist(ProxyServer server) {
        this.server = server;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        try {
            loadConfig();
            this.databaseManager = new DatabaseManager(config);
            new WhitelistManager(databaseManager);
            server.getEventManager().register(this, new WhitelistListener(this));
            registerCommands();
        } catch (IOException e) {
            logger.error("Error loading configuration", e);
        }
    }

    private void registerCommands() {
        CommandManager commandManager = server.getCommandManager();
        commandManager.register("wladd", new WlAdd(this));
        commandManager.register("wlquery", new WlQuery(this));
        commandManager.register("wlremove", new WlRemove(this));
    }

    private void loadConfig() throws IOException {
        File configFile = new File("plugins/NLKWhitelist/config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();
            logger.info("Created default configuration file at plugins/NLKWhitelist/config.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        // 检查配置文件中的字段是否正确填充
        if (config.getString("database.address") == null || config.getString("database.address").isEmpty()) {
            config.set("database.address", "localhost");
        }
        if (config.getString("database.port") == null || config.getString("database.port").isEmpty()) {
            config.set("database.port", 3306);
        }
        if (config.getString("database.name") == null || config.getString("database.name").isEmpty()) {
            config.set("database.name", "whitelist");
        }
        if (config.getString("database.user") == null || config.getString("database.user").isEmpty()) {
            config.set("database.user", "username");
        }
        if (config.getString("database.password") == null || config.getString("database.password").isEmpty()) {
            config.set("database.password", "password");
        }
        config.save(configFile);
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
