package ink.ptms.cronus.internal.task.listener;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.internal.event.EventPeriod;
import ink.ptms.cronus.internal.task.other.*;
import ink.ptms.cronus.internal.task.player.total.TaskPlayerTime;
import io.izzel.taboolib.module.inject.TSchedule;
import org.bukkit.Bukkit;

/**
 * @Author 坏黑
 * @Since 2019-06-14 17:48
 */
public class PeriodTask {

    static EventPeriod event = new EventPeriod();

    @TSchedule(period = 20, async = true)
    static void check20() {
        Bukkit.getOnlinePlayers().forEach(player -> CronusAPI.stageHandle(player, event, TaskLocation.class, TaskPermission.class, TaskPlayerVal.class, TaskPlayerVar.class, TaskQuestVal.class, TaskQuestVar.class));
    }

    @TSchedule(period = 1200, async = true)
    static void check60() {
        Bukkit.getOnlinePlayers().forEach(player -> CronusAPI.stageHandle(player, event, TaskPlayerTime.class));
    }
}
