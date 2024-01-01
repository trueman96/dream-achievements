package cc.dreamcode.spigot.achievements.config;

import cc.dreamcode.notice.minecraft.MinecraftNoticeType;
import cc.dreamcode.notice.minecraft.bukkit.BukkitNotice;
import cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.*;

@Configuration(
        child = "message.yml"
)
@Headers({
        @Header("## dream-achievements (Message-Config) ##"),
        @Header("Dostepne type: (DO_NOT_SEND, CHAT, ACTION_BAR, SUBTITLE, TITLE, TITLE_SUBTITLE)")
})
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class MessageConfig extends OkaeriConfig {

    public BukkitNotice usage = new BukkitNotice(MinecraftNoticeType.CHAT, "&7Poprawne uzycie: &c{usage}");
    public BukkitNotice noPermission = new BukkitNotice(MinecraftNoticeType.CHAT, "&4Nie posiadasz uprawnien.");
    public BukkitNotice noPlayer = new BukkitNotice(MinecraftNoticeType.CHAT, "&4Podanego gracza &cnie znaleziono.");
    public BukkitNotice playerIsOffline = new BukkitNotice(MinecraftNoticeType.CHAT, "&4Podany gracz &cjest offline.");
    public BukkitNotice notPlayer = new BukkitNotice(MinecraftNoticeType.CHAT, "&4Nie jestes graczem.");
    public BukkitNotice notNumber = new BukkitNotice(MinecraftNoticeType.CHAT, "&4Podana liczba &cnie jest cyfra.");
    public BukkitNotice playerIsMe = new BukkitNotice(MinecraftNoticeType.CHAT, "&4Nie rob tego &cna sobie.");


    public BukkitNotice invalidItem = BukkitNotice.of(MinecraftNoticeType.CHAT, "&cPodany item jest nieprawidłowy, prawdopodbnie jest powietrzem.");
    public BukkitNotice noAchievementsForType = BukkitNotice.of(MinecraftNoticeType.CHAT, "&cNie znaleziono zadnych osiagniec dla typu {type}.");
    public BukkitNotice receivedReward = BukkitNotice.of(MinecraftNoticeType.CHAT, "&aOtrzymano nagrode {reward} za osiagniecie {achievement}.");
    public BukkitNotice cannotReceive = BukkitNotice.of(MinecraftNoticeType.CHAT, "&cNie mozesz odebrać nagrody {reward} za osiagniecie {achievement} &f{progress}&7/&f{required}&c.");
}
