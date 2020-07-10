package ink.ptms.cronus.internal.program;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.program.effect.impl.EffectDelay;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author 坏黑
 * @Since 2019-05-26 22:38
 */
public class QuestProgram extends Program {

    private final AtomicInteger delay = new AtomicInteger(0);
    private final DataQuest dataQuest;
    private Event event;
    private boolean cancel;

    public QuestProgram(Player player, DataQuest dataQuest) {
        this.dataQuest = dataQuest;
        this.sender = player;
    }

    public QuestProgram(Player player, DataQuest dataQuest, Event event) {
        this.dataQuest = dataQuest;
        this.sender = player;
        this.event = event;
    }

    public void eval(List<Effect> effects) {
        delay.set(0);
        for (Effect effect : effects) {
            if (delay.get() == 0 || effect instanceof EffectDelay) {
                if (!cancel) {
                    eval(effect);
                }
            } else {
                Bukkit.getScheduler().runTaskLater(Cronus.getInst(), new Runnable() {

                    @Override
                    public void run() {
                        if (!cancel) {
                            QuestProgram.this.eval(effect);
                        }
                    }
                }, delay.get());
            }
        }
    }

    private void eval(Effect effect) {
        try {
            effect.eval(this);
        } catch (Throwable t) {
            logger.error("程序 \"" + effect.getSource() + ":" + line + "\" 执行时出现异常: " + t.getMessage());
        }
    }

    public Player getPlayer() {
        return (Player) sender;
    }

    public DataQuest getDataQuest() {
        return dataQuest;
    }

    public DataPlayer getDataPlayer() {
        return CronusAPI.getData(getPlayer());
    }

    public int getDelay() {
        return delay.get();
    }

    public void setDelay(int delay) {
        this.delay.set(delay);
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }
}
