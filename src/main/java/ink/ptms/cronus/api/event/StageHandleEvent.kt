package ink.ptms.cronus.api.event

import ink.ptms.cronus.internal.QuestTask
import io.izzel.taboolib.module.event.EventNormal
import org.bukkit.entity.Player
import org.bukkit.event.Event

/**
 * @Author sky
 * @Since 2020-03-23 19:26
 */
class StageHandleEvent(val player: Player, val event: Event, private val tasks: List<*>) : EventNormal<StageHandleEvent>() {

    @Suppress("UNCHECKED_CAST")
    fun getTasks(): List<Class<out QuestTask<*>>> {
        return tasks as List<Class<out QuestTask<*>>>
    }
}