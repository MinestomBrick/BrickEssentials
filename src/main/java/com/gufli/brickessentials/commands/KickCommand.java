package com.gufli.brickessentials.commands;

import com.gufli.brickutils.commands.ArgumentPlayer;
import com.gufli.brickutils.commands.BrickCommand;
import com.gufli.brickutils.translation.TranslationManager;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.entity.Player;

public class KickCommand extends BrickCommand {

    public KickCommand() {
        super("kick");

        // conditions
        setCondition(b -> b.permission("brickessentials.kick", "cmd.error.permission"));

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
        if (message.equals("")) {
            target.kick(TranslationManager.get().translate(target, "cmd.kick.target"));
        } else {
            target.kick(Component.text(message));
        }

        TranslationManager.get().send(sender, "cmd.kick", target.getUsername());
    }

}
