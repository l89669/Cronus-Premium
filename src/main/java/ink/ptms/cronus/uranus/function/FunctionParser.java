package ink.ptms.cronus.uranus.function;

import ink.ptms.cronus.uranus.program.Program;

/**
 * @Author 坏黑
 * @Since 2019-05-11 13:12
 */
public class FunctionParser {

    public static boolean hasFunction(String in) {
        return contains(in, '{') && contains(in, '}') && contains(in, ':');
    }

    public static Object parse(Program program, String in) {
        Object parsed = hasFunction(in) ? parseFirst(program, in) : in;
        while (hasFunction(parsed.toString())) {
            parsed = parseFirst(program, parsed);
        }
        return parsed;
    }

    @Deprecated
    public static String parseAll(Program program, String in) {
        return parse(program, in).toString();
    }

    public static Object parseFirst(Program program, Object in) {
        Result result = check(in.toString());
        if (result == null) {
            return in;
        }
        String name = result.getCenter().substring(0, result.getCenter().indexOf(":"));
        String argument = result.getCenter().substring(result.getCenter().indexOf(":") + 1);
        Function function = FunctionLoader.getFunction(name);
        Object output = function == null ? "<Null.Function>" : function.eval(program, function.allowArguments() ? argument.split("\\|") : new String[] {argument});
        return result.isEmpty() ? output : result.replace(String.valueOf(output));
    }

    public static Result check(String in) {
        char[] chars = in.toCharArray();
        int index = -1;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '{') {
                index = i;
            } else if (chars[i] == '}') {
                String full = in.substring(index + 1, i);
                return new Result(in.substring(0, index), full, in.substring(i + 1));
            }
        }
        return null;
    }

    public static String checkFully(String in, Replacer replacer) {
        Result result;
        while ((result = check(in)) != null) {
            in = result.replace(replacer.eval(result.getCenter()));
        }
        return in;
    }

    public static boolean contains(String in, char var) {
        char[] chars = in.toCharArray();
        for (char c : chars) {
            if (c == var) {
                return true;
            }
        }
        return false;
    }
}
