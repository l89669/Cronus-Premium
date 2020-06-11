package ink.ptms.cronus.event;

import ink.ptms.cronus.database.data.DataPlayer;
import io.izzel.taboolib.module.event.EventNormal;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CronusDataPushEvent extends EventNormal<CronusDataPushEvent> {

    private final Player player;
    private final DataPlayer dataPlayer;

    public CronusDataPushEvent(Player who, DataPlayer dataPlayer) {
        async(!Bukkit.isPrimaryThread());
        this.player = who;
        this.dataPlayer = dataPlayer;
    }

    public static CronusDataPushEvent call(Player who, DataPlayer dataPlayer) {
        return new CronusDataPushEvent(who, dataPlayer).call();
    }

    public Player getPlayer() {
        return player;
    }

    public DataPlayer getDataPlayer() {
        return dataPlayer;
    }
}
