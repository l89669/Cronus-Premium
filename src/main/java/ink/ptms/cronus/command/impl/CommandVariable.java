package ink.ptms.cronus.command.impl;

import com.google.common.collect.Lists;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.command.CronusCommand;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.program.NoneProgram;
import ink.ptms.cronus.internal.variable.VariableExecutor;
import ink.ptms.cronus.internal.variable.impl.EngineG;
import ink.ptms.cronus.internal.variable.impl.EngineY;
import ink.ptms.cronus.uranus.function.FunctionParser;
import io.izzel.taboolib.module.command.base.Argument;
import io.izzel.taboolib.module.command.base.BaseCommand;
import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.SubCommand;
import io.izzel.taboolib.util.ArrayUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-07-26 18:16
 */
@BaseCommand(name = "CronusVariable", aliases = {"cVariable", "cVar", "cv"}, permission = "*")
public class CommandVariable extends CronusCommand {

    @SubCommand
    BaseSubCommand playerTemp = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "修改临时变量";
        }

        @Override
        public Argument[] getArguments() {
            return new Argument[] {new Argument("玩家"), new Argument("键"), new Argument("符号", () -> Lists.newArrayList("+", "-", "=")), new Argument("值")};
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &7" + args[0] + " &c离线.");
                return;
            }
            DataPlayer dataPlayer = CronusAPI.getData(player);
            VariableExecutor.update(new EngineY(dataPlayer.getDataTemp()), args[1], args[2], FunctionParser.parseAll(new NoneProgram(player), ArrayUtil.arrayJoin(args, 3)));
            dataPlayer.push();
        }
    };

    @SubCommand
    BaseSubCommand playerGlobal = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "修改永久变量";
        }

        @Override
        public Argument[] getArguments() {
            return new Argument[] {new Argument("玩家"), new Argument("键"), new Argument("符号", () -> Lists.newArrayList("+", "-", "=")), new Argument("值")};
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &7" + args[0] + " &c离线.");
                return;
            }
            DataPlayer dataPlayer = CronusAPI.getData(player);
            VariableExecutor.update(new EngineY(dataPlayer.getDataGlobal()), args[1], args[2], FunctionParser.parseAll(new NoneProgram(player), ArrayUtil.arrayJoin(args, 3)));
            dataPlayer.push();
        }
    };

    @SubCommand
    BaseSubCommand questTemp = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "修改任务变量";
        }

        @Override
        public Argument[] getArguments() {
            return new Argument[] {new Argument("玩家"), new Argument("任务"), new Argument("键"), new Argument("符号", () -> Lists.newArrayList("+", "-", "=")), new Argument("值")};
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &7" + args[0] + " &c离线.");
                return;
            }
            DataPlayer dataPlayer = CronusAPI.getData(player);
            DataQuest playerQuest = dataPlayer.getQuest(args[1]);
            if (playerQuest == null) {
                error(sender, "任务 &7" + args[1] + " &c无效.");
                return;
            }
            VariableExecutor.update(new EngineY(playerQuest.getDataStage()), args[2], args[3], FunctionParser.parseAll(new NoneProgram(player), ArrayUtil.arrayJoin(args, 4)));
            dataPlayer.push();
        }
    };

    @SubCommand
    BaseSubCommand questGlobal = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "修改任务变量";
        }

        @Override
        public Argument[] getArguments() {
            return new Argument[] {new Argument("玩家"), new Argument("任务"), new Argument("键"), new Argument("符号", () -> Lists.newArrayList("+", "-", "=")), new Argument("值")};
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &7" + args[0] + " &c离线.");
                return;
            }
            DataPlayer dataPlayer = CronusAPI.getData(player);
            DataQuest playerQuest = dataPlayer.getQuest(args[1]);
            if (playerQuest == null) {
                error(sender, "任务 &7" + args[1] + " &c无效.");
                return;
            }
            VariableExecutor.update(new EngineY(playerQuest.getDataQuest()), args[2], args[3], FunctionParser.parseAll(new NoneProgram(player), ArrayUtil.arrayJoin(args, 4)));
            dataPlayer.push();
        }
    };

    @SubCommand
    BaseSubCommand server = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "修改全局变量";
        }

        @Override
        public Argument[] getArguments() {
            return new Argument[] {new Argument("键"), new Argument("符号", () -> Lists.newArrayList("+", "-", "=")), new Argument("值")};
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            VariableExecutor.update(new EngineG(), args[1], args[2], ArrayUtil.arrayJoin(args, 3));
        }
    };
}
