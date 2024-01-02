package cc.dreamcode.spigot.achievements.achievement;

import cc.dreamcode.menu.bukkit.BukkitMenuBuilder;
import cc.dreamcode.spigot.achievements.config.MessageConfig;
import cc.dreamcode.spigot.achievements.config.PluginConfig;
import cc.dreamcode.spigot.achievements.user.AchievementsUser;
import cc.dreamcode.spigot.achievements.user.AchievementsUserCache;
import cc.dreamcode.utilities.TimeUtil;
import cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.utilities.bukkit.ItemUtil;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.tasker.core.Tasker;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class AchievementMenu {

    private final Tasker tasker;
    private final PluginConfig pluginConfig;
    private final AchievementsUserCache achievementsUserCache;
    private final MessageConfig messageConfig;

    public void open(Player player) {
        BukkitMenuBuilder menuBuilder = this.pluginConfig.achievementMenuBuilder;
        this.tasker.newChain()
                .supplyAsync(() -> this.pluginConfig.achievementMenuBuilder.buildEmpty())
                .acceptAsync(menu -> {
                    AchievementsUser user = this.achievementsUserCache.findByUniqueId(player.getUniqueId());

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

                        long progress = user.getAchievementProgress(achievementType);
                        Achievement achievement = this.pluginConfig.achievementMap.computeIfAbsent(achievementType, k -> new ArrayList<>())
                                .stream()
                                .filter(a -> a.getRequired() > progress)
                                .min(Comparator.comparingLong(Achievement::getId))
                                .orElse(null), toReceive = this.pluginConfig.achievementMap.computeIfAbsent(achievementType, k -> new ArrayList<>())
                                .stream()
                                .filter(a -> a.getRequired() <= progress)
                                .max(Comparator.comparingLong(Achievement::getRequired))
                                .orElse(null);
                        if (toReceive != null && !user.isAchievementClaimed(toReceive)) {
                            menu.setItem(slot, ItemBuilder.of(item)
                                    .fixColors(new MapBuilder<String, Object>()
                                            .put("id", toReceive.getId())
                                            .put("reward", toReceive.getReward().getFriendlyName())
                                            .put("progress", toReceive.getType().equals(AchievementType.SPEND_TIME)
                                                    ? TimeUtil.convertDurationSeconds(Duration.ofMillis(progress)) : progress)
                                            .put("required", toReceive.getType().equals(AchievementType.SPEND_TIME)
                                                    ? TimeUtil.convertDurationSeconds(Duration.ofMillis(toReceive.getRequired())) : toReceive.getRequired())
                                            .put("available-receive", this.pluginConfig.availableReceive)
                                            .build())
                                    .toItemStack(), event -> {
                                player.closeInventory();
                                user.addClaimedAchievement(toReceive);
                                AchievementReward reward = toReceive.getReward();
                                ItemUtil.giveItem(player, reward.getItemStack());
                                this.messageConfig.receivedReward.send(player, new MapBuilder<String, Object>()
                                        .put("reward", reward.getFriendlyName())
                                        .put("achievement", toReceive.getUniqueId())
                                        .build());
                            });
                            continue;
                        }

                        if (achievement == null && toReceive != null && user.isAchievementClaimed(toReceive)) {
                            menu.setItem(slot, ItemBuilder.of(item)
                                    .fixColors(new MapBuilder<String, Object>()
                                            .put("id", toReceive.getId())
                                            .put("reward", toReceive.getReward().getFriendlyName())
                                            .put("progress", toReceive.getType().equals(AchievementType.SPEND_TIME)
                                                    ? TimeUtil.convertDurationSeconds(Duration.ofMillis(progress)) : progress)
                                            .put("required", toReceive.getType().equals(AchievementType.SPEND_TIME)
                                                    ? TimeUtil.convertDurationSeconds(Duration.ofMillis(toReceive.getRequired())) : toReceive.getRequired())
                                            .put("available-receive", this.pluginConfig.alreadyReceived)
                                            .build())
                                    .toItemStack(), event -> {
                                player.closeInventory();
                                AchievementReward reward = toReceive.getReward();
                                this.messageConfig.alreadyReceivedReward.send(player, new MapBuilder<String, Object>()
                                        .put("reward", reward.getFriendlyName())
                                        .put("achievement", toReceive.getUniqueId())
                                        .build());
                            });
                            continue;
                        }

                        if (achievement == null) {
                            this.messageConfig.noAchievementsForType.send(player, new MapBuilder<String, Object>()
                                    .put("type", achievementType.name())
                                    .build());
                            return;
                        }

                        menu.setItem(slot, ItemBuilder.of(item)
                                .fixColors(new MapBuilder<String, Object>()
                                        .put("id", achievement.getId())
                                        .put("reward", achievement.getReward().getFriendlyName())
                                        .put("progress", achievement.getType().equals(AchievementType.SPEND_TIME)
                                                ? TimeUtil.convertDurationSeconds(Duration.ofMillis(progress)) : progress)
                                        .put("required", achievement.getType().equals(AchievementType.SPEND_TIME)
                                                ? TimeUtil.convertDurationSeconds(Duration.ofMillis(achievement.getRequired())) : achievement.getRequired())
                                        .put("available-receive", this.pluginConfig.notReadyReceive)
                                        .build())
                                .toItemStack(), event -> this.messageConfig.cannotReceive.send(player, new MapBuilder<String, Object>()
                                .put("achievement", achievement.getUniqueId())
                                .put("reward", achievement.getReward().getFriendlyName())
                                .put("progress", achievement.getType().equals(AchievementType.SPEND_TIME)
                                        ? TimeUtil.convertDurationSeconds(Duration.ofMillis(progress)) : progress)
                                .put("required", achievement.getType().equals(AchievementType.SPEND_TIME)
                                        ? TimeUtil.convertDurationSeconds(Duration.ofMillis(achievement.getRequired())) : achievement.getRequired())
                                .build()));
                    }
                })
                .acceptSync(menu -> menu.open(player))
                .execute();
    }

    public Achievement findByProgress(AchievementType achievementType, long progress) {
        return this.pluginConfig.achievementMap.computeIfAbsent(achievementType, k -> new ArrayList<>())
                .stream()
                .filter(achievement -> achievement.getRequired() > progress)
                .findFirst().orElse(null);
    }
}