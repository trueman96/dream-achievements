package cc.dreamcode.spigot.achievements;

import cc.dreamcode.command.bukkit.BukkitCommandProvider;
import cc.dreamcode.menu.bukkit.BukkitMenuProvider;
import cc.dreamcode.menu.bukkit.okaeri.MenuBuilderSerdes;
import cc.dreamcode.notice.minecraft.bukkit.serdes.BukkitNoticeSerdes;
import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.platform.bukkit.DreamBukkitConfig;
import cc.dreamcode.platform.bukkit.DreamBukkitPlatform;
import cc.dreamcode.platform.bukkit.component.CommandComponentResolver;
import cc.dreamcode.platform.bukkit.component.ConfigurationComponentResolver;
import cc.dreamcode.platform.bukkit.component.ListenerComponentResolver;
import cc.dreamcode.platform.bukkit.component.RunnableComponentResolver;
import cc.dreamcode.platform.component.ComponentManager;
import cc.dreamcode.platform.persistence.DreamPersistence;
import cc.dreamcode.platform.persistence.component.DocumentPersistenceComponentResolver;
import cc.dreamcode.platform.persistence.component.DocumentRepositoryComponentResolver;
import cc.dreamcode.spigot.achievements.achievement.AchievementCommand;
import cc.dreamcode.spigot.achievements.achievement.AchievementListener;
import cc.dreamcode.spigot.achievements.achievement.AchievementMenu;
import cc.dreamcode.spigot.achievements.config.MessageConfig;
import cc.dreamcode.spigot.achievements.config.PluginConfig;
import cc.dreamcode.spigot.achievements.listener.PlayerQuitJoinListener;
import cc.dreamcode.spigot.achievements.listener.PointsChangeListener;
import cc.dreamcode.spigot.achievements.user.*;
import cc.dreamcode.utilities.optional.CustomOptional;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.tasker.bukkit.BukkitTasker;
import lombok.Getter;
import lombok.NonNull;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public final class AchievementsPlugin extends DreamBukkitPlatform implements DreamBukkitConfig, DreamPersistence {

    @Getter
    private static AchievementsPlugin achievementsPlugin;

    @Override
    public void load(@NonNull ComponentManager componentManager) {
        achievementsPlugin = this;
    }

    @Override
    public void enable(@NonNull ComponentManager componentManager) {
        this.registerInjectable(BukkitTasker.newPool(this));
        this.registerInjectable(BukkitMenuProvider.create(this));
        this.registerInjectable(BukkitCommandProvider.create(this, this.getInjector()));

        componentManager.registerResolver(CommandComponentResolver.class);
        componentManager.registerResolver(ListenerComponentResolver.class);
        componentManager.registerResolver(RunnableComponentResolver.class);

        componentManager.registerResolver(ConfigurationComponentResolver.class);
        componentManager.registerComponent(MessageConfig.class, messageConfig ->
                this.getInject(BukkitCommandProvider.class).ifPresent(bukkitCommandProvider -> {
                    bukkitCommandProvider.setRequiredPermissionMessage(messageConfig.noPermission.getText());
                    bukkitCommandProvider.setRequiredPlayerMessage(messageConfig.notPlayer.getText());
                }));

        componentManager.registerComponent(PluginConfig.class, pluginConfig -> {
            componentManager.setDebug(pluginConfig.debug);

            this.registerInjectable(pluginConfig.storageConfig);

            componentManager.registerResolver(DocumentPersistenceComponentResolver.class);
            componentManager.registerResolver(DocumentRepositoryComponentResolver.class);

            componentManager.registerComponent(DocumentPersistence.class);

            componentManager.registerComponent(AchievementsUserRepository.class);
            componentManager.registerComponent(AchievementsUserCache.class);

            componentManager.registerComponent(PlayerQuitJoinListener.class);

            componentManager.registerComponent(AchievementMenu.class);
            componentManager.registerComponent(AchievementCommand.class);
            componentManager.registerComponent(AchievementListener.class);

            componentManager.registerComponent(AchievementsUserSaveTask.class);
            componentManager.registerComponent(AchievementsUserSpentTimeTask.class);

            CustomOptional.ofNullable(this.getServer().getPluginManager().getPlugin("FunnyGuilds"))
                    .ifPresentOrElse(plugin -> {
                        componentManager.registerComponent(PointsChangeListener.class);
                        getInject(DreamLogger.class).ifPresent(dreamLogger -> dreamLogger.info("FunnyGuilds found, hooking into FunnyGuilds"));
                    }, () -> getInject(DreamLogger.class).ifPresent(dreamLogger -> dreamLogger.info("FunnyGuilds not found, skipping FunnyGuilds hook")));
            CustomOptional.ofNullable(this.getServer().getPluginManager().getPlugin("PlaceholderAPI"))
                    .ifPresentOrElse(plugin -> {
                        componentManager.registerComponent(AchievementsUserSpentTimePlaceholder.class, PlaceholderExpansion::register);
                        getInject(DreamLogger.class).ifPresent(dreamLogger -> dreamLogger.info("PlaceholderAPI found, hooking into PlaceholderAPI"));
                    }, () -> getInject(DreamLogger.class).ifPresent(dreamLogger -> dreamLogger.info("PlaceholderAPI not found, skipping PlaceholderAPI hook")));
        });
    }

    @Override
    public void disable() {
        // features need to be call when server is stopping
    }

    @Override
    public @NonNull DreamVersion getDreamVersion() {
        return DreamVersion.create("dream-achievements", "1.0-InDEV", "trueman96");
    }

    @Override
    public @NonNull OkaeriSerdesPack getConfigSerdesPack() {
        return registry -> {
            registry.register(new BukkitNoticeSerdes());
            registry.register(new MenuBuilderSerdes());
        };
    }

    @Override
    public @NonNull OkaeriSerdesPack getPersistenceSerdesPack() {
        return registry -> {
            registry.register(new SerdesBukkit());
        };
    }

}
