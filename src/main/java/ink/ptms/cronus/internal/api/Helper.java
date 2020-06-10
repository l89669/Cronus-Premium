package ink.ptms.cronus.internal.api;

import ink.ptms.cronus.util.Utils;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.lite.cooldown.Cooldown;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @Author sky
 * @Since 2020-06-10 17:51
 */
public interface Helper {

    static void normal(CommandSender sender, String args) {
        sender.sendMessage("§7§l[§f§lCronus§7§l] §7" + TLocale.Translate.setColored(args));
        // 音效
        if (sender instanceof Player && !Utils.isActionCooldown(sender)) {
            ((Player) sender).playSound(((Player) sender).getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 2f);
        }
    }

    static void error(CommandSender sender, String args) {
        sender.sendMessage("§c§l[§4§lCronus§c§l] §c" + TLocale.Translate.setColored(args));
        // 音效
        if (sender instanceof Player && !Utils.isActionCooldown(sender)) {
            ((Player) sender).playSound(((Player) sender).getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
        }
    }
}
