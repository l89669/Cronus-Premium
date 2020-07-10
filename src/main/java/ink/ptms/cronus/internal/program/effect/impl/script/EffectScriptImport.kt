package ink.ptms.cronus.internal.program.effect.impl.script

import com.google.common.collect.Maps
import ink.ptms.cronus.uranus.annotations.Auto
import ink.ptms.cronus.uranus.program.Program
import ink.ptms.cronus.uranus.program.effect.Effect

import java.util.regex.Matcher

/**
 * @Author 坏黑
 * @Since 2019-05-11 17:06
 */
@Auto
class EffectScriptImport : Effect() {

    var import: String? = null

    override fun pattern(): String {
        return "(script|js)[-.]import (?<class>.+)"
    }

    override fun getExample(): String {
        return "script.import [class]"
    }

    override fun match(matcher: Matcher) {
        import = matcher.group("class")
    }

    override fun eval(program: Program) {
        if (API.imports.containsKey(import)) {
            return
        }
        try {
            API.imports[import] = Class.forName(import)
        } catch (ignored: Throwable) {
        }
    }

    override fun toString(): String {
        return "EffectScriptImport(import=$import)"
    }

    object API {

        var imports = Maps.newHashMap<String, Any>()!!
    }
}
