package ink.ptms.cronus.service.status

import com.google.common.base.Enums
import ink.ptms.cronus.Cronus
import ink.ptms.cronus.database.data.DataQuest
import ink.ptms.cronus.event.CronusReloadServiceEvent
import ink.ptms.cronus.event.CronusTaskNextEvent
import ink.ptms.cronus.internal.QuestTask
import ink.ptms.cronus.internal.program.QuestProgram
import ink.ptms.cronus.internal.task.player.damage.TaskPlayerAttack
import ink.ptms.cronus.internal.task.player.damage.TaskPlayerDamaged
import ink.ptms.cronus.internal.task.special.Countable
import ink.ptms.cronus.internal.task.special.Uncountable
import ink.ptms.cronus.service.Service
import ink.ptms.cronus.uranus.annotations.Auto
import ink.ptms.cronus.uranus.function.FunctionParser
import io.izzel.taboolib.internal.apache.lang3.tuple.MutablePair
import io.izzel.taboolib.module.inject.TSchedule
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.lite.SoundPack
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.util.NumberConversions
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * @Author 坏黑
 * @Since 2019-07-21 14:40
 */
@Auto
class Status : Service, Listener {

    var type: StatusType? = null
    var barColor: BarColor? = null
    var barStyle: BarStyle? = null
    var sound: SoundPack? = null

    companion object {

        val barMap = ConcurrentHashMap<String, MutablePair<BossBar, Long>>()

        @TSchedule(period = 20)
        fun remove() {
            barMap.filterValues { System.currentTimeMillis() > it.right }.forEach { (k, _) -> barMap.remove(k)!!.left.removeAll() }
        }
    }

    override fun init() {
        type = Enums.getIfPresent(StatusType::class.java, Cronus.getConf().getString("Status.type", "")!!.toUpperCase()).or(StatusType.NONE)
        if (type == StatusType.BOSSBAR) {
            barColor = Enums.getIfPresent(BarColor::class.java, Cronus.getConf().getString("Status.bossbar.color", "")!!.toUpperCase()).or(BarColor.BLUE)
            barStyle = Enums.getIfPresent(BarStyle::class.java, Cronus.getConf().getString("Status.bossbar.style", "")!!.toUpperCase()).or(BarStyle.SEGMENTED_20)
        }
        sound = SoundPack(Cronus.getConf().getString("Status.sound"))
        CronusReloadServiceEvent.call(this)
    }

    @EventHandler
    fun e(e: CronusTaskNextEvent) {
        Bukkit.getScheduler().runTask(Cronus.getInst()) {
            if (!e.isCancelled && e.questTask.status != "<no-status>") {
                display(e.player, e.dataQuest, e.questTask)
            }
        }
    }

    fun display(player: Player, dataQuest: DataQuest, questTask: QuestTask<*>) {
        val questProgram = QuestProgram(player, dataQuest)
        val parsed = FunctionParser.parseAll(questProgram, TLocale.Translate.setColored(questTask.status))
        when (type) {
            StatusType.BOSSBAR -> {
                val newBar = Bukkit.createBossBar(parsed, barColor!!, barStyle!!)
                if (questTask.statusInput != null) {
                    val current = NumberConversions.toDouble(FunctionParser.parseAll(questProgram, questTask.statusInput.key))
                    val max = NumberConversions.toDouble(FunctionParser.parseAll(questProgram, questTask.statusInput.value))
                    newBar.progress = (current / max).coerceAtMost(1.0)
                } else if (questTask is TaskPlayerAttack) {
                    newBar.progress = questTask.getDamage(dataQuest) / questTask.damage
                } else if (questTask is TaskPlayerDamaged) {
                    newBar.progress = questTask.getDamage(dataQuest) / questTask.damage
                } else if (questTask is Countable<*>) {
                    newBar.progress = questTask.getCount(dataQuest).toDouble() / questTask.countMax
                } else if (questTask is Uncountable<*>) {
                    val total = questTask.total
                    if (total == null) {
                        newBar.progress = if (questTask.isCompleted(dataQuest)) 1.0 else 0.5
                    } else {
                        val totalNumber = total.number.number.toDouble()
                        val totalCurrent = questTask.getTotal(dataQuest)
                        if (totalNumber >= 0) {
                            newBar.progress = (totalCurrent / totalNumber).coerceAtMost(1.0)
                        } else {
                            newBar.progress = if (totalCurrent > 0) 0.0 else totalCurrent * -1 / (totalNumber * -1).coerceAtMost(1.0)
                        }
                    }
                } else {
                    newBar.progress = if (questTask.isCompleted(dataQuest)) 1.0 else 0.5
                }
                val id = "${player.name}.${dataQuest.currentQuest}.${dataQuest.currentStage}.${questTask.id}"
                if (barMap.containsKey(id)) {
                    barMap[id]!!.left.setTitle(newBar.title)
                    barMap[id]!!.left.progress = newBar.progress
                    barMap[id]!!.right = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10)
                } else {
                    newBar.isVisible = true
                    newBar.addPlayer(player)
                    barMap[id] = MutablePair.of(newBar, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10))
                }
            }
            StatusType.ACTIONBAR -> TLocale.Display.sendActionBar(player, parsed)
            StatusType.TITLE -> TLocale.Display.sendTitle(player, "", parsed, 5, 40, 5)
            else -> {
            }
        }
        if (Cronus.getConf().getStringList("Status.sound-disabled").none { dataQuest.quest!!.bookTag.contains(it) }) {
            sound!!.play(player)
        }
    }
}
