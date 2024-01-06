package cc.dreamcode.spigot.achievements.listener;

import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.spigot.achievements.achievement.AchievementType;
import cc.dreamcode.spigot.achievements.config.PluginConfig;
import cc.dreamcode.spigot.achievements.user.AchievementsUser;
import cc.dreamcode.spigot.achievements.user.AchievementsUserCache;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import net.dzikoysk.funnyguilds.event.rank.PointsChangeEvent;
import net.dzikoysk.funnyguilds.user.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import panda.std.Option;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class PointsChangeListener implements Listener {

    private final AchievementsUserCache achievementsUserCache;
    private final PluginConfig pluginConfig;
    private final DreamLogger dreamLogger;

    @EventHandler(priority = EventPriority.MONITOR)
    public void handle(PointsChangeEvent event) {
        Option<User> userOption = event.getDoer();
        if (userOption.isEmpty()) {
            this.dreamLogger.warning("PointsChangeEvent: userOption is empty");
            return;
        }

        User user = userOption.get();
        AchievementsUser achievementsUser = this.achievementsUserCache.findByUniqueId(user.getUUID());
        if (achievementsUser == null) {
            return;
        }
        achievementsUser.addAchievementProgress(AchievementType.CONQUERED_POINTS, event.getPointsChange());
        if (this.pluginConfig.debug) {
            this.dreamLogger.info("PointsChangeEvent: " + event.getPointsChange() + " for " + user.getName());
        }
    }

}