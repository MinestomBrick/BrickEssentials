package org.minestombrick.essentials.app.commands;

import org.minestombrick.commandtools.api.command.BrickCommand;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import org.minestombrick.i18n.api.I18nAPI;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class TimeSpeedCommand extends BrickCommand {

    public TimeSpeedCommand() {
        super("timespeed");

        setCondition(b -> b.permission("brickessentials.timespeed"));

        // invalid usage
        setInvalidUsageMessage("cmd.timecycle.usage");

        // arguments
        ArgumentNumber<Integer> timeSpeedArg = ArgumentType.Integer("timeSpeed").between(0, 1000);
        setInvalidArgumentMessage(timeSpeedArg, "cmd.error.args.number");

        addSyntax((sender, context) -> {
            int speed = context.get(timeSpeedArg);
            Instance instance = ((Player) sender).getInstance();

            instance.setTimeRate(speed);

            if ( speed == 0 ) {
                instance.setTimeUpdate(Duration.of(1, ChronoUnit.CENTURIES));
            } else {
                instance.setTimeUpdate(Duration.of((int) (1000 * (1f / speed)), ChronoUnit.MILLIS));
            }

            I18nAPI.get(this).send(sender, "cmd.timespeed", speed);
        }, timeSpeedArg);

    }

}
