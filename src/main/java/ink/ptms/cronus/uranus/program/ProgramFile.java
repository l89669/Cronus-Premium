package ink.ptms.cronus.uranus.program;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @Author 坏黑
 * @Since 2019-05-11 11:41
 */
public class ProgramFile {

    private final String name;
    private final ProgramLine program;
    private final YamlConfiguration conf;

    public ProgramFile(String name, YamlConfiguration conf, ProgramLine program) {
        this.name = name;
        this.conf = conf;
        this.program = program;
    }

    public Object eval(CommandSender sender, String... args) {
        return new Program().eval(this, sender, args);
    }

    public String getName() {
        return name;
    }

    public YamlConfiguration getConf() {
        return conf;
    }

    public ProgramLine getProgram() {
        return program;
    }
}
