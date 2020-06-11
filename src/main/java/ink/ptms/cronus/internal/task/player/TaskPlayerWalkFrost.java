package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.Countable;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.EntityBlockFormEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_walk_frost")
public class TaskPlayerWalkFrost extends Countable<EntityBlockFormEvent> {

    public TaskPlayerWalkFrost(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, EntityBlockFormEvent e) {
        return e.getBlock().getType().equals(Material.ICE);
    }

    @Override
    public String toString() {
        return "TaskPlayerWalkFrost{" +
                "count=" + count +
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

