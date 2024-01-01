package cc.dreamcode.spigot.achievements.achievement;

import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.spigot.achievements.config.PluginConfig;
import cc.dreamcode.spigot.achievements.user.AchievementsUser;
import cc.dreamcode.spigot.achievements.user.AchievementsUserCache;
import com.cryptomorin.xseries.XMaterial;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class AchievementListener implements Listener {

    private final PluginConfig pluginConfig;
    private final DreamLogger dreamLogger;
    private final AchievementsUserCache achievementsUserCache;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        AchievementsUser user = this.achievementsUserCache.findByUniqueId(player.getUniqueId());
        if (user == null) {
            return;
        }

        user.addAchievementProgress(AchievementType.MINED_BLOCKS, 1);
        if (this.pluginConfig.debug) {
            this.dreamLogger.info("BlockBreakEvent: " + player.getName() + " mined");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        {
            AchievementsUser user = this.achievementsUserCache.findByUniqueId(player.getUniqueId());
            if (user == null) {
                return;
            }

            user.addAchievementProgress(AchievementType.DEATHS, 1);
            if (this.pluginConfig.debug) {
                this.dreamLogger.info("PlayerDeathEvent: " + player.getName() + " died");
            }
        }
        {
            Player killer = player.getKiller();
            if (killer == null) {
                return;
            }

            AchievementsUser user = this.achievementsUserCache.findByUniqueId(killer.getUniqueId());
            if (user == null) {
                return;
            }

            user.addAchievementProgress(AchievementType.KILLS, 1);
            if (this.pluginConfig.debug) {
                this.dreamLogger.info("PlayerDeathEvent: " + killer.getName() + " killed");
            }
        }
    }

    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
        Player source = event.getPlayer();
        if (source == null) {
            return;
        }

        ItemStack item = event.getItem();
        Material material;
        try {
            material = XMaterial.TOTEM_OF_UNDYING.parseMaterial();
        } catch (Exception e) {
            if (this.pluginConfig.debug) {
                this.dreamLogger.info(
                        "PlayerItemConsumeEvent: " + source.getName() + " couldn't find totem!!");
            }
            return;
        }

        if (material == null) {
            if (this.pluginConfig.debug) {
                this.dreamLogger.info(
                        "PlayerItemConsumeEvent: " + source.getName() + " couldn't find totem!!");
            }
            return;
        }

        if (item == null || !item.getType().equals(material)) {
            return;
        }

        AchievementsUser user = this.achievementsUserCache.findByUniqueId(source.getUniqueId());
        if (user == null) {
            return;
        }

        user.addAchievementProgress(AchievementType.BEATEN_TOTEMS, 1);
        if (this.pluginConfig.debug) {
            this.dreamLogger.info("PlayerItemConsumeEvent: " + source.getName() + " consumed totem");
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getItemInHand();
        if (itemInHand.getItemMeta() == null || !itemInHand.getItemMeta().hasLore() || !itemInHand.getItemMeta().hasDisplayName()) {
            return;
        }

        ItemStack caseItem = this.pluginConfig.premiumCaseItemStack;
        if (!itemInHand.isSimilar(caseItem)) {
            return;
        }

        AchievementsUser user = this.achievementsUserCache.findByUniqueId(player.getUniqueId());
        if (user == null) {
            return;
        }

        user.addAchievementProgress(AchievementType.OPENED_CASE, 1);
        if (this.pluginConfig.debug) {
            this.dreamLogger.info("BlockPlaceEvent: " + player.getName() + " placed case");
        }
    }

}
