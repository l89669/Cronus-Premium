package ink.ptms.cronus;

import io.izzel.taboolib.loader.Plugin;
import io.izzel.taboolib.loader.PluginBase;
import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.dependency.Dependency;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.lite.Catchers;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @Author 坏黑
 * @Since 2019-05-23 18:06
 */
@Dependency(maven = "com.mongodb:mongodb:3.12.2", url = "https://skymc.oss-cn-shanghai.aliyuncs.com/libs/mongo-java-driver-3.12.2.jar")
public class Cronus extends Plugin {

    @TInject
    private static Cronus inst;
    @TInject(state = TInject.State.LOADING, init = "init", active = "active", cancel = "cancel")
    private static CronusService cronusService;
    @TInject(state = TInject.State.LOADING, init = "init", active = "start")
    private static CronusLoader cronusLoader;
    private static CronusVersion cronusVersion;
    @TInject(value = "config.yml")
    private static TConfig conf;
    @TInject
    private static TLogger logger;

    @Override
    public void onLoad() {
        cronusVersion = CronusVersion.fromString(getPlugin().getDescription().getVersion());
    }

    @Override
    public void onEnable() {
        try (InputStreamReader inputStreamReader = new InputStreamReader(getPlugin().getResource("motd.txt"), StandardCharsets.UTF_8); BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            bufferedReader.lines().forEach(l -> Bukkit.getConsoleSender().sendMessage(Strings.replaceWithOrder(l, getPlugin().getDescription().getVersion())));
        } catch (Throwable ignored) {
        }
    }

    @Override
    public void onDisable() {
        Catchers.getPlayerdata().clear();
    }

    public static void reloadQuest() {
        cronusLoader.start();
    }

    // *********************************
    //
    //        Getter and Setter
    //
    // *********************************

    public static PluginBase getInst() {
        return inst.getPlugin();
    }

    public static CronusLoader getCronusLoader() {
        return cronusLoader;
    }

    public static CronusService getCronusService() {
        return cronusService;
    }

    public static CronusVersion getCronusVersion() {
        return cronusVersion;
    }

    public static TConfig getConf() {
        return conf;
    }
}
