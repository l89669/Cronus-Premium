package ink.ptms.cronus.event;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.Quest;
import io.izzel.taboolib.module.event.EventCancellable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CronusQuestAcceptEvent extends EventCancellable<CronusQuestAcceptEvent> {

    private final Player player;
    private Quest quest;

    public CronusQuestAcceptEvent(Player who, Quest quest) {
        async(!Bukkit.isPrimaryThread());
        this.player = who;
        this.quest = quest;
    }

    public static CronusQuestAcceptEvent call(Player who, Quest quest) {
        return new CronusQuestAcceptEvent(who, quest).call();
    }

    public Player getPlayer() {
        return player;
    }

    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public DataQuest getDataQuest() {
        return CronusAPI.getData(player).getQuest(quest.getId());
    }
}
