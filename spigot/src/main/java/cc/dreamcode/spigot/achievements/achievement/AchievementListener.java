package cc.dreamcode.spigot.achievements.achievement;

import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.spigot.achievements.config.PluginConfig;
import cc.dreamcode.spigot.achievements.user.AchievementsUser;
import cc.dreamcode.spigot.achievements.user.AchievementsUserCache;
import com.cryptomorin.xseries.XMaterial;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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
    public void onEntityResurrectEvent(EntityResurrectEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }

        Player source = (Player) entity;
        if (this.pluginConfig.debug) {
            this.dreamLogger.info("EntityResurrectEvent: " + source.getName() + " consumed totem");
        }

        ItemStack item = source.getItemInHand();
        Material material = XMaterial.TOTEM_OF_UNDYING.parseMaterial();
        if (item == null || item.getType().equals(Material.AIR) || !item.getType().equals(material)) {
            if (this.pluginConfig.debug) {
                this.dreamLogger.info("EntityResurrectEvent: item " + source.getName() + " is null");
            }
            return;
        }

        AchievementsUser user = this.achievementsUserCache.findByUniqueId(source.getUniqueId());
        if (user == null) {
            if (this.pluginConfig.debug) {
                this.dreamLogger.info("EntityResurrectEvent: user " + source.getName() + " is null");
            }
            return;
        }

        user.addAchievementProgress(AchievementType.BEATEN_TOTEMS, 1);
        if (this.pluginConfig.debug) {
            this.dreamLogger.info("EntityResurrectEvent: " + source.getName() + " added progress");
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
