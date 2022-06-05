package com.mattmx.vtools;

import com.mattmx.vtools.commands.*;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.mattmx.vtools.util.Config;
import com.mattmx.vtools.util.DependencyChecker;
import net.kyori.adventure.text.format.TextColor;
import net.luckperms.api.LuckPermsProvider;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.FileConfiguration;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

@Plugin(id = "vtools", name="VTools+", version="1.0", description="Helpful velocity commands")
public class VTools {
    static VTools instance;

    private final Logger logger;
    private final ProxyServer server;
    private final File dataFolder;
    private boolean protocolize;

    public static final TextColor COLOR_RED = TextColor.fromCSSHexString("FF5555");
    public static final TextColor COLOR_YELLOW = TextColor.fromCSSHexString("FFFF55");

    @Inject
    public VTools(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
        instance = this;
        dataFolder = getDataFolder();
    }


    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        if (DependencyChecker.luckperms()) {
            logger.info("LuckPerms installed! " + LuckPermsProvider.get().getPluginMetadata().getVersion());
        } else {
            logger.warn("Missing LuckPerms, some placeholders will not be available.");
        }
        if (DependencyChecker.protocolize()) {
            logger.info("Protocolize installed!");
            protocolize = true;
        } else {
            logger.warn("Protocolize not installed. Please install for GUI support.");
            protocolize = false;
        }
        Config.init();
        server.getCommandManager().register("send", new CommandSend(server));
        server.getCommandManager().register("sendall", new CommandSendall(server));
        server.getCommandManager().register("broadcast", new CommandBroadcast(server), "bc", "alert");
        server.getCommandManager().register("find", new CommandFind(server), "search");
        server.getCommandManager().register("staffchat", new CommandStaffChat(server), "sc");
        server.getCommandManager().register("restart", new CommandRestart(server));
        server.getCommandManager().register("tps", new CommandTp(server), "jump");
        server.getCommandManager().register("servers", new CommandServers(server), "allservers");
        server.getCommandManager().register("vtools", new CommandVTools(server), "vt");
        server.getEventManager().register(this, new CommandStaffChat(server));
    }

    public static VTools get() {
        return instance;
    }

    public static FileConfiguration getConfig() {
        return Config.DEFAULT;
    }

    public static File getDataFolder() {
        File dataFolder = instance.dataFolder;
        if (dataFolder == null) {
            String path = "plugins/VTools/";
            try {
                dataFolder = new File(path);
                dataFolder.mkdir();
                return dataFolder;
            } catch (Exception e) {
                return null;
            }
        } else {
            return dataFolder;
        }
    }

    public void saveResource(@NotNull String resourcePath, boolean replace) {
        if (resourcePath == null || resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getClass().getClassLoader().getResourceAsStream(resourcePath);
        System.out.println(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found");
        }

        File outFile = new File(instance.dataFolder, resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(instance.dataFolder, resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists() || replace) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {

            }
        } catch (IOException ex) {

        }
    }

    public InputStream getResource(@NotNull String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }

        try {
            URL url = getClass().getResource(filename);
            if (url == null) {
                return null;
            }

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ex) {
            return null;
        }
    }

    public static Logger logger() {
        return instance.logger;
    }

    public static ProxyServer getServer() {
        return instance.server;
    }
}
