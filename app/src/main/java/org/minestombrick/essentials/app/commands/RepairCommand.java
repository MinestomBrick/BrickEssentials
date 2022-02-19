package org.minestombrick.essentials.app.commands;

import org.minestombrick.commandtools.api.command.BrickCommand;
import org.minestombrick.i18n.api.I18nAPI;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;

public class RepairCommand extends BrickCommand {

    public RepairCommand() {
        super("repair");

        // condition
        setCondition(b -> b.permission("brickessentials.repair").playerOnly());

        // usage
        setDefaultExecutor(this::execute);
    }

    private void execute(CommandSender sender, CommandContext context) {
        Player player = (Player) sender;

        ItemStack itemStack = player.getItemInMainHand();
        if (itemStack.isAir()) {
            I18nAPI.get(this).send(sender, "cmd.error.args.itemstack");
            return;
        }

        itemStack = itemStack.with(b -> b.meta(mb -> mb.damage(0)));
        player.setItemInMainHand(itemStack);

        I18nAPI.get(this).send(sender, "cmd.repair");
    }

}
