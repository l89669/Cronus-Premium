package ink.ptms.cronus.event;

import ink.ptms.cronus.service.Service;
import io.izzel.taboolib.module.event.EventNormal;
import org.bukkit.Bukkit;

public class CronusReloadServiceEvent extends EventNormal<CronusReloadServiceEvent> {

    private final Service service;

    public CronusReloadServiceEvent(Service service) {
        async(!Bukkit.isPrimaryThread());
        this.service = service;
    }

    public static CronusReloadServiceEvent call(Service service) {
        return new CronusReloadServiceEvent(service).call();
    }

    public Service getService() {
        return service;
    }
}
