package ink.ptms.cronus.builder.task.other;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.other.TaskAlways;
import ink.ptms.cronus.uranus.annotations.Auto;
import io.izzel.taboolib.util.item.ItemBuilder;
import io.izzel.taboolib.util.lite.Materials;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * @Author 坏黑
 * @Since 2019-06-22 22:19
 */
@Auto
public class Always extends TaskEntry {

    public Always() {
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Materials.TRIPWIRE_HOOK.parseMaterial()).name("§f始终触发").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask<?>> getTask() {
        return TaskAlways.class;
    }
}
