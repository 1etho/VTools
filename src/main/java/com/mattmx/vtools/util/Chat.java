package com.mattmx.vtools.util;

import com.mattmx.vtools.VTools;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.platform.PlayerAdapter;

public class Chat {
    private static LegacyComponentSerializer serializer = LegacyComponentSerializer.builder().character('&').hexCharacter('#').hexColors().build();
    public static TextComponent color(String s, Player p) {
        if (p != null) {
            s = format(s, p);
        } else {
            s = format(s);
        };
        return serializer.deserialize(s);
    }
    public static TextComponent color(String s) {
        s = format(s);
        return serializer.deserialize(s);
    }
    public static LegacyComponentSerializer getSerializer() {
        return serializer;
    }

    public static String format(String s, Player p) {
        LuckPerms lp = LuckPermsProvider.get();
        PlayerAdapter<Player> playerAdapter = lp.getPlayerAdapter(Player.class);
        CachedMetaData data = playerAdapter.getUser(p).getCachedData().getMetaData();
        String prefix = data.getPrefix();
        String suffix = data.getSuffix();
        String prefixes = String.join(" ", data.getPrefixes().values());
        String suffixes = String.join(" ", data.getSuffixes().values());
        s = s.replace("%prefix%", prefix == null ? "" : prefix)
                .replace("%suffix%", suffix == null ? "" : suffix)
                .replace("%prefixes%", prefixes)
                .replace("%suffixes%", suffixes)
                .replace("%player%", p.getUsername())
                .replace("%username%", p.getUsername())
                .replace("%server%", p.getCurrentServer().isPresent() ? p.getCurrentServer().get().getServerInfo().getName() : "None");
        return format(s);
    }

    public static String format(String s) {
        return s.replace("%all-online%", Integer.toString(VTools.getServer().getAllPlayers().size()));
    }
}
