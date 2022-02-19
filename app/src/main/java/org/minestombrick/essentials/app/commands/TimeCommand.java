package org.minestombrick.essentials.app.commands;

import org.minestombrick.commandtools.api.command.BrickCommand;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.minestom.server.entity.Player;
import org.minestombrick.i18n.api.I18nAPI;

public class TimeCommand extends BrickCommand {

    public TimeCommand() {
        super("time");

        setCondition(b -> b.permission("brickessentials.time"));

        // invalid usage
        setInvalidUsageMessage("cmd.time.usage");

        // arguments
        ArgumentEnum<Time> timeEnumArg = ArgumentType.Enum("timeEnum", Time.class);
        setInvalidArgumentMessage(timeEnumArg, "cmd.error.args.time");

        ArgumentNumber<Long> timeLongArg = ArgumentType.Long("timeLong").between((long) 0, (long) 24000);
        setInvalidArgumentMessage(timeLongArg, "cmd.error.args.number");

        addSyntax((sender, context) -> {
            Time time = context.get(timeEnumArg);
            ((Player) sender).getInstance().setTime(time.time());
            I18nAPI.get(this).send(sender, "cmd.time", time);
        }, timeEnumArg);

        addSyntax((sender, context) -> {
            long time = context.get(timeLongArg);
            ((Player) sender).getInstance().setTime(time);
            I18nAPI.get(this).send(sender, "cmd.time", time);
        }, timeLongArg);
    }

    private enum Time{
        SUNRISE(0),
        NOON(6000),
        SUNSET(12000),
        MIDNIGHT(18000);

        final long time;

        private Time(int time) {
            this.time = time;
        }

        public long time() {
            return time;
        }
    }

}
