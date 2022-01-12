package com.gufli.brickessentials.commands;

import com.gufli.brickutils.commands.ArgumentPlayer;
import com.gufli.brickutils.commands.CommandBase;
import com.gufli.brickutils.translation.TranslationManager;
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

public class TeleportHereCommand extends CommandBase {

    public TeleportHereCommand() {
        super("teleporthere", "tphere");

        // conditions
        setCondition("brickessentials.teleport", true);

        // usage
        setInvalidUsageMessage("cmd.teleporthere.usage");

        // arguments
        ArgumentPlayer player = new ArgumentPlayer("player");
        setInvalidArgumentMessage(player, "cmd.error.args.player");

        // executor
        addSyntax(this::execute, player);
    }

    private void execute(CommandSender sender, CommandContext context) {
        Player player = (Player) sender;
        Player target = context.get("player");

        target.teleport(player.getPosition());
        TranslationManager.get().send(sender, "cmd.teleporthere", target.getUsername());
        TranslationManager.get().send(target, "cmd.teleporthere.target", player.getUsername());
    }

}
