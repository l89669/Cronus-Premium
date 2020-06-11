package ink.ptms.cronus.event

import io.izzel.taboolib.module.event.EventCancellable
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class CronusBroadcastEvent(val players: List<Player>, var content: String) : EventCancellable<CronusBroadcastEvent>() {

    init {
        async(!Bukkit.isPrimaryThread())
    }
}