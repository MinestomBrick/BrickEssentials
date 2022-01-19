package com.gufli.brickessentials.commands;

import com.gufli.brickutils.commands.BrickCommand;
import com.gufli.brickutils.translation.TranslationManager;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

public class TeleportCommand extends BrickCommand {

    public TeleportCommand() {
        super("teleport", "tp");

        // conditions
        setCondition(b -> b.permission("brickessentials.teleport").playerOnly());

        // usage
        setInvalidUsageMessage("cmd.teleport.usage");

        // arguments
        ArgumentEntity entity = new ArgumentEntity("entity")
                .singleEntity(true);

        // executor
        addSyntax(this::execute, entity);
    }

    private void execute(CommandSender sender, CommandContext context) {
        Player player = (Player) sender;
        EntityFinder ef = context.get("entity");
        Entity entity = ef.findFirstEntity(sender);
        if ( entity == null ) {
            TranslationManager.get().send(sender, "cmd.teleport.invalid");
            return;
        }

        player.teleport(entity.getPosition());
        TranslationManager.get().send(sender, "cmd.teleport", entity.getCustomName());
    }

}
