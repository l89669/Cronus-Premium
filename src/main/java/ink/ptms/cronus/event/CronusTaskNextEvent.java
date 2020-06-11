package ink.ptms.cronus.event;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.Quest;
import ink.ptms.cronus.internal.QuestStage;
import ink.ptms.cronus.internal.QuestTask;
import io.izzel.taboolib.module.event.EventCancellable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CronusTaskNextEvent extends EventCancellable<CronusTaskNextEvent> {

    private final Player player;
    private final Quest quest;
    private final QuestStage questStage;
    private final QuestTask<?> questTask;

    public CronusTaskNextEvent(Player who, Quest quest, QuestStage questStage, QuestTask<?> questTask) {
        async(!Bukkit.isPrimaryThread());
        this.player = who;
        this.quest = quest;
        this.questStage = questStage;
        this.questTask = questTask;
    }

    public static CronusTaskNextEvent call(Player who, Quest quest, QuestStage questStage, QuestTask<?> questTask) {
        return new CronusTaskNextEvent(who, quest, questStage, questTask).call();
    }

    public Player getPlayer() {
        return player;
    }

    public Quest getQuest() {
        return quest;
    }

    public QuestStage getQuestStage() {
        return questStage;
    }

    public QuestTask<?> getQuestTask() {
        return questTask;
    }
    
    public DataQuest getDataQuest() {
        return CronusAPI.getData(player).getQuest(quest.getId());
    }
}
