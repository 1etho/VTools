package de.strifel.VTools.util;

public class DependencyChecker {
        public static boolean luckperms() {
            try {
                Class.forName("net.luckperms.api.LuckPerms");
                return true;
            } catch (ClassNotFoundException exception) {
                return false;
            }
        }
}
