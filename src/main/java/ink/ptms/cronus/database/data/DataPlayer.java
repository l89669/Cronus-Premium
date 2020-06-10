package ink.ptms.cronus.database.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.event.*;
import ink.ptms.cronus.internal.Quest;
import ink.ptms.cronus.internal.QuestStage;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.program.Action;
import ink.ptms.cronus.internal.program.QuestProgram;
import io.izzel.taboolib.internal.gson.annotations.Expose;
import io.izzel.taboolib.module.db.local.SecuredFile;
import io.izzel.taboolib.util.serialize.DoNotSerialize;
import io.izzel.taboolib.util.serialize.TSerializable;
import io.izzel.taboolib.util.serialize.TSerializeCollection;
import io.izzel.taboolib.util.serialize.TSerializeMap;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author 坏黑
 * @Since 2019-05-24 0:29
 */
@SuppressWarnings("rawtypes")
public class DataPlayer implements TSerializable, Comparable {

    @DoNotSerialize
    @Expose(serialize = false, deserialize = false)
    private Player player;
    @DoNotSerialize
    @Expose(serialize = false, deserialize = false)
    private YamlConfiguration dataTemp = new SecuredFile();
    @Expose
    private YamlConfiguration dataGlobal = new SecuredFile();
    @TSerializeMap
    @Expose
    private final ConcurrentMap<String, DataQuest> quest = Maps.newConcurrentMap();
    @TSerializeMap
    @Expose
    private final ConcurrentMap<String, Long> questCompleted = Maps.newConcurrentMap();
    @TSerializeMap
    @Expose
    private final ConcurrentMap<String, Long> itemCooldown = Maps.newConcurrentMap();
    @TSerializeCollection
    @Expose
    private final List<String> questHide = Lists.newArrayList();
    @TSerializeCollection
    @Expose
    private final List<String> questLogs = Lists.newArrayList();
    @DoNotSerialize
    @Expose(serialize = false, deserialize = false)
    private final Map<Object, Object> latestUpdate = Maps.newHashMap();
    @DoNotSerialize
    @Expose(serialize = false, deserialize = false)
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();


    public DataPlayer() {
    }

    public DataPlayer(Player player) {
        this.player = player;
    }

