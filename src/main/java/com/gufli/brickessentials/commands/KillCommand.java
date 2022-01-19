package com.gufli.brickessentials.commands;

import com.gufli.brickutils.commands.BrickCommand;
import com.gufli.brickutils.translation.TranslationManager;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

import java.util.List;

public class KillCommand extends BrickCommand {

    public KillCommand() {
        super("kill");

        // usage
        setInvalidUsageMessage("cmd.kill.usage");

        // arguments
        ArgumentEntity entity = new ArgumentEntity("entity");

        // self
        addConditionalSyntax(b -> b.permission("brickessentials.kill"),
                this::execute, entity);
    }

    private void execute(CommandSender sender, CommandContext context) {
        EntityFinder ef = context.get("entity");
        List<Entity> entities = ef.find(sender);
        if (entities.isEmpty()) {
            TranslationManager.get().send(sender, "cmd.kill.invalid");
            return;
        }

        entities.forEach(ent -> {
            if (ent instanceof Player p) {
                p.kill();
            } else {
                ent.remove();
            }
        });

        if (entities.size() > 1) {
            TranslationManager.get().send(sender, "cmd.kill.multiple", entities.size() + "");
        } else if ( entities.get(0) instanceof Player p ){
            TranslationManager.get().send(sender, "cmd.kill.single", p.getName());
        } else {
            TranslationManager.get().send(sender, "cmd.kill.single", entities.get(0).getEntityType().name());
        }
    }

}
