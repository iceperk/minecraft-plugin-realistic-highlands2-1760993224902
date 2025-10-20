package com.realistic_highlands2.commands;

import com.realistic_highlands2.RealisticHighlands2;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RLSeedCommand implements CommandExecutor {

    private final RealisticHighlands2 plugin;

    public RLSeedCommand(RealisticHighlands2 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("realistichighlands2.admin")) {
            sender.sendMessage("§cNie masz uprawnień do użycia tej komendy.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§eAktualny seed dla generowania świata: §f" + plugin.getPluginConfig().getSeed());
            return true;
        }

        if (args[0].equalsIgnoreCase("set")) {
            if (args.length < 2) {
                sender.sendMessage("§eUżycie: /rlseed set <nowy_seed>");
                return true;
            }
            try {
                long newSeed = Long.parseLong(args[1]);
                plugin.getPluginConfig().setSeed(newSeed);
                plugin.getPluginConfig().saveConfig();
                sender.sendMessage("§aSeed został ustawiony na: §f" + newSeed);
                sender.sendMessage("§eZmiana seeda będzie miała wpływ na nowo generowane chunk'i lub światy.");
            } catch (NumberFormatException e) {
                sender.sendMessage("§cNieprawidłowy format seeda. Podaj liczbę całkowitą.");
            }
        } else {
            sender.sendMessage("§eUżycie: /rlseed [set <nowy_seed>]");
        }

        return true;
    }
}
