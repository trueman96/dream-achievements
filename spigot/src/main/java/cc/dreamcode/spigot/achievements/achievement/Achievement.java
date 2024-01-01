package cc.dreamcode.spigot.achievements.achievement;

import eu.okaeri.configs.OkaeriConfig;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Achievement extends OkaeriConfig {

    private int id;

    private AchievementType type;
    private AchievementReward reward;
    private long required;

    public String getUniqueId() {
        return type.toString() + "-" + id;
    }
}