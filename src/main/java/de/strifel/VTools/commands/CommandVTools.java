package de.strifel.VTools.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.strifel.VTools.VTools;
import de.strifel.VTools.util.Chat;
import de.strifel.VTools.util.Config;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static de.strifel.VTools.VTools.COLOR_RED;

public class CommandVTools implements SimpleCommand {
    private final ProxyServer server;

    public CommandVTools(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(Invocation commandInvocation) {
        CommandSource commandSource = commandInvocation.source();
        String[] args = commandInvocation.arguments();

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "reload" -> {
                    Config.init();
                    commandSource.sendMessage(Chat.color("&cConfig files reloaded."));
                }
            }
        }
    }

    @Override
    public List<String> suggest(Invocation commandInvocation) {
        String[] currnetArgs = commandInvocation.arguments();
        List<String> arg = new ArrayList<>();
        if (commandInvocation.arguments().length == 1) {
            arg.add("reload");
        }
        return arg;
    }

    @Override
    public boolean hasPermission(Invocation commandInvocation) {
        return commandInvocation.source().hasPermission("vtools.admin");
    }
}
