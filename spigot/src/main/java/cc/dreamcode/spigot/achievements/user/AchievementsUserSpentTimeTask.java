package cc.dreamcode.spigot.achievements.user;

import cc.dreamcode.platform.bukkit.component.scheduler.Scheduler;
import cc.dreamcode.spigot.achievements.achievement.AchievementType;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Scheduler(delay = 30 * 20L, interval = 30 * 20L)
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class AchievementsUserSpentTimeTask implements Runnable {

    private final AchievementsUserCache achievementsUserCache;

    @Override
    public void run() {
        long currentTimeMillis = System.currentTimeMillis();
        for (Player value : Bukkit.getOnlinePlayers()) {
            AchievementsUser user = this.achievementsUserCache.findByUniqueId(value.getUniqueId());
            if (user == null) {
                continue;
            }

            long time = user.getSpendTime() + (currentTimeMillis - user.getLastTimeMeasurement());
            user.setSpendTime(time);
            user.setAchievementProgress(AchievementType.SPEND_TIME, time);
            user.setLastTimeMeasurement(System.currentTimeMillis());

        }
    }
}
