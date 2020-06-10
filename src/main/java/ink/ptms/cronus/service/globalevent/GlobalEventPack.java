package ink.ptms.cronus.service.globalevent;

import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.condition.Condition;
import ink.ptms.cronus.internal.program.QuestEffect;
import ink.ptms.cronus.internal.task.TaskCache;
import io.izzel.taboolib.module.db.local.SecuredFile;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-08-12 10:29
 */
public class GlobalEventPack {

    private final String name;
    private final Condition condition;
    private final QuestEffect effect;
    private final Map<String, Object> data = Maps.newHashMap();
    private QuestTask questTask;

    public GlobalEventPack(String name, Condition condition, QuestEffect effect) {
        this.name = name;
        this.condition = condition;
        this.effect = effect;
    }

    public boolean setupData() {
        if (!data.isEmpty()) {
            TaskCache taskCache = Cronus.getCronusService().getRegisteredTask().get(name.toLowerCase());
            if (taskCache != null) {
                YamlConfiguration yamlConfiguration = new SecuredFile();
                data.forEach((k, v) -> yamlConfiguration.set("data." + k, v));
                try {
                    questTask = taskCache.newInstance(yamlConfiguration);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "GlobalEventPack{" +
                "name='" + name + '\'' +
                ", condition=" + condition +
                ", effect=" + effect +
                ", data=" + data +
                ", questTask=" + questTask +
                '}';
    }

    // *********************************
    //
    //        Getter and Setter
    //
    // *********************************

    public String getName() {
        return name;
    }

    public Condition getCondition() {
        return condition;
    }

    public QuestEffect getEffect() {
        return effect;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public QuestTask getQuestTask() {
        return questTask;
    }
}
