package com.hysteria.practice.essentials.abilities;

import com.hysteria.practice.essentials.abilities.impl.*;
import com.hysteria.practice.HyPractice;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.TaskUtil;
import com.hysteria.practice.utilities.chat.CC;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

@Getter
public class AbilityManager {

    private final Cookie cookie;
    private final Rocket rocket;
    private final TimeWarp timeWarp;
    private final GuardianAngel guardianAngel;
    private final Combo combo;
    private final TankIngot tankIngot;
    private final EffectDisabler effectDisabler;
    private final Scrambler scrambler;
    private final Strength strength;
    private final SwapperAxe swapperAxe;
    private final Switcher switcher;
    private final NinjaStar ninjaStar;
    private final PocketBard pocketBard;
    private final AntiTrapper antitrapper;
    private final LuckyIngot luckyIngot;

    public AbilityManager() {
        this.guardianAngel = new GuardianAngel();
        this.combo = new Combo();
        this.antitrapper = new AntiTrapper();
        this.effectDisabler = new EffectDisabler();
        this.ninjaStar = new NinjaStar();
        this.pocketBard = new PocketBard();
        this.scrambler = new Scrambler();
        this.strength = new Strength();
        this.swapperAxe = new SwapperAxe();
        this.switcher = new Switcher();
        this.tankIngot = new TankIngot();
        this.rocket = new Rocket();
        this.cookie = new Cookie();
        this.timeWarp = new TimeWarp();
        this.luckyIngot = new LuckyIngot();
    }


    public void load() {
        Ability.getAbilities().forEach(Ability::register);
    }

    public ItemStack getAbility(String ability, int amount) {
        return new ItemBuilder(getMaterial(ability))
                .amount(amount)
                .data(getData(ability))
                .name(getDisplayName(ability))
                .lore(getDescription(ability))
                .build();
    }

    public String getDisplayName(String ability) {
        return HyPractice.get().getAbilityConfig().getString( ability + ".ICON.DISPLAYNAME");
    }

    public List<String> getDescription(String ability) {
        return HyPractice.get().getAbilityConfig().getStringList( ability + ".ICON.DESCRIPTION");
    }

    public Material getMaterial(String ability) {
        return Material.valueOf(HyPractice.get().getAbilityConfig().getString(ability + ".ICON.MATERIAL"));
    }

    public int getData(String ability) {
        return HyPractice.get().getAbilityConfig().getInteger(ability + ".ICON.DATA");
    }

    public int getCooldown(String ability) {
        return HyPractice.get().getAbilityConfig().getInteger(ability + ".COOLDOWN");
    }

    public Set<String> getAbilities() {
        return HyPractice.get().getAbilityConfig().getConfiguration().getKeys(false);
    }

    public void giveAbility(CommandSender sender, Player player, String key, String abilityName, int amount) {
        player.getInventory().addItem(this.getAbility(key, amount));
        if (player == sender) {
            CC.message(player, HyPractice.get().getAbilityConfig().getString("RECEIVED_ABILITY")
                    .replace("%ABILITY%", abilityName)
                    .replace("%AMOUNT%", String.valueOf(amount)));
        }
        else {
            CC.message(player, HyPractice.get().getAbilityConfig().getString("RECEIVED_ABILITY")
                    .replace("%ABILITY%", abilityName)
                    .replace("%AMOUNT%", String.valueOf(amount)));
            CC.sender(sender, HyPractice.get().getAbilityConfig().getString("GIVE_ABILITY")
                    .replace("%ABILITY%", abilityName)
                    .replace("%AMOUNT%", String.valueOf(amount))
                    .replace("%PLAYER%", player.getName()));
        }
    }

    public void playerMessage(Player player, String ability) {
        String displayName = getDisplayName(ability);
        String cooldown = String.valueOf(getCooldown(ability));

        HyPractice.get().getAbilityConfig().getStringList(ability + ".MESSAGE.PLAYER").forEach(
                message -> CC.message(player, message
                        .replace("%ABILITY%", displayName)
                        .replace("%COOLDOWN%", cooldown)));
    }

    public void targetMessage(Player target, Player player, String ability) {
        String displayName = getDisplayName(ability);

        HyPractice.get().getAbilityConfig().getStringList(ability + ".MESSAGE.TARGET").forEach(
                message -> CC.message(target, message
                        .replace("%ABILITY%", displayName)
                        .replace("%PLAYER%", player.getName())
                        .replace("%TARGET%", target.getName())));
    }

    public void cooldown(Player player, String abilityName, String cooldown) {
        CC.message(player, HyPractice.get().getAbilityConfig().getString("STILL_ON_COOLDOWN")
                .replace("%ABILITY%", abilityName)
                .replace("%COOLDOWN%", cooldown));
    }

    public void cooldownExpired(Player player, String abilityName, String ability) {
        TaskUtil.runLaterAsync(() ->
                CC.message(player, HyPractice.get().getAbilityConfig().getString("COOLDOWN_EXPIRED")
                        .replace("%ABILITY%", abilityName)), getCooldown(ability) * 20L);
    }
}
