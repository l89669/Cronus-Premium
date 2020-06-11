package ink.ptms.cronus.event;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataPlayer;
import io.izzel.taboolib.module.event.EventNormal;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CronusVisibleToggleEvent extends EventNormal<CronusVisibleToggleEvent> {

    private final Player player;

    public CronusVisibleToggleEvent(Player who) {
        async(!Bukkit.isPrimaryThread());
        this.player = who;
    }

    public static CronusVisibleToggleEvent call(Player who) {
        return new CronusVisibleToggleEvent(who).call();
    }

    public Player getPlayer() {
        return player;
    }

    public DataPlayer getDataPlayer() {
        return CronusAPI.getData(player);
    }
}
