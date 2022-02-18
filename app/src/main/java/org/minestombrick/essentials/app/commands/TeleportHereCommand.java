package org.minestombrick.essentials.app.commands;

import com.gufli.brickutils.commands.BrickCommand;
import org.minestombrick.i18n.api.translation.I18nAPI;
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
            I18nAPI.get(this).send(sender, "cmd.teleporthere.invalid");
            return;
        }

        Pos target = player.getPosition();
        entities.forEach(ent -> ent.teleport(target));
        entities.stream().filter(ent -> ent instanceof Player).map(ent -> (Player) ent).forEach(p ->
                I18nAPI.get(this).send(p, "cmd.teleporthere.target", player.getName()));

        if ( entities.size() > 1 ) {
            I18nAPI.get(this).send(sender, "cmd.teleporthere.multiple", entities.size() + "");
        } else if ( entities.get(0) instanceof Player p ) {
            I18nAPI.get(this).send(sender, "cmd.teleporthere.single", p.getName());
        } else {
            I18nAPI.get(this).send(sender, "cmd.teleporthere.single", entities.get(0).getCustomName());
        }
    }

}
