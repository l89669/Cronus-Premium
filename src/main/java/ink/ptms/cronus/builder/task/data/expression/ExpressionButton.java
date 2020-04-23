package ink.ptms.cronus.builder.task.data.expression;

import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.builder.task.TaskExpression;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-07-01 14:34
 */
public class ExpressionButton extends TaskExpression {

    public ExpressionButton(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public String getDisplay() {
        return "按钮";
    }

    @Override
    public Material getMaterial() {
        return Material.STONE_BUTTON;
    }

    @Override
    public String getKey() {
        return "button";
    }

    @Override
    public Object defaultValue() {
        return null;
    }
}
