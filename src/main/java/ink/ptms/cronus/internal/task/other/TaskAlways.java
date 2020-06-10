package ink.ptms.cronus.internal.task.other;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.event.EventPeriod;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.UnEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "always")
public class TaskAlways extends UnEvent {

    public TaskAlways(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, EventPeriod event) {
        return true;
    }

    @Override
    public String toString() {
        return "TaskAlways{" +
                ", id='" + id + '\'' +
                ", config=" + config +
                ", condition=" + condition +
                ", conditionRestart=" + conditionRestart +
                ", guide=" + guide +
                ", action=" + action +
                '}';
    }
}
