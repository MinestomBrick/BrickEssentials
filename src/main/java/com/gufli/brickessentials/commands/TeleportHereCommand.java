package com.gufli.brickessentials.commands;

import com.gufli.brickutils.commands.BrickCommand;
import com.gufli.brickutils.translation.TranslationManager;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

import java.util.List;

public class TeleportHereCommand extends BrickCommand {

    public TeleportHereCommand() {
        super("teleporthere", "tphere");

        // conditions
        setCondition(b -> b.permission("brickessentials.teleport").playerOnly());


        // usage
        setInvalidUsageMessage("cmd.teleporthere.usage");

        // arguments
        ArgumentEntity entity = new ArgumentEntity("entity");

        // executor
        addSyntax(this::execute, entity);
    }

    private void execute(CommandSender sender, CommandContext context) {
        Player player = (Player) sender;
        EntityFinder ef = context.get("entity");
        List<Entity> entities = ef.find(sender);
        if ( entities.isEmpty() ) {
            TranslationManager.get().send(sender, "cmd.teleporthere.invalid");
            return;
        }

        Pos target = player.getPosition();
        entities.forEach(ent -> ent.teleport(target));
        entities.stream().filter(ent -> ent instanceof Player).map(ent -> (Player) ent).forEach(p ->
                TranslationManager.get().send(p, "cmd.teleporthere.target", player.getName()));

        if ( entities.size() > 1 ) {
            TranslationManager.get().send(sender, "cmd.teleporthere.multiple", entities.size() + "");
        } else if ( entities.get(0) instanceof Player p ) {
            TranslationManager.get().send(sender, "cmd.teleporthere.single", p.getName());
        } else {
            TranslationManager.get().send(sender, "cmd.teleporthere.single", entities.get(0).getCustomName());
        }
    }

}
