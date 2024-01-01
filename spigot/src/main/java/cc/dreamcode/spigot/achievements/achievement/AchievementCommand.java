package cc.dreamcode.spigot.achievements.achievement;

import cc.dreamcode.command.annotations.RequiredPermission;
import cc.dreamcode.command.annotations.RequiredPlayer;
import cc.dreamcode.command.bukkit.BukkitCommand;
import cc.dreamcode.spigot.achievements.config.MessageConfig;
import cc.dreamcode.spigot.achievements.config.PluginConfig;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.tasker.core.Tasker;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@RequiredPlayer
public class AchievementCommand extends BukkitCommand {

    private final AchievementMenu achievementMenu;

    @Inject
    public AchievementCommand(AchievementMenu achievementMenu) {
        super("osiagniecia", "os", "osiagniecie", "achievements");
        this.achievementMenu = achievementMenu;
        registerSubcommand(AchievementReloadCommand.class);
        registerSubcommand(AchievementPremiumCaseItemCommand.class);
    }

    @Override
    public void content(@NonNull CommandSender sender, @NonNull String[] args) {
        this.achievementMenu.open((Player) sender);
    }

    @Override
    public List<String> tab(@NonNull CommandSender sender, @NonNull String[] args) {
        return new ArrayList<>();
    }

    @RequiredPermission
    public static class AchievementReloadCommand extends BukkitCommand {

        private final PluginConfig pluginConfig;
        private final Tasker tasker;

        @Inject
        public AchievementReloadCommand(PluginConfig pluginConfig, Tasker tasker) {
            super("reload");
            this.pluginConfig = pluginConfig;
            this.tasker = tasker;
        }

        @Override
        public void content(@NonNull CommandSender sender, @NonNull String[] args) {
            this.tasker.newSharedChain("config-reload")
                    .async(() -> this.pluginConfig.load(true))
                    .sync(() -> sender.sendMessage("&aPrzeładowano config osiągnięć!"))
                    .execute();
        }

        @Override
        public List<String> tab(@NonNull CommandSender sender, @NonNull String[] args) {
            return null;
        }
    }

    @RequiredPlayer
    @RequiredPermission
    public static class AchievementPremiumCaseItemCommand extends BukkitCommand {

        private final MessageConfig messageConfig;
        private final PluginConfig pluginConfig;
        private final Tasker tasker;

        @Inject
        public AchievementPremiumCaseItemCommand(MessageConfig messageConfig, PluginConfig pluginConfig, Tasker tasker) {
            super("case");
            this.messageConfig = messageConfig;
            this.pluginConfig = pluginConfig;
            this.tasker = tasker;
        }

        @Override
        public void content(@NonNull CommandSender sender, @NonNull String[] args) {
            Player source = (Player) sender;
            ItemStack itemInHand = source.getItemInHand();
            if (itemInHand == null || itemInHand.getType().equals(Material.AIR)) {
                this.messageConfig.invalidItem.send(source);
                return;
            }

            this.pluginConfig.premiumCaseItemStack = itemInHand;

            this.tasker.newSharedChain("config-reload")
                    .async(this.pluginConfig::save)
                    .sync(() -> sender.sendMessage("&aZapisano itemek!"))
                    .execute();
        }

        @Override
        public List<String> tab(@NonNull CommandSender sender, @NonNull String[] args) {
            return null;
        }
    }
}
