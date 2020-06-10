package ink.ptms.cronus.builder.task.total;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.expression.ExpressionTotal;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.player.total.TaskPlayerTime;
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
public class PlayerTime extends TaskEntry {

    public PlayerTime() {
        objective.add(ExpressionTotal.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Materials.CLOCK.parseMaterial()).name("§f在线时间 (分钟)").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskPlayerTime.class;
    }
}
