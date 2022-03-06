package de.strifel.VTools.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.strifel.VTools.VTools;
import de.strifel.VTools.util.Chat;
import net.kyori.adventure.text.Component;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static de.strifel.VTools.VTools.COLOR_RED;
import static de.strifel.VTools.VTools.COLOR_YELLOW;

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
                commandSource.sendMessage(Chat.color(VTools.getConfig().getString("send-command.sent"), player));
                player.sendMessage(Chat.color(VTools.getConfig().getString("send-command.got-sent"), player));
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

    public List<String> suggest(SimpleCommand.Invocation invocation) {
        String[] currentArgs = invocation.arguments();

        List<String> arg = new ArrayList<String>();
        if (currentArgs.length == 1) {
            for (Player player : server.getAllPlayers()) {
                if (currentArgs[0].startsWith(player.getUsername())) arg.add(player.getUsername());
            }
            return arg;
        } else if (currentArgs.length == 2) {
            for (RegisteredServer server : server.getAllServers()) {
                if (currentArgs[1].startsWith(server.getServerInfo().getName())) arg.add(server.getServerInfo().getName());
            }
        }
        return arg;
    }

    public boolean hasPermission(SimpleCommand.Invocation invocation) {
        return invocation.source().hasPermission("vtools.send");
    }
}
