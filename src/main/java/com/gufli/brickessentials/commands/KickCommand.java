package com.gufli.brickessentials.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

public class KickCommand extends Command {

    public KickCommand() {
        super("kick");

        // conditions
        setCondition((sender, commandString) -> sender instanceof ConsoleSender ||
                sender.hasPermission("brickessentials.kick") ||
                (sender instanceof Player p && p.getPermissionLevel() == 4));

        // usage
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /kick <player> <message>");
        });

        // arguments
        ArgumentEntity player = ArgumentType.Entity("player").onlyPlayers(true);
        ArgumentWord message = ArgumentType.Word("message");
        message.setDefaultValue("");

        setArgumentCallback((sender, exception) -> {
            sender.sendMessage(Component.text(exception.getInput() + " is not online.")); // TODO
        }, player);

        // executor
        addSyntax(this::execute, player, message);
    }

    private void execute(CommandSender sender, CommandContext context) {
        Player target = ((EntityFinder) context.get("player")).findFirstPlayer(sender);
        if ( target == null ) {
            return;
        }

        String message = context.get("message");
        target.kick(Component.text(message));

        sender.sendMessage(Component.text("You kicked " + target.getUsername() + " from the server.")); // TODO
    }

}
