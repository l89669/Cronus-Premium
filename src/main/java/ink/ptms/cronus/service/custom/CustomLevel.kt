package ink.ptms.cronus.service.custom

import ink.ptms.cronus.Cronus
import ink.ptms.cronus.CronusAPI
import ink.ptms.cronus.event.CronusVariableUpdateEvent
import ink.ptms.cronus.service.Service
import ink.ptms.cronus.uranus.annotations.Auto
import io.izzel.taboolib.util.lite.Scripts
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.util.NumberConversions
import javax.script.CompiledScript
import javax.script.SimpleBindings

/**
 * @Author sky
 * @Since 2020-07-11 1:42
 */
@Auto
class CustomLevel : Service, Listener {

    val registeredLevel = HashMap<String, Level>()

    override fun init() {
        registeredLevel.clear()
        registeredLevel.putAll(Cronus.getConf().getConfigurationSection("CustomLevel")?.getKeys(false)?.map {
            val section = Cronus.getConf().getConfigurationSection("CustomLevel.$it")!!
            return@map it to Level(it, section.getInt("default"), section.getInt("maximum"), Scripts.compile(section.getString("experience")!!))
        }?.toMap() ?: emptyMap())
    }

    @EventHandler
    fun e(e: CronusVariableUpdateEvent) {

    }

    fun addExp(player: Player, id: String, value: Int) {
        val type = registeredLevel[id] ?: return
        val data = CronusAPI.getData(player).dataGlobal
        var level = data.getInt("CustomLevel.$id.level", type.def)
        if (level >= type.max) {
            return
        }
        var exp = data.getInt("CustomLevel.$id.experience") + value
        var expNextLevel = type.get(level)
        while (exp >= expNextLevel) {
            level += 1
            exp -= expNextLevel
            expNextLevel = type.get(level)
        }
        if (level >= type.max) {
            data.set("CustomLevel.$id.level", type.max)
            data.set("CustomLevel.$id.experience", expNextLevel)
        } else {
            data.set("CustomLevel.$id.level", level)
            data.set("CustomLevel.$id.experience", exp)
        }
    }

    fun setExp(player: Player, id: String, value: Int) {
        if (registeredLevel.containsKey(id)) {
            CronusAPI.getData(player).dataGlobal.set("CustomLevel.$id.experience", value)
            addExp(player, id, 0)
        }
    }

    fun getExp(player: Player, id: String): Int {
        val data = CronusAPI.getData(player).dataGlobal
        return data.getInt("CustomLevel.$id.experience")
    }

    fun addLevel(player: Player, id: String, value: Int) {
        val type = registeredLevel[id] ?: return
        val data = CronusAPI.getData(player).dataGlobal
        data.set("CustomLevel.$id.level", (data.getInt("CustomLevel.$id.level", type.def) + value).coerceAtLeast(type.def).coerceAtMost(type.max))
        addExp(player, id, 0)
    }

    fun setLevel(player: Player, id: String, value: Int) {
        val type = registeredLevel[id] ?: return
        val data = CronusAPI.getData(player).dataGlobal
        data.set("CustomLevel.$id.level", value.coerceAtLeast(type.def).coerceAtMost(type.max))
        addExp(player, id, 0)
    }

    fun getLevel(player: Player, id: String): Int {
        val type = registeredLevel[id] ?: return 0
        val data = CronusAPI.getData(player).dataGlobal
        return data.getInt("CustomLevel.$id.level", type.def)
    }

    class Level(val id: String, val def: Int, val max: Int, val experience: CompiledScript) {

        fun get(level: Int): Int {
            return NumberConversions.toInt(experience.eval(SimpleBindings(mapOf("level" to level))))
        }
    }
}