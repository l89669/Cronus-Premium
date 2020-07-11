package ink.ptms.cronus.uranus.program.effect;

import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.FunctionParser;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.util.Strumber;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 15:24
 */
@Auto
public class EffectReturn extends Effect {

    private String value;
    private Object parsed;
    private boolean hasFunction;

    @Override
    public String pattern() {
        return "^return (?<value>.+)";
    }

    @Override
    public String getExample() {
        return "return [value]";
    }

    @Override
    public void match(Matcher matcher) {
        value = matcher.group("value");
        hasFunction = FunctionParser.hasFunction(value);
        if (!hasFunction) {
            parsed = new Strumber(value).get();
        }
    }

    @Override
    public void eval(Program program) {
        if (hasFunction) {
            program.setResult(FunctionParser.parse(program, value));
        } else {
            program.setResult(parsed);
        }
    }

}
