package ink.ptms.cronus.event;

import ink.ptms.cronus.internal.QuestStage;
import io.izzel.taboolib.module.event.EventNormal;
import org.bukkit.Bukkit;

public class CronusInitQuestStageEvent extends EventNormal<CronusInitQuestStageEvent> {

    private final QuestStage quest;

    public CronusInitQuestStageEvent(QuestStage quest) {
        async(!Bukkit.isPrimaryThread());
        this.quest = quest;
    }

    public static CronusInitQuestStageEvent call(QuestStage quest) {
        return new CronusInitQuestStageEvent(quest).call();
    }

    public QuestStage getQuestStage() {
        return quest;
    }
}
