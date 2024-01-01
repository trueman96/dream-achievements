package cc.dreamcode.spigot.achievements.listener;

import cc.dreamcode.spigot.achievements.user.AchievementsUser;
import cc.dreamcode.spigot.achievements.user.AchievementsUserCache;
import cc.dreamcode.spigot.achievements.user.AchievementsUserRepository;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.persistence.PersistencePath;
import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.tasker.core.Tasker;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.HashSet;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class PlayerQuitJoinListener implements Listener {

    private final Tasker tasker;
    private final DocumentPersistence documentPersistence;
    private final AchievementsUserCache achievementsUserCache;
    private final AchievementsUserRepository achievementsUserRepository;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent(@NonNull PlayerJoinEvent event) {
        Player source = event.getPlayer();
        this.tasker.newChain()
                .supplyAsync(() -> {
                    AchievementsUser achievementsUser = this.achievementsUserRepository.findByPath(source.getUniqueId()).orElseGet(() -> {
                        AchievementsUser user = (AchievementsUser) this.documentPersistence.update(ConfigManager.create(AchievementsUser.class),
                                this.achievementsUserRepository.getCollection());
                        user.setPath(PersistencePath.of(source.getUniqueId().toString()));
                        user.setClaimedAchievements(new HashSet<>());
                        user.setAchievementProgress(new HashMap<>());
                        return user;
                    });
                    achievementsUser.setName(source.getName());
                    return achievementsUser;
                })
                .acceptAsync(achievementsUser -> {
                    achievementsUser.setLastTimeMeasurement(System.currentTimeMillis());
                    this.achievementsUserCache.add(achievementsUser);
                })
                .execute();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(@NonNull PlayerQuitEvent event) {
        Player source = event.getPlayer();
        this.tasker.newChain()
                .supplyAsync(() -> this.achievementsUserCache.findByUniqueId(source.getUniqueId()))
                .acceptAsync(AchievementsUser::save)
                .acceptAsync(this.achievementsUserCache::remove)
                .execute();
    }
}
