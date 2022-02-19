package org.minestombrick.essentials.app.commands;

import org.minestombrick.commandtools.api.command.BrickCommand;
import org.minestombrick.i18n.api.I18nAPI;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.arguments.relative.ArgumentRelativeBlockPosition;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;
import net.minestom.server.utils.location.RelativeVec;

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

        ArgumentRelativeBlockPosition block = new ArgumentRelativeBlockPosition("position");

        // executor
        addSyntax(this::executeEntity, entity);
        addSyntax(this::executePos, block);
    }

    private void executeEntity(CommandSender sender, CommandContext context) {
        Player player = (Player) sender;
        EntityFinder ef = context.get("entity");
        Entity entity = ef.findFirstEntity(sender);
        if ( entity == null ) {
            I18nAPI.get(this).send(sender, "cmd.teleport.invalid");
            return;
        }

        player.teleport(entity.getPosition());
        I18nAPI.get(this).send(sender, "cmd.teleport", entity.getCustomName());
    }

    private void executePos(CommandSender sender, CommandContext context) {
        Player player = (Player) sender;
        RelativeVec vec = context.get("position");

        Vec target = vec.from(player);
        player.teleport(new Pos(target.x(), target.y(), target.z()));
        I18nAPI.get(this).send(sender, "cmd.teleport", target);
    }

}
