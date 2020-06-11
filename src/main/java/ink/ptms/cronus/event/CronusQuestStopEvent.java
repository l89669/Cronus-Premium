package ink.ptms.cronus.event;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.Quest;
import io.izzel.taboolib.module.event.EventNormal;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CronusQuestStopEvent extends EventNormal<CronusQuestStopEvent> {

    private final Player player;
    private final Quest quest;

    public CronusQuestStopEvent(Player who, Quest quest) {
        async(!Bukkit.isPrimaryThread());
        this.player = who;
        this.quest = quest;
    }

    public static CronusQuestStopEvent call(Player who, Quest quest) {
        return new CronusQuestStopEvent(who, quest).call();
    }

    public Player getPlayer() {
        return player;
    }

    public Quest getQuest() {
        return quest;
    }

    public DataQuest getDataQuest() {
        return CronusAPI.getData(player).getQuest(quest.getId());
    }
}
