package com.realistic_highlands2.commands;

import com.realistic_highlands2.RealisticHighlands2;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RLGenCommand implements CommandExecutor {

    private final RealisticHighlands2 plugin;

    public RLGenCommand(RealisticHighlands2 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("realistichighlands2.admin")) {
            sender.sendMessage("§cNie masz uprawnień do użycia tej komendy.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§eUżycie: /rlgen <reload | info>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                plugin.reloadConfig();
                plugin.getPluginConfig().loadConfig();
                sender.sendMessage("§aKonfiguracja RealisticHighlands2 została przeładowana.");
                break;
            case "info":
                sender.sendMessage("§e--- RealisticHighlands2 Info ---");
                sender.sendMessage("§eWersja: §f" + plugin.getDescription().getVersion());
                sender.sendMessage("§eAutor: §f" + plugin.getDescription().getAuthors().get(0));
                sender.sendMessage("§eDomyślna nazwa świata: §f" + plugin.getPluginConfig().getDefaultWorldName());
                sender.sendMessage("§eAutomatyczne tworzenie świata: §f" + plugin.getPluginConfig().isAutoCreateDefaultWorld());
                sender.sendMessage("§eAktualny Seed: §f" + plugin.getPluginConfig().getSeed());
                sender.sendMessage("§e--- Koniec Info ---");
                break;
            default:
                sender.sendMessage("§eUżycie: /rlgen <reload | info>");
                break;
        }

        return true;
    }
}
