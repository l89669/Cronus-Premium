package ink.ptms.cronus.event;

import ink.ptms.cronus.internal.Quest;
import io.izzel.taboolib.module.event.EventNormal;
import org.bukkit.Bukkit;

public class CronusQuestInitEvent extends EventNormal<CronusQuestInitEvent> {

    private final Quest quest;

    public CronusQuestInitEvent(Quest quest) {
        async(!Bukkit.isPrimaryThread());
        this.quest = quest;
    }

    public static CronusQuestInitEvent call(Quest quest) {
        return new CronusQuestInitEvent(quest).call();
    }

    public Quest getQuest() {
        return quest;
    }
}
