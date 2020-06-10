package ink.ptms.cronus.builder.task.player;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.Count;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.player.TaskPlayerWalkFrost;
import io.izzel.taboolib.util.lite.Materials;
import ink.ptms.cronus.uranus.annotations.Auto;
import io.izzel.taboolib.util.item.ItemBuilder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * @Author 坏黑
 * @Since 2019-06-22 22:19
 */
@Auto
public class PlayerWalkFrost extends TaskEntry {

    public PlayerWalkFrost() {
        objective.add(Count.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Materials.ICE.parseMaterial()).name("§f冰霜行者触发").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskPlayerWalkFrost.class;
    }
}
