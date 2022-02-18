package org.minestombrick.essentials.app.listeners;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.metadata.other.BoatMeta;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.client.play.ClientSteerBoatPacket;
import net.minestom.server.network.packet.client.play.ClientSteerVehiclePacket;
import net.minestom.server.utils.Direction;

public class VehicleListener {

    public VehicleListener() {
        MinecraftServer.getGlobalEventHandler().addListener(PlayerBlockInteractEvent.class, this::onBlockClick);
        MinecraftServer.getGlobalEventHandler().addListener(PlayerEntityInteractEvent.class, this::onEntityRightClick);
        MinecraftServer.getGlobalEventHandler().addListener(EntityAttackEvent.class, this::onEntityLeftClick);
        MinecraftServer.getGlobalEventHandler().addListener(PlayerPacketEvent.class, this::onPacket);
    }

    private void onBlockClick(PlayerBlockInteractEvent event) {
        ItemStack item = event.getPlayer().getItemInHand(event.getHand());
        if ( item.isAir() ) {
            return;
        }

        Material mat = item.getMaterial();
        if ( !isVehicle(mat) ) {
            return;
        }

        Direction dir = event.getBlockFace().toDirection();
        Point position = event.getBlockPosition().add(dir.normalX(), dir.normalY(), dir.normalZ());

        EntityCreature entity;
        if ( mat.name().contains("boat") ) {
            entity = new EntityCreature(EntityType.BOAT);
            BoatMeta meta = (BoatMeta) entity.getEntityMeta();
            String name = mat.name().substring(10).replace("_boat", "").toUpperCase();
            meta.setType(BoatMeta.Type.valueOf(name));
        } else {
            entity = new EntityCreature(EntityType.MINECART);
        }

        entity.setInstance(event.getPlayer().getInstance(), new Pos(position));
    }

    private void onEntityRightClick(PlayerEntityInteractEvent event) {
        if ( !isVehicle(event.getTarget().getEntityType()) ) {
            return;
        }

        event.getTarget().addPassenger(event.getPlayer());
    }

    private void onEntityLeftClick(EntityAttackEvent event) {
        if ( !(event.getEntity() instanceof Player player) ) {
            return;
        }

        if ( !isVehicle(event.getTarget().getEntityType()) ) {
            return;
        }

        if ( player.getGameMode() != GameMode.CREATIVE ) {
            return;
        }

        event.getTarget().remove();
    }

    private void onPacket(PlayerPacketEvent event) {
        if ( !(event.getPacket() instanceof ClientSteerVehiclePacket packet) ) {
            return;
        }

        if ( event.getPlayer().getVehicle() == null ) {
            return;
        }

        boolean unmount = (packet.flags() & 0x2) != 0;
        if ( !unmount ) {
            return;
        }

        event.getPlayer().getVehicle().removePassenger(event.getPlayer());
    }

    private boolean isVehicle(EntityType type) {
        return type.name().contains("boat") || type == EntityType.MINECART;
    }

    private boolean isVehicle(Material material) {
        return material.name().contains("boat") || material == Material.MINECART;
    }


}
