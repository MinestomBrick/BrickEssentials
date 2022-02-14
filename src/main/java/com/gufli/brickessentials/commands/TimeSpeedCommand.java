package com.gufli.brickessentials.commands;

import com.gufli.brickutils.commands.BrickCommand;
import com.gufli.brickutils.translation.TranslationAPI;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.minestom.server.entity.Player;

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
            ((Player) sender).getInstance().setTimeRate(speed);
            ((Player) sender).getInstance().setTimeUpdate(Duration.of((int) (1000 * (1f / speed)), ChronoUnit.MILLIS));
            TranslationAPI.get().send(sender, "cmd.timespeed", speed);
        }, timeSpeedArg);

    }

}
