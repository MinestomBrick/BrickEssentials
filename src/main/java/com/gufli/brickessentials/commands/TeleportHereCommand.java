package com.gufli.brickessentials.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

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
        ArgumentEntity player = ArgumentType.Entity("player").onlyPlayers(true);

        setArgumentCallback((sender, exception) -> {
            sender.sendMessage(Component.text(exception.getInput() + " is not online.")); // TODO
        }, player);

        // executor
        addSyntax(this::execute, player);
    }

    private void execute(CommandSender sender, CommandContext context) {
        Player player = (Player) sender;
        Player target = ((EntityFinder) context.get("player")).findFirstPlayer(sender);
        if ( target == null ) {
            return;
        }

        target.teleport(player.getPosition());
        sender.sendMessage(Component.text("Teleported " + target.getUsername() + " to you.")); // TODO
        target.sendMessage("You have been teleported to " + player.getUsername() + ".");
    }

}
