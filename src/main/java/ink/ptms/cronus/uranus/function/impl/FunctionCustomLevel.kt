package ink.ptms.cronus.uranus.function.impl

import ink.ptms.cronus.Cronus
import ink.ptms.cronus.service.custom.CustomLevel
import ink.ptms.cronus.uranus.annotations.Auto
import ink.ptms.cronus.uranus.function.Function
import ink.ptms.cronus.uranus.program.Program

/**
 * @Author 坏黑
 * @Since 2019-05-11 13:16
 */
@Auto
class FunctionCustomLevel : Function() {

    override fun getName(): String {
        return "customlevel"
    }

    override fun eval(program: Program, vararg args: String): Any {
        if (args.size == 1) {
            return "<invalid.argument>"
        }
        val service = Cronus.getCronusService().getService(CustomLevel::class.java)
        return when (args[0].toLowerCase()) {
            "level.current" -> service.getLevel(program.asPlayer(), args[1])
            "level.def", "level.default" -> service.registeredLevel[args[1]]?.def ?: -1
            "level.max", "level.maximum" -> service.registeredLevel[args[1]]?.max ?: -1
            "exp.current", "expression.current" -> service.getExp(program.asPlayer(), args[1])
            "exp.max", "expression.max" -> service.registeredLevel[args[1]]?.get(service.getLevel(program.asPlayer(), args[1])) ?: -1
            else -> "<invalid>"
        }
    }
}