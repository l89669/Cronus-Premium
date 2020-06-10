package ink.ptms.cronus.command;

import ink.ptms.cronus.internal.api.Helper;
import ink.ptms.cronus.util.Utils;
import io.izzel.taboolib.module.command.base.BaseMainCommand;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.lite.cooldown.Cooldown;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
