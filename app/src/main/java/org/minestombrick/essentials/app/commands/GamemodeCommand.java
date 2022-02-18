package org.minestombrick.essentials.app.commands;

import com.gufli.brickutils.commands.ArgumentPlayer;
import com.gufli.brickutils.commands.BrickCommand;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import org.minestombrick.i18n.api.translation.I18nAPI;

public class GamemodeCommand extends BrickCommand {

    public GamemodeCommand() {
        super("gamemode", "gm");

        // invalid usage
        setInvalidUsageMessage("cmd.gamemode.usage");

        // arguments
        ArgumentPlayer player = new ArgumentPlayer("player");
        setInvalidArgumentMessage(player, "cmd.error.args.player");

        ArgumentEnum<GameMode> gamemode = ArgumentType.Enum("gamemode", GameMode.class)
                .setFormat(ArgumentEnum.Format.LOWER_CASED);
        setInvalidArgumentMessage(gamemode);

        addConditionalSyntax(b -> b.permission("brickessentials.gamemode").playerOnly(),
                this::executeOnSelf, gamemode);

        addConditionalSyntax(b -> b.permission("brickessentials.gamemode.other"),
                this::executeOnOther, player, gamemode);
    }

    private void executeOnSelf(CommandSender sender, CommandContext context) {
        Player player = (Player) sender;
        GameMode gamemode = context.get("gamemode");
        player.setGameMode(gamemode);
        I18nAPI.get(this).send(sender, "cmd.gamemode", gamemode.name());
    }

    private void executeOnOther(CommandSender sender, CommandContext context) {
        Player target = context.get("player");
        GameMode gamemode = context.get("gamemode");
        target.setGameMode(gamemode);

        if (target != sender) {
            I18nAPI.get(this).send(target, "cmd.gamemode.other.target", gamemode.name());
        }

        I18nAPI.get(this).send(sender, "cmd.gamemode.other", target.getUsername(), gamemode.name());
    }

}
