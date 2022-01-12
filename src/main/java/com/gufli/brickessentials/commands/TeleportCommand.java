package com.gufli.brickessentials.commands;

import com.gufli.brickutils.commands.ArgumentPlayer;
import com.gufli.brickutils.commands.CommandBase;
import com.gufli.brickutils.translation.TranslationManager;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;

public class TeleportCommand extends CommandBase {

    public TeleportCommand() {
        super("teleport", "tp");

        // conditions
        setCondition("brickessentials.teleport", true);

        // usage
        setInvalidUsageMessage("cmd.teleport.usage");

        // arguments
        ArgumentPlayer player = new ArgumentPlayer("player");
        setInvalidArgumentMessage(player, "cmd.error.args.player");

        // executor
        addSyntax(this::execute, player);
    }

    private void execute(CommandSender sender, CommandContext context) {
        Player player = (Player) sender;
        Player target = context.get("player");
        player.teleport(target.getPosition());
        TranslationManager.get().send(sender, "cmd.teleport", target.getUsername());
    }

}
