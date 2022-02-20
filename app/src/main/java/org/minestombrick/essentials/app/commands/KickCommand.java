package org.minestombrick.essentials.app.commands;

import org.minestombrick.commandtools.api.arguments.ArgumentPlayer;
import org.minestombrick.commandtools.api.command.BrickCommand;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.entity.Player;
import org.minestombrick.i18n.api.I18nAPI;

public class KickCommand extends BrickCommand {

    public KickCommand() {
        super("kick");

        // conditions
        setCondition(b -> b.permission("brick.essentials.kick", "cmd.error.permission"));

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
            target.kick(I18nAPI.get(this).translate(target, "cmd.kick.target"));
        } else {
            target.kick(Component.text(message));
        }

        I18nAPI.get(this).send(sender, "cmd.kick", target.getUsername());
    }

}
