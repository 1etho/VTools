package com.mattmx.vtools.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.mattmx.vtools.util.Chat;

import java.util.ArrayList;
import java.util.List;

public class CommandBroadcast implements SimpleCommand {
    private final ProxyServer server;

    public CommandBroadcast(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(SimpleCommand.Invocation invocation) {
        CommandSource commandSource = invocation.source();
        String[] strings = invocation.arguments();

        if (strings.length > 0) {
            String message = String.join(" ", strings);
            for (Player player : server.getAllPlayers()) {
                player.sendMessage(Chat.color(message, player));
            }
        } else {
            commandSource.sendMessage(Chat.color("&cUsage: /broadcast <message>"));
        }
    }

    @Override
    public List<String> suggest(SimpleCommand.Invocation invocation) {
        return new ArrayList<String>();
    }

    @Override
    public boolean hasPermission(SimpleCommand.Invocation invocation) {
        return invocation.source().hasPermission("vtools.broadcast");
    }
}
