package com.mattmx.vtools.util;

public class DependencyChecker {
        public static boolean luckperms() {
            try {
                Class.forName("net.luckperms.api.LuckPerms");
                return true;
            } catch (ClassNotFoundException exception) {
                return false;
            }
        }

        public static boolean protocolize() {
            try {
                Class.forName("dev.simplix.protocolize.api.Platform");
                return true;
            } catch (ClassNotFoundException exception) {
                return false;
            }
        }
}
