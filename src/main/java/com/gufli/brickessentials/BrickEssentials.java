package com.gufli.brickessentials;

import com.gufli.brickessentials.commands.*;
import com.gufli.brickessentials.listeners.VehicleListener;
import com.gufli.brickessentials.listeners.SpawnEggListener;
import com.gufli.brickutils.translation.SimpleTranslationManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.extensions.Extension;

import java.util.Locale;

public class BrickEssentials extends Extension {

    @Override
    public void initialize() {
        getLogger().info("Enabling " + nameAndVersion() + ".");

        SimpleTranslationManager tm = new SimpleTranslationManager(this, Locale.ENGLISH);
        tm.loadTranslations(this, "languages");

        // register commands
        CommandManager commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new GamemodeCommand());
        commandManager.register(new TeleportCommand());
        commandManager.register(new TeleportHereCommand());
        commandManager.register(new RepairCommand());
        commandManager.register(new KillCommand());
        commandManager.register(new KickCommand());
        commandManager.register(new TimeCommand());
        commandManager.register(new TimeSpeedCommand());

        // register events
        new VehicleListener();
        new SpawnEggListener();

        getLogger().info("Enabled " + nameAndVersion() + ".");
    }

    @Override
    public void terminate() {
        getLogger().info("Disabled " + nameAndVersion() + ".");
    }

    private String nameAndVersion() {
        return getOrigin().getName() + " v" + getOrigin().getVersion();
    }

}
