package cc.dreamcode.spigot.achievements.user;

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

    public UUID getUniqueId() {
        return this.getPath().toUUID();
    }

}
