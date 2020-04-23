package ink.ptms.cronus.internal.task.special

import ink.ptms.cronus.database.data.DataQuest

/**
 * @Author sky
 * @Since 2020-04-07 20:35
 */
interface Damageable {

    fun getDamage(dataQuest: DataQuest): Double
}