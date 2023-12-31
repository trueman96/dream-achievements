package cc.dreamcode.spigot.achievements.user;

import eu.okaeri.persistence.repository.DocumentRepository;
import eu.okaeri.persistence.repository.annotation.DocumentCollection;
import lombok.NonNull;

import java.util.Optional;
import java.util.UUID;

@DocumentCollection(path = "user", keyLength = 36)
public interface AchievementsUserRepository extends DocumentRepository<UUID, AchievementsUser> {

    default Optional<AchievementsUser> findByName(@NonNull String name, boolean ignoreCase) {
        return this.streamAll()
                .filter(user -> ignoreCase
                        ? user.getName().equalsIgnoreCase(name)
                        : user.getName().equals(name))
                .findFirst();
    }

}
