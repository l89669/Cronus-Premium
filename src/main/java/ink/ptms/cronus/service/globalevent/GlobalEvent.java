package ink.ptms.cronus.service.globalevent;

import com.google.common.collect.Lists;
import ink.ptms.cronus.CronusMirror;
import ink.ptms.cronus.event.CronusReloadServiceEvent;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.condition.ConditionParser;
import ink.ptms.cronus.internal.program.QuestEffect;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.service.Service;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.util.UtilsKt;
import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-08-12 10:28
 */
@Auto
public class GlobalEvent implements Service, Listener {

    @TInject("events.yml")
    private static TConfig conf;
    @TInject
    private static TLogger logger;

    private final List<GlobalEventPack> packs = Lists.newArrayList();

    @Override
    public void active() {
        packs.clear();
        for (Object element : conf.getList("Events")) {
            Map<String, Object> map;
            if (element instanceof Map) {
                map = (Map<String, Object>) element;
            } else if (element instanceof ConfigurationSection) {
                map = ((ConfigurationSection) element).getValues(false);
            } else {
                logger.warn("Invalid Type: " + element);
                continue;
            }
            // 缺少动作
            if (!map.containsKey("effect")) {
                logger.warn("No Effect: " + element);
                continue;
            }
            try {
                GlobalEventPack eventPack = new GlobalEventPack(String.valueOf(map.get("name")), ConditionParser.fromObject(map.get("condition")), new QuestEffect((List<String>) map.get("effect")));
                // 特殊条件
                if (map.get("data") instanceof Map) {
                    eventPack.getData().putAll((Map<String, Object>) map.get("data"));
                    // 特殊条件初始化失败
                    if (!eventPack.setupData()) {
                        logger.error("Event " + map.get("name") + " data setup failed.");
                    }
                }
                packs.add(eventPack);
            } catch (Throwable t) {
                logger.error("Event " + map.get("name") + " failed to load.");
                t.printStackTrace();
            }
        }
        CronusReloadServiceEvent.call(this);
    }

    @EventHandler
    public void e(PlayerJoinEvent e) {
        handle(e.getPlayer(), e, "player_join");
    }

    @EventHandler
    public void e(PlayerQuitEvent e) {
        handle(e.getPlayer(), e, "player_quit");
    }

    public void handle0(Player player, Event event, List<Class<? extends QuestTask<?>>> tasks) {
        List<String> list = new ArrayList<>();
        for (Class<? extends QuestTask<?>> t : tasks) {
            list.add(t.getAnnotation(Task.class).name());
        }
        handle(player, event, list);
    }

    public void handle(Player player, Event event, String... tasks) {
        handle(player, event, Lists.newArrayList(tasks));
    }

    public void handle(Player player, Event event, List<String> tasks) {
        if (tasks == null || tasks.isEmpty() || event == null) {
            return;
        }
        (CronusMirror.isIgnored(event.getClass()) ? CronusMirror.getMirror() : CronusMirror.getMirror("GlobalEvent:" + event.getEventName())).check(new Runnable() {

            @Override
            public void run() {
                // 获取所有全局事件
                for (GlobalEventPack eventPack : packs) {
                    // 判断事件名称
                    if (UtilsKt.INSTANCE.any(tasks, i -> eventPack.getName().equalsIgnoreCase(i))
                            // 条件
                            && (eventPack.getCondition() == null || eventPack.getCondition().check(player))
                            // 数据
                            && (eventPack.getQuestTask() == null || eventPack.getQuestTask().check(player, event))) {
                        try {
                            eventPack.getEffect().eval(player);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public boolean isSelect(List<Class<? extends QuestTask<?>>> tasks, String name) {
        return UtilsKt.INSTANCE.any(tasks, task -> task.getAnnotation(Task.class).name().equalsIgnoreCase(name));
    }

    public List<GlobalEventPack> getPacks() {
        return packs;
    }
}
