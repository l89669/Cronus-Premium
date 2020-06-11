package ink.ptms.cronus.event;

import ink.ptms.cronus.service.dialog.DialogGroup;
import io.izzel.taboolib.module.event.EventNormal;
import org.bukkit.Bukkit;

public class CronusInitDialogEvent extends EventNormal<CronusInitDialogEvent> {

    private final DialogGroup dialogGroup;

    public CronusInitDialogEvent(DialogGroup dialogGroup) {
        async(!Bukkit.isPrimaryThread());
        this.dialogGroup = dialogGroup;
    }

    public static CronusInitDialogEvent call(DialogGroup dialogGroup) {
        return new CronusInitDialogEvent(dialogGroup).call();
    }

    public DialogGroup getDialogGroup() {
        return dialogGroup;
    }
}