    /**
     * 通过任务序号或标签获取玩家正在进行中的任务
     */
    public DataQuest getQuest(String questId) {
        for (Map.Entry<String, DataQuest> entry : quest.entrySet()) {
            Quest quest = entry.getValue().getQuest();
            if (quest != null && (questId.equals(quest.getId()) || questId.equals(quest.getLabel()))) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void clearDataTemp() {
        dataTemp = new SecuredFile();
    }

    public void clearDataGlobal() {
        dataGlobal = new SecuredFile();
    }

    public void acceptQuest(Quest quest) {
        if (quest == null) {
            return;
        }
        // 条件判断
        if (quest.getConditionAccept() != null && !quest.getConditionAccept().check(player)) {
            quest.eval(new QuestProgram(player, new DataQuest(quest.getId(), quest.getFirstStage())), Action.ACCEPT_FAIL);
            return;
        }
        // 冷却判断
        if (isQuestCooldown(quest)) {
            quest.eval(new QuestProgram(player, new DataQuest(quest.getId(), quest.getFirstStage())), Action.COOLDOWN);
            return;
        }
        CronusQuestAcceptEvent call = CronusQuestAcceptEvent.call(player, quest);
        if (!call.isCancelled()) {
            // 获取数据
            DataQuest dataQuest = new DataQuest(call.getQuest().getId(), call.getQuest().getFirstStage());
            // 执行动作
            call.getQuest().eval(new QuestProgram(player, dataQuest), Action.ACCEPT);
            // 移除记录
            this.quest.put(quest.getId(), dataQuest);
            this.questCompleted.remove(quest.getId());
        }
    }

    public void failureQuest(Quest quest) {
        if (quest == null) {
            return;
        }
        DataQuest dataQuest = this.quest.remove(quest.getId());
        if (dataQuest != null) {
            // 阶段失败
            QuestStage questStage = dataQuest.getStage();
            if (questStage != null) {
                CronusStageFailureEvent.call(player, quest, questStage);
                // 执行动作
                questStage.eval(new QuestProgram(player, dataQuest), Action.FAILURE);
            }
            // 任务失败
            CronusQuestFailureEvent.call(player, quest);
            // 执行动作
            quest.eval(new QuestProgram(player, dataQuest), Action.FAILURE);
        }
    }

    public void completeQuest(Quest quest) {
        if (quest == null || isQuestCompleted(quest.getId())) {
            return;
        }
        DataQuest dataQuest = this.quest.get(quest.getId());
        if (dataQuest == null) {
            return;
        }
        QuestStage questStage = dataQuest.getStage();
        if (questStage == null) {
            return;
        }
        // 将所有条目设定为完成状态
        for (QuestTask questTask : questStage.getTask()) {
            questTask.complete(dataQuest);
        }
        // 任务检查
        dataQuest.checkAndComplete(player);
        completeQuest(quest);
    }

    public void stopQuest() {
        for (Map.Entry<String, DataQuest> entry : this.quest.entrySet()) {
            Quest quest = this.quest.remove(entry.getKey()).getQuest();
            if (quest != null) {
                CronusQuestStopEvent.call(player, quest);
            }
        }
    }

    public void stopQuest(Quest quest) {
        if (quest == null) {
            return;
        }
        if (this.quest.containsKey(quest.getId())) {
            this.quest.remove(quest.getId());
            CronusQuestStopEvent.call(player, quest);
        }
    }

    public boolean isQuestCooldown(Quest quest) {
        return questCompleted.containsKey(quest.getId()) && (quest.getCooldown() == -1 || System.currentTimeMillis() - questCompleted.get(quest.getId()) < quest.getCooldown());
    }

    public boolean isQuestCompleted(String id) {
        return questCompleted.getOrDefault(id, 0L) > 0;
    }

    public void setQuestCompleted(String id, boolean var) {
        if (var) {
            questCompleted.put(id, System.currentTimeMillis());
        } else {
            questCompleted.remove(id);
        }
    }

    public void writeQuestLogs(String in) {
        if (questLogs.isEmpty()) {
            questLogs.add(in);
        } else {
            questLogs.add(0, in);
        }
    }

    @Override
    public void read(String fieldName, String value) {
        if (fieldName.equals("dataGlobal")) {
            try {
                dataGlobal.loadFromString(value);
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String write(String fieldName, Object value) {
        if (fieldName.equals("dataGlobal")) {
            return dataGlobal.saveToString();
        }
        return null;
    }

    public DataPlayer push() {
        if (!player.hasMetadata("cronus:downloaded")) {
            System.out.println("[Cronus] 玩家 " + player.getName() + " 在数据同步之前写入数据");
            return this;
        }
        CronusDataPushEvent.call(player, this);
        executorService.submit(() -> Cronus.getCronusService().getDatabase().upload(player, this));
        return this;
    }

    // *********************************
    //
    //        Getter and Setter
    //
    // *********************************

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Map<String, DataQuest> getQuest() {
        return quest;
    }

    public Map<String, Long> getQuestCompleted() {
        return questCompleted;
    }

    public Map<String, Long> getItemCooldown() {
        return itemCooldown;
    }

    public List<String> getQuestHide() {
        return questHide;
    }

    public List<String> getQuestLogs() {
        return questLogs;
    }

    public YamlConfiguration getDataGlobal() {
        return dataGlobal;
    }

    public YamlConfiguration getDataTemp() {
        return dataTemp;
    }

    public Map<Object, Object> getLatestUpdate() {
        return latestUpdate;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return 0;
    }
}
