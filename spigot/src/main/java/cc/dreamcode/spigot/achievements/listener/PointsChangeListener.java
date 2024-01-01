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
import org.bukkit.event.Listener;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class PointsChangeListener implements Listener {

    private final AchievementsUserCache achievementsUserCache;
    private final PluginConfig pluginConfig;
    private final DreamLogger dreamLogger;

    @EventHandler
    public void handle(PointsChangeEvent event) {
        User user = event.getDoer().get();
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