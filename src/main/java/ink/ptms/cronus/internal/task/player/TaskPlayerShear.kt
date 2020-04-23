package ink.ptms.cronus.internal.task.player

import ink.ptms.cronus.database.data.DataQuest
import ink.ptms.cronus.internal.bukkit.ItemStack
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser
import ink.ptms.cronus.internal.task.Task
import ink.ptms.cronus.internal.task.special.Countable
import ink.ptms.cronus.util.Utils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.entity.Sheep
import org.bukkit.event.player.PlayerShearEntityEvent

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_shear")
class TaskPlayerShear(config: ConfigurationSection) : Countable<PlayerShearEntityEvent>(config) {

    private var color: List<String>? = null
    private var entity: String? = null
    private var item: ItemStack? = null

    override fun init(data: Map<String, Any>) {
        super.init(data)
        item = if (data.containsKey("item")) BukkitParser.toItemStack(data["item"]) else null
        color = if (data.containsKey("color")) data["color"].toString().split(":").toList() else null
        entity = if (data.containsKey("entity")) data["entity"].toString() else null
    }

    override fun check(player: Player, dataQuest: DataQuest, e: PlayerShearEntityEvent): Boolean {
        if (e.entity is Sheep && color != null && color!!.none { (e.entity as Sheep).color!!.name.equals(it, true) }) {
            return false
        } else {
            return (item == null || item!!.isItem(Utils.NonNull(Utils.getUsingItem(e.player, Material.SHEARS)))) && (entity == null || entitySelector.isSelect(e.entity, entity))
        }
    }

    override fun toString(): String {
        return "TaskPlayerShear{" +
                "entity=" + entity +
                ", color=" + color +
                ", item=" + item +
                ", count=" + count +
                ", id='" + id + '\''.toString() +
                ", config=" + config +
                ", condition=" + condition +
                ", conditionRestart=" + conditionRestart +
                ", guide=" + guide +
                ", status='" + status + '\''.toString() +
                ", action=" + action +
                '}'.toString()
    }
}
