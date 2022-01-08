package com.gufli.brickessentials.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.condition.CommandCondition;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

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
        ArgumentEntity player = ArgumentType.Entity("player").onlyPlayers(true);

        setArgumentCallback((sender, exception) -> {
            sender.sendMessage(Component.text(exception.getInput() + " is not online.")); // TODO
        }, player);

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
        Player target = ((EntityFinder) context.get("player")).findFirstPlayer(sender);
        if ( target == null ) {
            return;
        }

        target.kill();
        sender.sendMessage(Component.text("You killed " + target.getUsername() + ".")); // TODO
    }

}
