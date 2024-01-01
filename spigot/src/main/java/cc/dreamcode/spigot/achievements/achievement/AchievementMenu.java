package cc.dreamcode.spigot.achievements.achievement;

import cc.dreamcode.menu.bukkit.BukkitMenuBuilder;
import cc.dreamcode.menu.bukkit.BukkitMenuProvider;
import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import cc.dreamcode.spigot.achievements.config.PluginConfig;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.tasker.core.Tasker;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class AchievementMenu {

    private final Tasker tasker;
    private final PluginConfig pluginConfig;
    private final BukkitMenuProvider bukkitMenuProvider;

    public void open(Player player) {
        BukkitMenuBuilder menuBuilder = this.pluginConfig.achievementMenuBuilder;
        this.tasker.newChain()
                .supplyAsync(() -> this.pluginConfig.achievementMenuBuilder.buildEmpty())
                .acceptAsync(menu -> {
                    for (Map.Entry<Integer, ItemStack> entry : menuBuilder.getItems().entrySet()) {
                        int slot = entry.getKey();
                        ItemStack item = entry.getValue();
                        if (item == null || item.getType().equals(Material.AIR)) {
                            continue;
                        }

                        AchievementType achievementType = this.pluginConfig.achievementTypeSlot.get(slot);
                        if (achievementType == null) {
                            menu.setItem(slot, item);
                            continue;
                        }

                        menu.setItem(slot, item, event -> {

                        });
                    }
                })
                .acceptSync(menu -> menu.open(player))
                .execute();
    }
}
