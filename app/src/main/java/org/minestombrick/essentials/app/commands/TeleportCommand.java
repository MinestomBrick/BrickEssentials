package org.minestombrick.essentials.app.commands;

import net.kyori.adventure.text.Component;
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
import org.minestombrick.commandtools.api.command.BrickCommand;
import org.minestombrick.i18n.api.I18nAPI;

import java.util.Collections;
import java.util.List;

public class TeleportCommand extends BrickCommand {

    public TeleportCommand() {
        super("teleport", "tp");

        // conditions
        setCondition(b -> b.permission("brick.essentials.teleport").playerOnly());

        // usage
        setInvalidUsageMessage("cmd.teleport.usage");

        // arguments
        ArgumentEntity target = new ArgumentEntity("targetEntity")
                .singleEntity(true);

        ArgumentEntity entities = new ArgumentEntity("entities");

        ArgumentRelativeBlockPosition position = new ArgumentRelativeBlockPosition("position");

        // executor
        addSyntax(this::executeEntity, target);
        addConditionalSyntax(b -> b.permission("brick.essentials.teleport.other").playerOnly(),
                this::executeEntity, entities, target);

        addSyntax(this::executePos, position);
        addConditionalSyntax(b -> b.permission("brick.essentials.teleport.other").playerOnly(),
                this::executePos, entities, position);
    }

    private void executeEntity(CommandSender sender, CommandContext context) {
        List<Entity> entities = entities(sender, context);

        EntityFinder tef = context.get("targetEntity");
        Entity target = tef.findFirstEntity(sender);

        if ( target == null || entities.isEmpty()) {
            I18nAPI.get(this).send(sender, "cmd.teleport.invalid");
            return;
        }

        teleport(sender, entities, target.getPosition(), name(target));
    }

    private void executePos(CommandSender sender, CommandContext context) {
        List<Entity> entities = entities(sender, context);
        if ( entities.isEmpty()) {
            I18nAPI.get(this).send(sender, "cmd.teleport.invalid");
            return;
        }

        Player player = (Player) sender;
        RelativeVec vec = context.get("position");

        Pos target = vec.from(player).asPosition();

        teleport(sender, entities, target, Component.text(String.format("%.2f %.2f %.2f", target.x(), target.y(), target.z())));
    }

    // util

    private List<Entity> entities(CommandSender sender, CommandContext context) {
        EntityFinder ef = context.get("entities");
        List<Entity> entities;
        if ( ef != null ) {
            entities = ef.find(sender);
        } else {
            entities = Collections.singletonList((Player) sender);
        }

        return entities;
    }

    private Component name(Entity entity) {
        Component name = entity.getCustomName();
        if (name == null) {
            if (entity instanceof Player tp) {
                name = tp.getName();
            } else {
                name = Component.text(entity.getEntityType().name().substring(10));
            }
        }
        return name;
    }

    private void teleport(CommandSender sender, List<Entity> entities, Pos target, Component targetDisplay) {
        for ( Entity ent : entities ) {
            if ( ent instanceof Player p ) {
                I18nAPI.get(this).send(p, "cmd.teleport", targetDisplay);
            }

            ent.teleport(target);
        }

        if ( entities.size() == 1 && !entities.contains(sender)) {
            I18nAPI.get(this).send(sender, "cmd.teleport.other.single", name(entities.get(0)), targetDisplay);
        } else if ( entities.size() > 1) {
            I18nAPI.get(this).send(sender, "cmd.teleport.other.multiple", entities.size(), targetDisplay);
        }
    }

}
