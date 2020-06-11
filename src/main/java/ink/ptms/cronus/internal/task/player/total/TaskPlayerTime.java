package ink.ptms.cronus.internal.task.player.total;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.Uncountable;
import ink.ptms.cronus.util.Sxpression;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_time")
public class TaskPlayerTime extends Uncountable<Event> {

    private Sxpression time;

    public TaskPlayerTime(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        time = data.containsKey("time") ? new Sxpression(data.get("time")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, Event e) {
        return time == null || time.isSelect(getCount(player, dataQuest, e));
    }

    @Override
    public double getCount(Player player, DataQuest dataQuest, Event e) {
        return 1;
    }

    @Override
    public String toString() {
        return "TaskPlayerTime{" +
                "time=" + time +
                ", total=" + total +
                ", id='" + id + '\'' +
                ", config=" + config +
                ", condition=" + condition +
                ", conditionRestart=" + conditionRestart +
                ", guide=" + guide +
                ", status='" + status + '\'' +
                ", action=" + action +
                '}';
    }
}
