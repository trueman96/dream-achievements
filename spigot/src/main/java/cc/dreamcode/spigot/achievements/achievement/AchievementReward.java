package cc.dreamcode.spigot.achievements.achievement;

import eu.okaeri.configs.OkaeriConfig;
import lombok.*;
import org.bukkit.inventory.ItemStack;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AchievementReward extends OkaeriConfig {

    private String friendlyName;
    private ItemStack itemStack;

}