package ink.ptms.cronus.internal.program.function;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.Quest;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.Function;
import ink.ptms.cronus.uranus.program.Program;
import io.izzel.taboolib.module.locale.TLocale;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-12 15:24
 */
@Auto
public class FunctionQuest extends Function {

    @Override
    public String getName() {
        return "quest";
    }

    @Override
    public Object eval(Program program, String... args) {
        if (program instanceof QuestProgram) {
            DataQuest dataQuest = ((QuestProgram) program).getDataQuest();
            Quest quest = dataQuest.getQuest();
            if (quest == null) {
                return "<Invalid-Quest>";
            }
            switch (args[0].toLowerCase()) {
                case "display":
                    return dataQuest.getQuest().getDisplay();
                case "name":
                    return dataQuest.getCurrentQuest();
                case "stage":
                    return dataQuest.getCurrentStage();
                case "stage.next":
                    int index = dataQuest.getStageIndex(quest) + 1;
                    return index >= quest.getStage().size() ? "-" : quest.getStage().get(index).getId();
                case "time.start":
                    return dataQuest.getTimeStart();
                case "time.complete":
                    return ((QuestProgram) program).getDataPlayer().getQuestCompleted().getOrDefault(dataQuest.getCurrentQuest(), 0L);
                case "visible":
                    return ((QuestProgram) program).getDataPlayer().getQuestHide().contains(quest.getId()) ? TLocale.asString("quest-placeholder-visible0") : TLocale.asString("quest-placeholder-visible1");
                default:
                    return dataQuest.getCurrentQuest();
            }
        } else if (program.getSender() instanceof Player) {
            DataPlayer dataPlayer = CronusAPI.getData((Player) program.getSender());
            switch (args[0].toLowerCase()) {
                case "current": {
                    for (Map.Entry<String, DataQuest> pair : dataPlayer.getQuest().entrySet()) {
                        Quest quest = pair.getValue().getQuest();
                        if (quest != null && (quest.getId().equals(args[1]) || quest.getLabel().equals(args[1]) || quest.getBookTag().contains(args[1]))) {
                            return "true";
                        }
                    }
                    return "false";
                }
                case "completed": {
                    for (String id : dataPlayer.getQuestCompleted().keySet()) {
                        Quest quest = Cronus.getCronusService().getRegisteredQuest().get(id);
                        if (quest != null && (quest.getId().equals(args[1]) || quest.getLabel().equals(args[1]) || quest.getBookTag().contains(args[1]))) {
                            return "true";
                        }
                    }
                    return "false";
                }
                case "current.count": {
                    int i = 0;
                    for (Map.Entry<String, DataQuest> pair : dataPlayer.getQuest().entrySet()) {
                        Quest quest = pair.getValue().getQuest();
                        if (quest != null && (quest.getId().equals(args[1]) || quest.getLabel().equals(args[1]) || quest.getBookTag().contains(args[1]))) {
                            i++;
                        }
                    }
                    return String.valueOf(i);
                }
                case "completed.count": {
                    int i = 0;
                    for (String id : dataPlayer.getQuestCompleted().keySet()) {
                        Quest quest = Cronus.getCronusService().getRegisteredQuest().get(id);
                        if (quest != null && (quest.getId().equals(args[1]) || quest.getLabel().equals(args[1]) || quest.getBookTag().contains(args[1]))) {
                            i++;
                        }
                    }
                    return String.valueOf(i);
                }
                default:
                    return "<Error>";
            }
        }
        return "<Non-Quest>";
    }
}
