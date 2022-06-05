package com.mattmx.vtools.commands;

import com.mattmx.vtools.VTools;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.command.VelocityBrigadierMessage;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.mattmx.vtools.util.Chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CommandStaffChat implements SimpleCommand {
    private final ProxyServer server;
    private List<String> toggled = new ArrayList<>();

    public CommandStaffChat(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(SimpleCommand.Invocation invocation) {
        CommandSource commandSource = invocation.source();
        String[] strings = invocation.arguments();

        if (strings.length > 0) {
            String channel = strings[0].startsWith("c:") && !strings[0].equals("c:") ? strings[0].split(":")[1] : null;
            //String message = "§4[Staff]§r " + (commandSource instanceof Player ? ((Player) commandSource).getUsername() : "Console") + (channel != null ? " (" + channel + ")" : "")+ " > " + String.join(" ", Arrays.copyOfRange(strings, channel == null ? 0 : 1, strings.length)).replace("&", "§");
            String message = VTools.getConfig().getString("staff-chat.format")
                    .replace("%message%", String.join(" ", Arrays.copyOfRange(strings, channel == null ? 0 : 1, strings.length)));
            for (Player player : server.getAllPlayers()) {
                if (player.hasPermission("vtools.staffchat" + (channel != null ? "." + channel : ""))) {
                    player.sendMessage(Chat.color(message, (commandSource instanceof Player ? ((Player) commandSource) : null)));
                }
            }
        } else {
            if (invocation.source() instanceof Player p) {
                if (toggled.contains(p.getUniqueId().toString())) {
                    toggled.remove(p.getUniqueId().toString());
                    p.sendMessage(Chat.color(VTools.getConfig().getString("staff-chat.toggled").replace("%state%", "off")));
                } else {
                    toggled.add(p.getUniqueId().toString());
                    p.sendMessage(Chat.color(VTools.getConfig().getString("staff-chat.toggled").replace("%state%", "on")));
                }
            }
        }
    }

    @Override
    public List<String> suggest(SimpleCommand.Invocation invocation) {
        return new ArrayList<String>();
    }

    @Override
    public boolean hasPermission(SimpleCommand.Invocation invocation) {
        return invocation.source().hasPermission("vtools.staffchat");
    }

    @Subscribe
    public void onChat(PlayerChatEvent e) {
        if (!toggled.contains(e.getPlayer().getUniqueId().toString())) return;
        if (e.getPlayer().hasPermission("vtools.staffchat")) {
            String message = VTools.getConfig().getString("staff-chat.format").replace("%message%", e.getMessage());
            for (Player player : server.getAllPlayers()) {
                if (player.hasPermission("vtools.staffchat")) {
                    player.sendMessage(Chat.color(message, e.getPlayer()));
                }
            }
        } else {
            e.getPlayer().sendMessage(Chat.color(VTools.getConfig().getString("staff-chat.toggled").replace("%state%", "off")));
            toggled.remove(e.getPlayer().getUniqueId().toString());
        }
    }

}
