package ink.ptms.cronus.event;

import ink.ptms.cronus.database.data.DataPlayer;
import io.izzel.taboolib.module.event.EventNormal;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CronusDataPullEvent extends EventNormal<CronusDataPullEvent> {

    private final Player player;
    private final DataPlayer dataPlayer;

    public CronusDataPullEvent(Player player, DataPlayer dataPlayer) {
        async(!Bukkit.isPrimaryThread());
        this.player = player;
        this.dataPlayer = dataPlayer;
    }

    public static CronusDataPullEvent call(Player who, DataPlayer dataPlayer) {
        return new CronusDataPullEvent(who, dataPlayer).call();
    }

    public Player getPlayer() {
        return this.player;
    }

    public DataPlayer getDataPlayer() {
        return dataPlayer;
    }
}
