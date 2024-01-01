package cc.dreamcode.spigot.achievements.config;

import cc.dreamcode.menu.bukkit.BukkitMenuBuilder;
import cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import cc.dreamcode.platform.persistence.StorageConfig;
import cc.dreamcode.spigot.achievements.achievement.Achievement;
import cc.dreamcode.spigot.achievements.achievement.AchievementReward;
import cc.dreamcode.spigot.achievements.achievement.AchievementType;
import cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import com.cryptomorin.xseries.XMaterial;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration(
        child = "config.yml"
)
@Header({
        "## dream-achievements (Main-Config) ##",
        "##",
        "## Placeholders:",
        "## %achievements-user_spent-time% - Spędzony czas na serwerze",
})
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class PluginConfig extends OkaeriConfig {
    @Comment("Debug pokazuje dodatkowe informacje do konsoli. Lepiej wylaczyc. :P")
    public boolean debug = true;

    @Comment("Uzupelnij ponizsze menu danymi.")
    public StorageConfig storageConfig = new StorageConfig("dreamachievements");

    public Map<AchievementType, List<Achievement>> achievementMap;

    @Comment({
            "Pamiętaj by zmienić sloty jeszcze poniżej!!",
            "Placeholder 'available-receive' zwraca to co masz w availableReceive, a 'not-ready-receive' to co masz w notReadyReceive.",
    })
    public BukkitMenuBuilder achievementMenuBuilder;

    public Map<Integer, AchievementType> achievementTypeSlot = new HashMap<>();

    public ItemStack premiumCaseItemStack = ItemBuilder.of(XMaterial.CHEST.parseItem())
            .setName("&7Skrzynka premium")
            .setLore(
                    "&8&l| &7Kliknij aby otworzyć skrzynkę premium!"
            )
            .toItemStack();

    public String availableReceive = "&aMożliwość odbioru nagrody, kliknij aby odebrać!", notReadyReceive = "&cNagroda nie jest jeszcze dostępna.";

    public PluginConfig() {
        int i = 10;
        for (AchievementType value : AchievementType.values()) {
            this.achievementTypeSlot.put(i, value);
            i++;
        }
        this.achievementMenuBuilder = new BukkitMenuBuilder("&0&lOsiągnięcia", 3, new MapBuilder<Integer, ItemStack>()
                .putAll(IntStream.range(0, 27).boxed()
                        .filter(j -> !this.achievementTypeSlot.containsKey(j))
                        .collect(
                                Collectors.toMap(
                                        j -> j,
                                        j -> XMaterial.BLACK_STAINED_GLASS_PANE.parseItem(),
                                        (a, b) -> b)
                        )
                )
                .put(10, ItemBuilder.of(XMaterial.DIAMOND_SWORD.parseMaterial())
                        .setName("&cZabójstwa #{id}")
                        .setLore(
                                "&8&l| &7Postęp: &f{progress}&7/&f{required}",
                                "&8&l| &7Nagroda: &f{reward}",
                                "",
                                "{available-receive}"
                        )
                        .toItemStack())
                .put(11, ItemBuilder.of(XMaterial.SHIELD.parseMaterial())
                        .setName("&cRanking #{id}")
                        .setLore(
                                "&8&l| &7Postęp: &f{progress}&7/&f{required}",
                                "&8&l| &7Nagroda: &f{reward}",
                                "",
                                "{available-receive}"
                        )
                        .toItemStack())
                .put(12, ItemBuilder.of(XMaterial.SKELETON_SKULL.parseItem())
                        .setName("&fŚmierci #{id}")
                        .setLore(
                                "&8&l| &7Postęp: &f{progress}&7/&f{required}",
                                "&8&l| &7Nagroda: &f{reward}",
                                "",
                                "{available-receive}"
                        )
                        .toItemStack())
                .put(13, ItemBuilder.of(XMaterial.CLOCK.parseItem())
                        .setName("&bSpędzony czas #{id}")
                        .setLore(
                                "&8&l| &7Postęp: &f{progress}&7/&f{required}",
                                "&8&l| &7Nagroda: &f{reward}",
                                "",
                                "{available-receive}"
                        )
                        .toItemStack())
                .put(14, ItemBuilder.of(XMaterial.DIAMOND_PICKAXE.parseItem())
                        .setName("&7Rozkopane bloki #{id}")
                        .setLore(
                                "&8&l| &7Postęp: &f{progress}&7/&f{required}",
                                "&8&l| &7Nagroda: &f{reward}",
                                "",
                                "{available-receive}"
                        )
                        .toItemStack())
                .put(15, ItemBuilder.of(XMaterial.TOTEM_OF_UNDYING.parseItem())
                        .setName("&7Zbite totemy #{id}")
                        .setLore(
                                "&8&l| &7Postęp: &f{progress}&7/&f{required}",
                                "&8&l| &7Nagroda: &f{reward}",
                                "",
                                "{available-receive}"
                        )
                        .toItemStack())
                .put(16, ItemBuilder.of(XMaterial.CHEST.parseItem())
                        .setName("&7Otworzone skrzynki premium #{id}")
                        .setLore(
                                "&8&l| &7Postęp: &f{progress}&7/&f{required}",
                                "&8&l| &7Nagroda: &f{reward}",
                                "",
                                "{available-receive}"
                        )
                        .toItemStack())
                .build());
        this.achievementMap = new HashMap<>();
        for (AchievementType value : AchievementType.values()) {
            if (value.equals(AchievementType.SPEND_TIME)) {
                this.achievementMap.put(value, Arrays.asList(
                        Achievement.builder()
                                .id(1)
                                .type(value)
                                .reward(AchievementReward.builder()
                                        .friendlyName("1x Kompas")
                                        .itemStack(ItemBuilder.of(Material.COMPASS)
                                                .toItemStack()
                                        )
                                        .build())
                                .required(TimeUnit.HOURS.toMillis(1))
                                .build(),
                        Achievement.builder()
                                .id(2)
                                .type(value)
                                .reward(AchievementReward.builder()
                                        .friendlyName("1x Kompas")
                                        .itemStack(ItemBuilder.of(Material.COMPASS)
                                                .toItemStack()
                                        )
                                        .build())
                                .required(TimeUnit.HOURS.toMillis(2))
                                .build()
                ));
                continue;
            }
            this.achievementMap.put(value, Arrays.asList(
                    Achievement.builder()
                            .id(1)
                            .type(value)
                            .reward(AchievementReward.builder()
                                    .friendlyName("1x Kompas")
                                    .itemStack(ItemBuilder.of(Material.COMPASS)
                                            .toItemStack()
                                    )
                                    .build())
                            .required(1)
                            .build(),
                    Achievement.builder()
                            .id(2)
                            .type(value)
                            .reward(AchievementReward.builder()
                                    .friendlyName("1x Kompas")
                                    .itemStack(ItemBuilder.of(Material.COMPASS)
                                            .toItemStack()
                                    )
                                    .build())
                            .required(2)
                            .build()
            ));
        }
    }

    public List<Achievement> findByType(AchievementType type) {
        return this.achievementMap.get(type);
    }

}