package com.gufli.brickessentials.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;

import java.util.Arrays;
import java.util.Optional;

public class GamemodeCommand extends Command {

    public GamemodeCommand() {
        super("gamemode");

        // usage
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /gamemode [player] <gamemode>");
        });

        // arguments
        Argument<?> player = ArgumentType.Word("player");
        Argument<?> mode = ArgumentType.Word("mode")
                .from(Arrays.stream(GameMode.values()).map(gm -> gm.name().toLowerCase()).toArray(String[]::new));

        // invalid gamemode
        setArgumentCallback(((sender, exception) -> {
            sender.sendMessage("'" + exception.getInput() + "' is not a valid gamemode!");
        }), mode);

        // self
        addConditionalSyntax((sender, commandString) -> {
            return sender instanceof Player &&
                    sender.hasPermission("brickessentials.gamemode");
        }, this::executeOnSelf, mode);

        // other
        addConditionalSyntax((sender, commandString) -> {
            return sender instanceof ConsoleSender ||
                    sender.hasPermission("brickessentials.gamemode.other");
        }, this::executeOnOther, player, mode);
    }

    private void executeOnSelf(CommandSender sender, CommandContext context) {
        Player player = (Player) sender;
        String gamemodeName = context.get("mode");
        GameMode mode = GameMode.valueOf(gamemodeName.toUpperCase());
        player.setGameMode(mode);
        player.sendMessage(Component.text("Your gamemode has been changed to " + gamemodeName + ".")); // TODO
    }

    private void executeOnOther(CommandSender sender, CommandContext context) {
        String gamemodeName = context.get("mode");
        String targetName = context.get("player");
        GameMode mode = GameMode.valueOf(gamemodeName.toUpperCase());

        Optional<Player> target = MinecraftServer.getConnectionManager().getOnlinePlayers().stream()
                .filter(p -> p.getUsername().equalsIgnoreCase(targetName)).findFirst();

        if (target.isPresent()) {
            target.get().setGameMode(mode);
            sender.sendMessage(Component.text("You changed the gamemode of " + target.get().getUsername() + " to " + gamemodeName + ".")); // TODO
        } else {
            sender.sendMessage(Component.text("The player " + targetName + " is not online.")); // TODO
        }
    }

}
