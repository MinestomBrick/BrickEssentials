package org.minestombrick.essentials.app;

import org.minestombrick.essentials.app.commands.*;
import org.minestombrick.essentials.app.listeners.SpawnEggListener;
import org.minestombrick.essentials.app.listeners.VehicleListener;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.extensions.Extension;
import org.minestombrick.i18n.api.I18nAPI;
import org.minestombrick.i18n.api.namespace.I18nNamespace;

import java.util.Locale;

public class BrickEssentials extends Extension {

    @Override
    public void initialize() {
        getLogger().info("Enabling " + nameAndVersion() + ".");

        I18nNamespace namespace = new I18nNamespace(this, Locale.ENGLISH);
        namespace.loadValues(this, "languages");
        I18nAPI.get().register(namespace);

        // register commands
        CommandManager commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new GamemodeCommand());
        commandManager.register(new TeleportCommand());
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
