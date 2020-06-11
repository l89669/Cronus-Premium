package ink.ptms.cronus.command;

import ink.ptms.cronus.internal.api.Helper;
import io.izzel.taboolib.module.command.base.BaseMainCommand;
import org.bukkit.command.CommandSender;

/**
 * @Author 坏黑
 * @Since 2019-05-31 21:59
 */
public abstract class CronusCommand extends BaseMainCommand implements Helper {

    @Deprecated
    public static void normal(CommandSender sender, String args) {
        Helper.normal(sender, args);
    }

    @Deprecated
    public static void error(CommandSender sender, String args) {
        Helper.error(sender, args);
    }
}
