package ink.ptms.cronus.event;

import ink.ptms.cronus.service.dialog.DialogPack;
import io.izzel.taboolib.module.event.EventCancellable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CronusDialogEvalEvent extends EventCancellable<CronusDialogEvalEvent> {

    private final Player player;
    private DialogPack pack;

    public CronusDialogEvalEvent(Player who, DialogPack pack) {
        async(!Bukkit.isPrimaryThread());
        this.player = who;
        this.pack = pack;
    }

    public static CronusDialogEvalEvent call(DialogPack pack, Player player) {
        return new CronusDialogEvalEvent(player, pack).call();
    }

    public Player getPlayer() {
        return player;
    }

    public DialogPack getPack() {
        return pack;
    }

    public void setPack(DialogPack pack) {
        this.pack = pack;
    }
}
