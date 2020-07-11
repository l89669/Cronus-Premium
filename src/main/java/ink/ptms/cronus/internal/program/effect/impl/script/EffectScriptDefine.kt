package ink.ptms.cronus.internal.program.effect.impl.script

import ink.ptms.cronus.uranus.annotations.Auto
import ink.ptms.cronus.uranus.function.FunctionParser
import ink.ptms.cronus.uranus.program.Program
import ink.ptms.cronus.uranus.program.effect.Effect
import ink.ptms.cronus.util.Strumber
import java.util.regex.Matcher

/**
 * @Author 坏黑
 * @Since 2019-05-11 17:06
 */
@Auto
class EffectScriptDefine : Effect() {

    var name: String? = null
    var value: String? = null
    var parsed: Any? = null
    var hasFunction = false

    override fun pattern(): String {
        return "(script|js)[-.]define\\.(?<name>\\S+) (?<value>.+)"
    }

    override fun getExample(): String {
        return "script.define.[name] [value]"
    }

    override fun match(matcher: Matcher) {
        name = matcher.group("name")
        value = matcher.group("value")
        hasFunction = FunctionParser.hasFunction(value)
        if (!hasFunction) {
            parsed = Strumber(value).get()
        }
    }

    override fun eval(program: Program) {
        if (hasFunction) {
            program.scriptDefinedMap[name] = FunctionParser.parse(program, value)
        } else {
            program.scriptDefinedMap[name] = parsed
        }
    }

    override fun toString(): String {
        return "EffectScriptDefine(name=$name, value=$value)"
    }
}
