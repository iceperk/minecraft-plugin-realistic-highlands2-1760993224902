package com.realistic_highlands2.commands;

import com.realistic_highlands2.RealisticHighlands2;
import com.realistic_highlands2.generator.RealisticWorldGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RLWorldCommand implements CommandExecutor {

    private final RealisticHighlands2 plugin;

    public RLWorldCommand(RealisticHighlands2 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cTa komenda może być używana tylko przez graczy.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("realistichighlands2.world")) {
            player.sendMessage("§cNie masz uprawnień do użycia tej komendy.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("§eUżycie: /rlworld <create [nazwa]> | <teleport [nazwa]>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                if (args.length < 2) {
                    player.sendMessage("§eUżycie: /rlworld create <nazwa_świata>");
                    return true;
                }
                String newWorldName = args[1];
                if (Bukkit.getWorld(newWorldName) != null) {
                    player.sendMessage("§cŚwiat o nazwie '" + newWorldName + "' już istnieje.");
                    return true;
                }

                player.sendMessage("§aRozpoczynanie tworzenia świata '" + newWorldName + "'. Może to chwilę potrwać...");
                WorldCreator wc = new WorldCreator(newWorldName);
                wc.generator(new RealisticWorldGenerator(plugin, plugin.getPluginConfig().getSeed()));
                // Użycie globalnego seeda dla nowo tworzonych światów, chyba że podano inny
                // wc.seed( ... ); 
                Bukkit.createWorld(wc);
                player.sendMessage("§aŚwiat '" + newWorldName + "' został utworzony.");
                break;
            case "teleport":
                if (args.length < 2) {
                    player.sendMessage("§eUżycie: /rlworld teleport <nazwa_świata>");
                    return true;
                }
                String targetWorldName = args[1];
                World targetWorld = Bukkit.getWorld(targetWorldName);
                if (targetWorld == null) {
                    player.sendMessage("§cŚwiat o nazwie '" + targetWorldName + "' nie istnieje.");
                    return true;
                }
                player.teleport(targetWorld.getSpawnLocation());
                player.sendMessage("§aTeleportowano do świata '" + targetWorldName + "'.");
                break;
            default:
                player.sendMessage("§eUżycie: /rlworld <create [nazwa]> | <teleport [nazwa]>");
                break;
        }

        return true;
    }
}
