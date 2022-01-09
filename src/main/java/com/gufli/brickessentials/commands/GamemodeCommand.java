package com.gufli.brickessentials.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.condition.CommandCondition;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

public class GamemodeCommand extends Command {

    public GamemodeCommand() {
        super("gamemode");

        // conditions
        CommandCondition selfcondition = (sender, cs) ->
                sender instanceof Player p && (
                        p.hasPermission("brickessentials.gamemode") ||
                        p.getPermissionLevel() == 4
                );

        CommandCondition othercondition = (sender, cs) ->
                sender instanceof ConsoleSender ||
                sender.hasPermission("brickessentials.gamemode.other") ||
                (sender instanceof Player p && p.getPermissionLevel() == 4);

        setCondition(((sender, cs) -> selfcondition.canUse(sender, cs)
                || othercondition.canUse(sender, cs)));

        // usage
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /gamemode [player] <gamemode>");
        });

        // arguments
        ArgumentEntity player = ArgumentType.Entity("player")
                .onlyPlayers(true).singleEntity(true);

        ArgumentEnum<GameMode> mode = ArgumentType.Enum("gamemode", GameMode.class)
                .setFormat(ArgumentEnum.Format.LOWER_CASED);

        // invalid gamemode
        setArgumentCallback((sender, exception) -> {
            sender.sendMessage(exception.getInput() + " is not a valid gamemode!");
        }, mode);

        setArgumentCallback((sender, exception) -> {
            sender.sendMessage(Component.text(exception.getInput() + " is not online.")); // TODO
        }, player);

        // self
        addConditionalSyntax(selfcondition, this::executeOnSelf, mode);

        // other
        addConditionalSyntax(othercondition, this::executeOnOther, player, mode);
    }

    private void executeOnSelf(CommandSender sender, CommandContext context) {
        Player player = (Player) sender;
        GameMode gamemode = context.get("gamemode");
        player.setGameMode(gamemode);
        player.sendMessage(Component.text("Your gamemode has been changed to " + gamemode + ".")); // TODO
    }

    private void executeOnOther(CommandSender sender, CommandContext context) {
        Player target = ((EntityFinder) context.get("player")).findFirstPlayer(sender);
        if (target == null) {
            return;
        }

        GameMode gamemode = context.get("gamemode");
        target.setGameMode(gamemode);
        sender.sendMessage(Component.text("You changed the gamemode of " + target.getUsername() + " to " + gamemode + ".")); // TODO
    }

}
