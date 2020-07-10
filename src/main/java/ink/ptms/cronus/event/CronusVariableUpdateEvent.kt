package ink.ptms.cronus.event

import ink.ptms.cronus.internal.variable.VariableEngine
import ink.ptms.cronus.util.Strumber
import io.izzel.taboolib.module.event.EventCancellable
import org.bukkit.Bukkit

/**
 * @Author sky
 * @Since 2020-07-11 1:47
 */
class CronusVariableUpdateEvent(
        val engine: VariableEngine,
        var key: String,
        val value: Strumber,
        val type: Type
) : EventCancellable<CronusVariableUpdateEvent>() {

    init {
        async(!Bukkit.isPrimaryThread())
    }

    enum class Type {

        MODIFY, INCREASE, DECREASE
    }
}