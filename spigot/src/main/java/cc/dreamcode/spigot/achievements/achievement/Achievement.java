package cc.dreamcode.spigot.achievements.achievement;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Achievement {

    private final int id;

    private final AchievementType type;
    private final AchievementReward reward;
    private final long required;

    public String getUniqueId() {
        return type.toString() + "-" + id;
    }
}