package cc.dreamcode.spigot.achievements.listener;

import cc.dreamcode.spigot.achievements.user.AchievementsUser;
import cc.dreamcode.spigot.achievements.user.AchievementsUserRepository;
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

import java.util.HashMap;
import java.util.HashSet;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class PlayerQuitJoinListener implements Listener {

    private final Tasker tasker;
    private final DocumentPersistence documentPersistence;
    private final AchievementsUserRepository achievementsUserRepository;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent(@NonNull PlayerJoinEvent event) {
        Player source = event.getPlayer();
        this.tasker.newChain()
                .supplyAsync(() -> {
                    AchievementsUser achievementsUser = this.achievementsUserRepository.findByPath(source.getUniqueId()).orElseGet(() -> {
                        AchievementsUser user = (AchievementsUser) this.documentPersistence.createDocument(
                                this.achievementsUserRepository.getCollection(), PersistencePath.of(source.getUniqueId().toString()));
                        user.setClaimedAchievements(new HashSet<>());
                        user.setAchievementProgress(new HashMap<>());
                        return user;
                    });
                    achievementsUser.setName(source.getName());
                    return achievementsUser;
                })
                .acceptAsync(user -> {

                })
                .execute();
    }
}
