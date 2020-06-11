package ink.ptms.cronus.event;

import ink.ptms.cronus.internal.QuestBook;
import io.izzel.taboolib.module.event.EventNormal;
import org.bukkit.Bukkit;

public class CronusInitQuestBookEvent extends EventNormal<CronusInitQuestBookEvent> {

    private final QuestBook quest;

    public CronusInitQuestBookEvent(QuestBook quest) {
        async(!Bukkit.isPrimaryThread());
        this.quest = quest;
    }

    public static CronusInitQuestBookEvent call(QuestBook quest) {
        return new CronusInitQuestBookEvent(quest).call();
    }

    public QuestBook getQuestStage() {
        return quest;
    }
}
