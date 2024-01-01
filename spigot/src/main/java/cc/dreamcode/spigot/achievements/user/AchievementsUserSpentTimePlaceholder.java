package cc.dreamcode.spigot.achievements.user;

import cc.dreamcode.utilities.TimeUtil;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class AchievementsUserSpentTimePlaceholder extends PlaceholderExpansion {

    private final AchievementsUserCache achievementsUserCache;

    @Override
    public @NotNull String getAuthor() {
        return "trueman96";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "achievements-user";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("spent-time")) {
            return player == null ? "null" : TimeUtil.convertDurationSeconds(
                    Duration.ofMillis(this.achievementsUserCache.findByUniqueId(player.getUniqueId()).getSpendTime()));
        }

        return null;
    }
}