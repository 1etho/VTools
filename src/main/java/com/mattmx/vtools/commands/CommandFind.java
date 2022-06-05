package com.mattmx.vtools.commands;

import com.mattmx.vtools.VTools;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.mattmx.vtools.util.Chat;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandFind implements SimpleCommand {
    private final ProxyServer server;

    public CommandFind(ProxyServer server) {
        this.server = server;
    }


    @Override
    public void execute(SimpleCommand.Invocation invocation) {
        CommandSource commandSource = invocation.source();
        String[] strings = invocation.arguments();

        if (strings.length == 1) {
            Optional<Player> player = server.getPlayer(strings[0]);
            if (player.isPresent() && player.get().getCurrentServer().isPresent()) {
                commandSource.sendMessage(Chat.color(VTools.getConfig().getString("find-command.player-found"), player.get()));
                //commandSource.sendMessage(Component.text("Player " + strings[0] + " is on " + player.get().getCurrentServer().get().getServerInfo().getName() + "!").color(COLOR_YELLOW));
            } else {
                commandSource.sendMessage(Chat.color(VTools.getConfig().getString("find-command.player-not-online")
                        .replace("%player%", strings[0])));
                commandSource.sendMessage(Component.text("The player is not online!").color(VTools.COLOR_YELLOW));
            }
        } else {
            commandSource.sendMessage(Component.text("Usage: /find <username>").color(VTools.COLOR_RED));
        }
    }

    @Override
    public List<String> suggest(SimpleCommand.Invocation invocation) {
        String[] currentArgs = invocation.arguments();

        List<String> arg = new ArrayList<>();
        if (currentArgs.length == 1 && invocation.source().hasPermission("vtools.find.autocomplete")) {
            for (Player player : server.getAllPlayers()) {
                if (player.getUsername().startsWith(currentArgs[0])) arg.add(player.getUsername());
            }
        }
        return arg;
    }

    @Override
    public boolean hasPermission(SimpleCommand.Invocation invocation) {
        return invocation.source().hasPermission("vtools.find");
    }
}
