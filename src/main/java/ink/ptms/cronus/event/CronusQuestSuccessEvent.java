package ink.ptms.cronus.event;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.Quest;
import io.izzel.taboolib.module.event.EventNormal;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CronusQuestSuccessEvent extends EventNormal<CronusQuestSuccessEvent> {

    private final Player player;
    private final Quest quest;

    public CronusQuestSuccessEvent(Player who, Quest quest) {
        async(!Bukkit.isPrimaryThread());
        this.player = who;
        this.quest = quest;
    }

    public static CronusQuestSuccessEvent call(Player who, Quest quest) {
        return new CronusQuestSuccessEvent(who, quest).call();
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
