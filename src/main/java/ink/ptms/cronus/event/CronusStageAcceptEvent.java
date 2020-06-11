package ink.ptms.cronus.event;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.Quest;
import ink.ptms.cronus.internal.QuestStage;
import io.izzel.taboolib.module.event.EventNormal;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CronusStageAcceptEvent extends EventNormal<CronusStageAcceptEvent> {

    private final Player player;
    private final Quest quest;
    private final QuestStage questStage;

    public CronusStageAcceptEvent(Player who, Quest quest, QuestStage questStage) {
        async(!Bukkit.isPrimaryThread());
        this.player = who;
        this.quest = quest;
        this.questStage = questStage;
    }

    public static CronusStageAcceptEvent call(Player who, Quest quest, QuestStage stage) {
       return new CronusStageAcceptEvent(who, quest, stage).call();
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

    public DataQuest getDataQuest() {
        return CronusAPI.getData(player).getQuest(quest.getId());
    }
}
