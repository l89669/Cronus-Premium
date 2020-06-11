package ink.ptms.cronus.event;

import ink.ptms.cronus.internal.QuestTask;
import io.izzel.taboolib.module.event.EventNormal;
import org.bukkit.Bukkit;

public class CronusInitQuestTaskEvent extends EventNormal<CronusInitQuestTaskEvent> {

    private final QuestTask<?> quest;

    public CronusInitQuestTaskEvent(QuestTask<?> quest) {
        async(!Bukkit.isPrimaryThread());
        this.quest = quest;
    }

    public static CronusInitQuestTaskEvent call(QuestTask<?> quest) {
        return new CronusInitQuestTaskEvent(quest).call();
    }

    public QuestTask<?> getQuestStage() {
        return quest;
    }
}
