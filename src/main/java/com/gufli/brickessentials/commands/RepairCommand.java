package com.gufli.brickessentials.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;

public class RepairCommand extends Command {

    public RepairCommand() {
        super("repair");

        // condition
        setCondition((sender, commandString) -> sender instanceof Player p && (
                        p.hasPermission("brickessentials.repair") ||
                        p.getPermissionLevel() == 4
                )
        );

        // usage
        setDefaultExecutor(this::execute);
    }

    private void execute(CommandSender sender, CommandContext context) {
        Player player = (Player) sender;

        ItemStack itemStack = player.getItemInMainHand();
        if (itemStack.isAir()) {
            sender.sendMessage(Component.text("You need to hold an item in your main hand.")); // TODO
            return;
        }

        itemStack = itemStack.with(b -> b.meta(mb -> mb.damage(0)));
        player.setItemInMainHand(itemStack);

        sender.sendMessage(Component.text("Repaired the item in your hand.")); // TODO
    }

}
