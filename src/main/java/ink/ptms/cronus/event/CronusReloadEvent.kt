package ink.ptms.cronus.event

import io.izzel.taboolib.module.event.EventNormal
import org.bukkit.Bukkit

class CronusReloadEvent : EventNormal<CronusReloadEvent>() {

    init {
        async(!Bukkit.isPrimaryThread())
    }
}