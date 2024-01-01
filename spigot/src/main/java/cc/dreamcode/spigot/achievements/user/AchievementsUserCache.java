package cc.dreamcode.spigot.achievements.user;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AchievementsUserCache {

    private final Map<UUID, AchievementsUser> achievementsUserMap = new ConcurrentHashMap<>();

    public void add(AchievementsUser achievementsUser) {
        this.achievementsUserMap.put(achievementsUser.getUniqueId(), achievementsUser);
    }

    public void remove(AchievementsUser achievementsUser) {
        this.achievementsUserMap.remove(achievementsUser.getUniqueId());
    }

    public AchievementsUser findByUniqueId(UUID uuid) {
        return this.achievementsUserMap.get(uuid);
    }

    public AchievementsUser findByName(String name) {
        for (AchievementsUser user : this.achievementsUserMap.values()) {
            if (user.getName().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }

    public Collection<AchievementsUser> values() {
        return Collections.unmodifiableCollection(this.achievementsUserMap.values());
    }
}
