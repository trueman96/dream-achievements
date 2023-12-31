package cc.dreamcode.spigot.achievements.achievement;

import cc.dreamcode.command.annotations.RequiredPlayer;
import cc.dreamcode.command.bukkit.BukkitCommand;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@RequiredPlayer
public class AchievementCommand extends BukkitCommand {

    private final AchievementMenu achievementMenu;

    @Inject
    public AchievementCommand(AchievementMenu achievementMenu) {
        super("osiagniecia", "os", "osiagniecie", "achievements");
        this.achievementMenu = achievementMenu;
    }

    @Override
    public void content(@NonNull CommandSender sender, @NonNull String[] args) {
        this.achievementMenu.open((Player) sender);
    }

    @Override
    public List<String> tab(@NonNull CommandSender sender, @NonNull String[] args) {
        return new ArrayList<>();
    }
}
