package com.gufli.brickessentials.commands;

import com.gufli.brickutils.commands.ArgumentPlayer;
import com.gufli.brickutils.commands.CommandBase;
import com.gufli.brickutils.translation.TranslationManager;
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

public class KickCommand extends CommandBase {

    public KickCommand() {
        super("kick");

        // conditions
        setCondition("brickessentials.kick");

        // usage
        setInvalidUsageMessage("cmd.kick.usage");

        // arguments
        ArgumentPlayer player = new ArgumentPlayer("player");
        setInvalidArgumentMessage(player, "cmd.error.args.player");

        ArgumentWord message = ArgumentType.Word("message");
        message.setDefaultValue("");

        // executor
        addSyntax(this::execute, player, message);
    }

    private void execute(CommandSender sender, CommandContext context) {
        Player target = context.get("player");

        String message = context.get("message");
        if ( message.equals("") ) {
            target.kick(TranslationManager.get().translate(target, "cmd.kick.target"));
        } else {
            target.kick(Component.text(message));
        }

        TranslationManager.get().send(sender, "cmd.kick", target.getUsername());
    }

}
