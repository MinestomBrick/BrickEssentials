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
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.condition.CommandCondition;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

public class KillCommand extends CommandBase {

    public KillCommand() {
        super("kill");

        // conditions
        CommandCondition selfcondition = createCondition("brickessentials.kill", true);
        CommandCondition othercondition = createCondition("brickessentials.kill.other");
        setConditions(selfcondition, othercondition);

        // usage
        setInvalidUsageMessage("cmd.kill.usage");

        // arguments
        ArgumentPlayer player = new ArgumentPlayer("player");
        setInvalidArgumentMessage(player, "cmd.error.args.player");

        // self
        addConditionalSyntax(selfcondition, this::executeOnSelf);
        addConditionalSyntax(othercondition, this::executeOnOther, player);
    }

    private void executeOnSelf(CommandSender sender, CommandContext context) {
        Player player = (Player) sender;
        player.kill();
        TranslationManager.get().send(sender, "cmd.kill");
    }

    private void executeOnOther(CommandSender sender, CommandContext context) {
        Player target = context.get("player");
        target.kill();
        TranslationManager.get().send(sender, "cmd.kill.other", target.getUsername());
    }

}
