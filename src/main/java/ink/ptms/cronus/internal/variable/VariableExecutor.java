package ink.ptms.cronus.internal.variable;

import com.google.common.collect.Lists;
import ink.ptms.cronus.event.CronusVariableUpdateEvent;
import ink.ptms.cronus.util.Strumber;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;

/**
 * @Author sky
 * @Since 2019-08-17 18:41
 */
public class VariableExecutor {

    @TInject
    private static TLogger logger;

    public static void update(VariableEngine engine, String key, String symbol, String value) {
        switch (symbol) {
            case "=": {
                if (value == null || value.equalsIgnoreCase("null")) {
                    engine.reset(key);
                } else if (value.equals("[]")) {
                    engine.modify(key, Lists.newArrayList(), VariableType.LIST);
                } else {
                    Strumber stringNumber = new Strumber(value);
                    CronusVariableUpdateEvent event = new CronusVariableUpdateEvent(engine, key, stringNumber, CronusVariableUpdateEvent.Type.MODIFY).call();
                    if (event.nonCancelled()) {
                        engine.modify(event.getKey(), event.getValue().get(), event.getValue().getType().toVariableType());
                    }
                }
                break;
            }
            case "+": {
                Strumber stringNumber = new Strumber(value);
                CronusVariableUpdateEvent event = new CronusVariableUpdateEvent(engine, key, stringNumber, CronusVariableUpdateEvent.Type.INCREASE).call();
                if (event.nonCancelled()) {
                    if (!engine.increase(event.getKey(), event.getValue().get(), event.getValue().getType().toVariableType())) {
                        VariableResult result = engine.select(key);
                        logger.warn("Expression \"" + key + " " + symbol + " " + value + "\" unable to support existing variable: " + result.getValue() + " (" + result.getType() + ")");
                    }
                }
                break;
            }
            case "-": {
                Strumber stringNumber = new Strumber(value);
                CronusVariableUpdateEvent event = new CronusVariableUpdateEvent(engine, key, stringNumber, CronusVariableUpdateEvent.Type.DECREASE).call();
                if (event.nonCancelled()) {
                    if (!engine.decrease(event.getKey(), event.getValue().get(), event.getValue().getType().toVariableType())) {
                        VariableResult result = engine.select(key);
                        logger.warn("Expression \"" + key + " " + symbol + " " + value + "\" unable to support existing variable: " + result.getValue() + " (" + result.getType() + ")");
                    }
                }
                break;
            }
            default: {
                logger.warn("Cannot format symbol in \"" + key + " " + symbol + " " + value);
                break;
            }
        }
    }
}
