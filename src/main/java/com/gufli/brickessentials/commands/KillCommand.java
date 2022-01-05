package com.gufli.brickessentials.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.condition.CommandCondition;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;

import java.util.Optional;

public class KillCommand extends Command {

    public KillCommand() {
        super("kill");

        // conditions
        CommandCondition selfcondition = (sender, cs) -> sender instanceof Player &&
                sender.hasPermission("brickessentials.kill");
        CommandCondition othercondition = (sender, cs) -> sender instanceof ConsoleSender ||
                sender.hasPermission("brickessentials.kill.other");
        setCondition(((sender, cs) -> selfcondition.canUse(sender, cs)
                || othercondition.canUse(sender, cs)));

        // usage
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /kill <player>");
        });

        // arguments
        ArgumentWord player = ArgumentType.Word("player");

        // self
        addConditionalSyntax(selfcondition, this::executeOnSelf);

        // other
        addConditionalSyntax(othercondition, this::executeOnOther, player);
    }

    private void executeOnSelf(CommandSender sender, CommandContext context) {
        Player player = (Player) sender;
        player.kill();
        player.sendMessage(Component.text("You killed yourself.")); // TODO
    }

    private void executeOnOther(CommandSender sender, CommandContext context) {
        String targetName = context.get("player");

        Optional<Player> target = MinecraftServer.getConnectionManager().getOnlinePlayers().stream()
                .filter(p -> p.getUsername().equalsIgnoreCase(targetName)).findFirst();

        if (target.isEmpty()) {
            sender.sendMessage(Component.text(targetName + " is not online.")); // TODO
            return;
        }

        target.get().kill();
        sender.sendMessage(Component.text("You killed " + target.get().getUsername() + ".")); // TODO
    }

}
