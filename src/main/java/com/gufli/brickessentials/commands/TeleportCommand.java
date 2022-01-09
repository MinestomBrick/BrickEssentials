package com.gufli.brickessentials.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

public class TeleportCommand extends Command {

    public TeleportCommand() {
        super("teleport", "tp");

        // conditions
        setCondition((sender, commandString) -> sender instanceof Player p && (
                p.hasPermission("brickessentials.teleport") ||
                p.getPermissionLevel() == 4
        ));

        // usage
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /teleport <player>");
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
        if (target == null) {
            return;
        }

        player.teleport(target.getPosition());
        sender.sendMessage(Component.text("Teleported to " + target.getUsername() + ".")); // TODO
    }

}
