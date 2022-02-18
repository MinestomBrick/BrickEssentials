package org.minestombrick.essentials.app.listeners;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.other.BoatMeta;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.Direction;

public class SpawnEggListener {

    public SpawnEggListener() {
        MinecraftServer.getGlobalEventHandler().addListener(PlayerBlockInteractEvent.class, this::onBlockClick);
    }

    public void onBlockClick(PlayerBlockInteractEvent event) {
        ItemStack item = event.getPlayer().getItemInHand(event.getHand());
        if ( item.isAir() ) {
            return;
        }

        Material mat = item.getMaterial();
        if ( !mat.name().contains("spawn_egg") ) {
            return;
        }

        Direction dir = event.getBlockFace().toDirection();
        Point position = event.getBlockPosition().add(dir.normalX(), dir.normalY(), dir.normalZ());

        String name = mat.name().substring(10).replace("_spawn_egg", "");
        EntityType type = EntityType.fromNamespaceId(name.toLowerCase());

        EntityCreature entity = new EntityCreature(type);
        entity.setInstance(event.getPlayer().getInstance(), new Pos(position));
    }

}
