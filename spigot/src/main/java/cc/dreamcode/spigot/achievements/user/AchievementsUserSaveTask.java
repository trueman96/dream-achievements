package cc.dreamcode.spigot.achievements.user;

import cc.dreamcode.platform.bukkit.component.scheduler.Scheduler;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;

@Scheduler(delay = 10 * 20L, interval = 10 * 20L)
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class AchievementsUserSaveTask implements Runnable {

    private final AchievementsUserCache achievementsUserCache;

    @Override
    public void run() {
        this.achievementsUserCache.values().stream()
                .filter(AchievementsUser::isNeedUpdate)
                .forEach(achievementsUser -> {
                    achievementsUser.setNeedUpdate(false);
                    achievementsUser.save();
                });
    }
}
