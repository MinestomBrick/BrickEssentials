package com.gufli.brickessentials;

import com.gufli.brickessentials.commands.*;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.extensions.Extension;

import java.util.HashSet;
import java.util.Set;

public class BrickEssentials extends Extension {

    private final Set<Command> commands = new HashSet<>();

    private String nameAndVersion() {
        return getOrigin().getName() + " v" + getOrigin().getVersion();
    }

    @Override
    public void initialize() {
        getLogger().info("Enabling " + nameAndVersion() + ".");

        // register commands
        commands.add(new GamemodeCommand());
        commands.add(new TeleportCommand());
        commands.add(new TeleportHereCommand());
        commands.add(new RepairCommand());
        commands.add(new KillCommand());
        commands.add(new KickCommand());

        CommandManager commandManager = MinecraftServer.getCommandManager();
        commands.forEach(commandManager::register);

        getLogger().info("Enabled " + nameAndVersion() + ".");
    }

    @Override
    public void terminate() {
        // unregister commands
        CommandManager commandManager = MinecraftServer.getCommandManager();
        commands.forEach(commandManager::unregister);
        commands.clear();

        getLogger().info("Disabled " + nameAndVersion() + ".");
    }

}
