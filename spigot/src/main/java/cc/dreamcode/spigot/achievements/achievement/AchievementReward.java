package cc.dreamcode.spigot.achievements.achievement;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.inventory.ItemStack;

@Getter
@Builder
@ToString
public class AchievementReward {

    private final String friendlyName;
    private final ItemStack itemStack;

}