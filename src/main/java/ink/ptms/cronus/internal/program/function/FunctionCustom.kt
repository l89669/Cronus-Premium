package ink.ptms.cronus.internal.program.function

import ink.ptms.cronus.Cronus
import ink.ptms.cronus.internal.program.QuestProgram
import ink.ptms.cronus.service.custom.CustomFunction
import ink.ptms.cronus.uranus.annotations.Auto
import ink.ptms.cronus.uranus.function.Function
import ink.ptms.cronus.uranus.program.Program
import ink.ptms.cronus.util.Strumber

/**
 * @Author sky
 * @Since 2020-07-11 9:14
 */
@Auto
class FunctionCustom : Function() {

    override fun getName(): String {
        return "customfunction"
    }

    override fun eval(program: Program, vararg args: String): Any {
        if (program is QuestProgram) {
            return Cronus.getCronusService().getService(CustomFunction::class.java).get(args[0], program.player, *(1 until args.size).map { Strumber(args[it]).get() }.toTypedArray()) ?: "<null>"
        }
        return "<null>"
    }
}