package com.gufli.brickessentials.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.entity.Player;

import java.util.Optional;

public class TeleportHereCommand extends Command {

    public TeleportHereCommand() {
        super("teleporthere", "tphere");

        // conditions
        setCondition((sender, commandString) -> sender instanceof Player &&
                sender.hasPermission("brickessentials.teleport"));

        // usage
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /teleporthere <player>");
        });

        // arguments
        ArgumentWord player = ArgumentType.Word("player");

        // executor
        addSyntax(this::execute, player);
    }

    private void execute(CommandSender sender, CommandContext context) {
        Player player = (Player) sender;
        String targetName = context.get("player");

        Optional<Player> target = MinecraftServer.getConnectionManager().getOnlinePlayers().stream()
                .filter(p -> p.getUsername().equalsIgnoreCase(targetName)).findFirst();

        if (target.isEmpty()) {
            sender.sendMessage(Component.text(targetName + " is not online.")); // TODO
            return;
        }

        target.get().teleport(player.getPosition());
        sender.sendMessage(Component.text("Teleported " + target.get().getUsername() + " to you.")); // TODO
        target.get().sendMessage("You have been teleported to " + player.getUsername() + ".");
    }

}
