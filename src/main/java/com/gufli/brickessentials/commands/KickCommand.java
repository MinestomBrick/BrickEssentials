package com.gufli.brickessentials.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.entity.Player;

import java.util.Optional;

public class KickCommand extends Command {

    public KickCommand() {
        super("kick");

        // conditions
        setCondition((sender, commandString) -> sender instanceof ConsoleSender ||
                sender.hasPermission("brickessentials.kick"));

        // usage
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /kick <player> <message>");
        });

        // arguments
        ArgumentWord player = ArgumentType.Word("player");
        ArgumentWord message = ArgumentType.Word("message");
        message.setDefaultValue("");

        // executor
        addSyntax(this::execute, player, message);
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

        String message = context.get("message");
        player.kick(Component.text(message));

        sender.sendMessage(Component.text("You kicked " + target.get().getUsername() + " from the server.")); // TODO
    }

}
