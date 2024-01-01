package cc.dreamcode.spigot.achievements.user;

import cc.dreamcode.spigot.achievements.achievement.Achievement;
import cc.dreamcode.spigot.achievements.achievement.AchievementType;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;
import eu.okaeri.persistence.document.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class AchievementsUser extends Document {

    private String name;
    private Set<String> claimedAchievements;
    private Map<String, Long> achievementProgress;
    private long lastTimeMeasurement, spendTime;

    private transient boolean needUpdate;

    public UUID getUniqueId() {
        return this.getPath().toUUID();
    }


    public long getAchievementProgress(AchievementType type) {
        return this.achievementProgress.getOrDefault(type.toString(), 0L);
    }

    public void addAchievementProgress(AchievementType type, long progress) {
        this.achievementProgress.put(type.toString(), getAchievementProgress(type) + progress);
        needUpdate = true;
    }

    public void setAchievementProgress(AchievementType type, long progress) {
        this.achievementProgress.put(type.toString(), progress);
        needUpdate = true;
    }

    public boolean isAchievementClaimed(Achievement achievement) {
        return this.claimedAchievements.contains(achievement.getUniqueId());
    }

    public void addClaimedAchievement(Achievement achievement) {
        this.claimedAchievements.add(achievement.getUniqueId());
        needUpdate = true;
    }

}
