package ink.ptms.cronus.database.data.item.hook

import ink.ptms.cronus.database.data.item.ItemStorage
import ink.ptms.zaphkiel.ZaphkielAPI
import org.bukkit.inventory.ItemStack

/**
 * @Author 坏黑
 * @Since 2019-05-28 23:38
 */
class StorageZaphkiel : ItemStorage() {

    override fun depend(): String {
        return "Zaphkiel"
    }

    override fun getItem(name: String): ItemStack? {
        return ZaphkielAPI.getItem(name)?.save()
    }

    override fun addItem(name: String, itemStack: ItemStack) {
    }

    override fun delItem(name: String) {
    }

    override fun getItems(): List<String> {
        return ZaphkielAPI.registeredItem.keys.toList()
    }
}