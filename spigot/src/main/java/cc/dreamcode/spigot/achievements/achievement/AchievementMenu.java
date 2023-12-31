package cc.dreamcode.spigot.achievements.achievement;

import cc.dreamcode.menu.bukkit.BukkitMenuBuilder;
import cc.dreamcode.menu.bukkit.BukkitMenuProvider;
import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import cc.dreamcode.spigot.achievements.config.PluginConfig;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class AchievementMenu {

    private final PluginConfig pluginConfig;
    private final BukkitMenuProvider bukkitMenuProvider;

    public void open(Player player) {
        BukkitMenuBuilder menuBuilder = this.pluginConfig.achievementsConfig.achievementMenuBuilder;
        BukkitMenu menu = menuBuilder.buildEmpty();
        for (Map.Entry<Integer, ItemStack> entry : menuBuilder.getItems().entrySet()) {
            int slot = entry.getKey();
            ItemStack item = entry.getValue();
            if (item == null || item.getType().equals(Material.AIR)) {
                continue;
            }


        }
    }

    private void openAchievements(Player player, AchievementType type) {
    }
}
