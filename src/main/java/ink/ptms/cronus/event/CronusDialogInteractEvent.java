package ink.ptms.cronus.event;

import ink.ptms.cronus.service.dialog.DialogPack;
import io.izzel.taboolib.module.event.EventCancellable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CronusDialogInteractEvent extends EventCancellable<CronusDialogInteractEvent> {

    private final Player player;
    private final Entity target;
    private DialogPack pack;

    public CronusDialogInteractEvent(Player who, DialogPack pack, Entity target) {
        async(!Bukkit.isPrimaryThread());
        this.player = who;
        this.target = target;
        this.pack = pack;
    }

    public static CronusDialogInteractEvent call(DialogPack pack, Entity target, Player player) {
        return new CronusDialogInteractEvent(player, pack, target).call();
    }

    public Player getPlayer() {
        return player;
    }

    public Entity getTarget() {
        return target;
    }

    public DialogPack getPack() {
        return pack;
    }

    public void setPack(DialogPack pack) {
        this.pack = pack;
    }
}
