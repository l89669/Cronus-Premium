package ink.ptms.cronus.service.questitem

import ink.ptms.cronus.Cronus
import io.izzel.taboolib.module.inject.TListener
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerSwapHandItemsEvent

/**
 * @Author 坏黑
 * @Since 2019-07-23 14:51
 */
@TListener(version = ">10900")
class QuestItem10900 : Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun e(e: PlayerSwapHandItemsEvent) {
        val questItem = Cronus.getCronusService().getService(QuestItem::class.java)
        if (questItem.items.any { it.item.isSimilar(e.offHandItem) || it.item.isSimilar(e.mainHandItem) }) {
            e.isCancelled = true
        }
    }
}