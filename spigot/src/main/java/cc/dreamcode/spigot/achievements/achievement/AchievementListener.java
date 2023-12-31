package cc.dreamcode.spigot.achievements.achievement;

import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class AchievementListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
    }

}
