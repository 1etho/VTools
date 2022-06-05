package com.mattmx.vtools.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.mattmx.vtools.VTools;
import com.mattmx.vtools.util.Chat;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mattmx.vtools.VTools.COLOR_RED;

public class CommandSend implements SimpleCommand {
    private final ProxyServer server;

    public CommandSend(ProxyServer server) {
        this.server = server;
    }

    public void execute(SimpleCommand.Invocation invocation) {
        CommandSource commandSource = invocation.source();
        String[] strings = invocation.arguments();

        if (strings.length == 2) {
            Optional<Player> oPlayer = server.getPlayer(strings[0]);
            Optional<RegisteredServer> oServer = server.getServer(strings[1]);
            if (oPlayer.isPresent() && oServer.isPresent()) {
                Player player = oPlayer.get();
                RegisteredServer server = oServer.get();
                player.createConnectionRequest(server).connect();
                commandSource.sendMessage(Chat.color(VTools.getConfig().getString("send-command.sent")
                        .replace("%server%", strings[1]), player));
                player.sendMessage(Chat.color(VTools.getConfig().getString("send-command.got-sent")
                        .replace("%server%", strings[1]), player));
                //commandSource.sendMessage(Component.text("You send " + player.getUsername() + " to " + server.getServerInfo().getName()).color(COLOR_YELLOW));
                //commandSource.sendMessage(Component.text("You got send to " + server.getServerInfo().getName()).color(COLOR_YELLOW));
            } else {
                commandSource.sendMessage(Chat.color(VTools.getConfig().getString("send-command.error")));
                //commandSource.sendMessage(Component.text("The server or user does not exists!").color(COLOR_RED));
            }
        } else {
            commandSource.sendMessage(Component.text("Usage: /send <username> <server>").color(COLOR_RED));
        }
    }

    @Override
    public List<String> suggest(SimpleCommand.Invocation invocation) {
        String[] currentArgs = invocation.arguments();
        List<String> arg = new ArrayList<String>();
        if (currentArgs.length == 1) {
            for (Player player : server.getAllPlayers()) {
                if (player.getUsername().startsWith(currentArgs[0])) arg.add(player.getUsername());
            }
        } else if (currentArgs.length == 2) {
            for (RegisteredServer server : server.getAllServers()) {
                if (server.getServerInfo().getName().startsWith(currentArgs[1])) arg.add(server.getServerInfo().getName());
            }
        }
        return arg;
    }

    public boolean hasPermission(SimpleCommand.Invocation invocation) {
        return invocation.source().hasPermission("vtools.send");
    }
}
