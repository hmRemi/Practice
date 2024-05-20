package com.hysteria.practice.game.kit;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.queue.Queue;
import com.hysteria.practice.utilities.InventoryUtil;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.PotionUtil;
import com.hysteria.practice.utilities.file.type.BasicConfigurationFile;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Remi
 * @project HyPractice
 * @date 5/20/2024
 */
@Getter

public class KitRepository {

    private final List<Kit> kits = new ArrayList<>();

    public void loadKits() {
        FileConfiguration config = HyPractice.get().getKitsConfig().getConfiguration();

        config.getConfigurationSection("kits").getKeys(false).forEach(kitName -> {
            String path = "kits." + kitName;
            Kit kit = new Kit(kitName);
            loadBasicKitInfo(config, path, kit);
            loadKitLoadoout(config, path, kit);
            loadKitGameRules(config, path, kit);
            loadKitEditRules(config, path, kit);
            kits.add(kit);
            addKitToQueue(kit);
        });
    }

    /**
     * Save a kit to the kits.yml file
     *
     * @param kit the kit to save
     */
    public void saveKit(Kit kit) {
        String path = "kits." + kit.getName();

        FileConfiguration config = HyPractice.get().getKitsConfig().getConfiguration();
        saveBasicKitInfo(config, path, kit);
        saveKitLoadout(config, path, kit);
        saveKitGameRules(config, path, kit);
        saveKitEditRules(config, path, kit);

        HyPractice.get().getKitsConfig().save();
        HyPractice.get().getKitsConfig().reload();
    }

    /**
     * Delete a kit from the kits.yml file and memory
     *
     * @param kit the kit to delete
     */
    public void deleteKit(Kit kit) {
        String path = "kits." + kit.getName();

        BasicConfigurationFile configFile = HyPractice.get().getKitsConfig();
        configFile.getConfiguration().set(path, null);
        configFile.save();
        configFile.reload();

        kits.remove(kit);
    }

    /**
     * Get a kit by name
     *
     * @param name the name of the kit
     * @return the kit
     */
    public Kit getKitByName(String name) {
        return kits.stream().filter(kit -> kit.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /**
     * Get a kit by kit
     *
     * @param kit the kit
     * @return the kit
     */
    public Kit getKit(Kit kit) {
        return kits.stream().filter(kit1 -> kit1.equals(kit)).findFirst().orElse(null);
    }

    /**
     * Load the basic kit info from the kits.yml file
     *
     * @param config the config file
     * @param path   the path to the kit
     * @param kit    the kit
     */
    private void loadBasicKitInfo(FileConfiguration config, String path, Kit kit) {
        kit.setEnabled(config.getBoolean(path + ".enabled"));
        kit.setSlot(config.getInt(path + ".slot"));
        kit.setDisplayName(config.getString(path + ".displayName"));
        kit.setDisplayIcon(new ItemBuilder(Material.valueOf(config.getString(path + ".icon.material")))
                .durability(config.getInt(path + ".icon.durability"))
                .build());
    }

    /**
     * Save the basic kit info to the kits.yml file
     *
     * @param config the config file
     * @param path   the path to the kit
     * @param kit    the kit
     */
    private void saveBasicKitInfo(FileConfiguration config, String path, Kit kit) {
        config.set(path + ".enabled", kit.isEnabled());
        config.set(path + ".slot", kit.getSlot());
        config.set(path + ".displayName", kit.getDisplayName());
        config.set(path + ".icon.material", kit.getDisplayIcon().getType().name());
        config.set(path + ".icon.durability", kit.getDisplayIcon().getDurability());
    }

    /**
     * Load the kit loadout from the kits.yml file
     *
     * @param config the config file
     * @param path   the path to the kit
     * @param kit    the kit
     */
    private void loadKitLoadoout(FileConfiguration config, String path, Kit kit) {
        try {
            kit.getKitLoadout().setArmor(InventoryUtil.itemStackArrayFromBase64(config.getString(path + ".loadout.armor")));
            kit.getKitLoadout().setContents(InventoryUtil.itemStackArrayFromBase64(config.getString(path + ".loadout.contents")));
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("Error loading kit loadout for " + kit.getName());
        }
    }

    /**
     * Save the kit loadout to the kits.yml file
     *
     * @param config the config file
     * @param path   the path to the kit
     * @param kit    the kit
     */
    private void saveKitLoadout(FileConfiguration config, String path, Kit kit) {
        config.set(path + ".loadout.armor", InventoryUtil.itemStackArrayToBase64(kit.getKitLoadout().getArmor()));
        config.set(path + ".loadout.contents", InventoryUtil.itemStackArrayToBase64(kit.getKitLoadout().getContents()));
    }

    /**
     * Load the kit game rules from the kits.yml file
     *
     * @param config the config file
     * @param path   the path to the kit
     * @param kit    the kit
     */
    private void loadKitGameRules(FileConfiguration config, String path, Kit kit) {
        kit.getGameRules().setBuild(config.getBoolean(path + ".game-rules.allow-build"));
        kit.getGameRules().setSpleef(config.getBoolean(path + ".game-rules.spleef"));
        kit.getGameRules().setParkour(config.getBoolean(path + ".game-rules.parkour"));
        kit.getGameRules().setSumo(config.getBoolean(path + ".game-rules.sumo"));
        kit.getGameRules().setSoup(config.getBoolean(path + ".game-rules.soup"));
        kit.getGameRules().setBridge(config.getBoolean(path + ".game-rules.bridge"));
        kit.getGameRules().setHealthRegeneration(config.getBoolean(path + ".game-rules.health-regeneration"));
        kit.getGameRules().setNofalldamage(config.getBoolean(path + ".game-rules.nofalldamage"));
        kit.getGameRules().setAntiFood(config.getBoolean(path + ".game-rules.antifood"));
        kit.getGameRules().setNodamage(config.getBoolean(path + ".game-rules.nodamage"));
        kit.getGameRules().setShowHealth(config.getBoolean(path + ".game-rules.show-health"));
        kit.getGameRules().setHitDelay(config.getInt(path + ".game-rules.hit-delay"));
        kit.getGameRules().setRanked(config.getBoolean(path + ".game-rules.ranked"));
        kit.getGameRules().setHcf(config.getBoolean(path + ".game-rules.hcf"));
        kit.getGameRules().setHcftrap(config.getBoolean(path + ".game-rules.hcftrap"));
        kit.getGameRules().setBattlerush(config.getBoolean(path + ".game-rules.battlerush"));
        kit.getGameRules().setLives(config.getBoolean(path + ".game-rules.lives"));
        kit.getGameRules().setBoxing(config.getBoolean(path + ".game-rules.boxing"));
        kit.getGameRules().setBedFight(config.getBoolean(path + ".game-rules.bedfight"));
        kit.getGameRules().setKbProfile(config.getString(path + ".game-rules.kbprofile"));
        config.getStringList(path + ".game-rules.effects")
                .stream()
                .map(PotionUtil::convertStringToPotionEffect)
                .collect(Collectors.toList())
                .forEach(potionEffect -> kit.getGameRules().getEffects().add(potionEffect));
    }

    /**
     * Save the kit game rules to the kits.yml file
     *
     * @param config the config file
     * @param path   the path to the kit
     * @param kit    the kit
     */
    private void saveKitGameRules(FileConfiguration config, String path, Kit kit) {
        config.set(path + ".game-rules.allow-build", kit.getGameRules().isBuild());
        config.set(path + ".game-rules.spleef", kit.getGameRules().isSpleef());
        config.set(path + ".game-rules.parkour", kit.getGameRules().isParkour());
        config.set(path + ".game-rules.soup", kit.getGameRules().isSoup());
        config.set(path + ".game-rules.sumo", kit.getGameRules().isSumo());
        config.set(path + ".game-rules.bridge", kit.getGameRules().isBridge());
        config.set(path + ".game-rules.health-regeneration", kit.getGameRules().isHealthRegeneration());
        config.set(path + ".game-rules.antifood", kit.getGameRules().isAntiFood());
        config.set(path + ".game-rules.nofalldamage", kit.getGameRules().isNofalldamage());
        config.set(path + ".game-rules.lives", kit.getGameRules().isLives());
        config.set(path + ".game-rules.show-health", kit.getGameRules().isShowHealth());
        config.set(path + ".game-rules.hit-delay", kit.getGameRules().getHitDelay());
        config.set(path + ".game-rules.ranked", kit.getGameRules().isRanked());
        config.set(path + ".game-rules.hcf", kit.getGameRules().isHcf());
        config.set(path + ".game-rules.hcftrap", kit.getGameRules().isHcftrap());
        config.set(path + ".game-rules.battlerush", kit.getGameRules().isBattlerush());
        config.set(path + ".game-rules.effects", kit.getGameRules().getEffects().stream().map(PotionUtil::convertPotionEffectToString).collect(Collectors.toList()));
        config.set(path + ".game-rules.boxing", kit.getGameRules().isBoxing());
        config.set(path + ".game-rules.bedfight", kit.getGameRules().isBedFight());
        config.set(path + ".game-rules.kbprofile", kit.getGameRules().getKbProfile());
    }

    /**
     * Load the kit edit rules from the kits.yml file
     *
     * @param config the config file
     * @param path   the path to the kit
     * @param kit    the kit
     */
    private void loadKitEditRules(FileConfiguration config, String path, Kit kit) {
        kit.getEditRules().setAllowPotionFill(config.getBoolean(".edit-rules.allow-potion-fill"));

        if (config.getConfigurationSection(path + ".edit-rules.items") != null) {
            for (String itemKey : config.getConfigurationSection(path + ".edit-rules.items").getKeys(false)) {
                kit.getEditRules().getEditorItems().add(new ItemBuilder(Material.valueOf(
                        config.getString(path + ".edit-rules.items." + itemKey + ".material")))
                        .durability(config.getInt(path + ".edit-rules.items." + itemKey + ".durability"))
                        .amount(config.getInt(path + ".edit-rules.items." + itemKey + ".amount"))
                        .build());
            }
        }
    }

    /**
     * Save the kit edit rules to the kits.yml file
     *
     * @param config the config file
     * @param path   the path to the kit
     * @param kit    the kit
     */
    private void saveKitEditRules(FileConfiguration config, String path, Kit kit) {
        config.set(path + ".edit-rules.allow-potion-fill", kit.getEditRules().isAllowPotionFill());
        List<ItemStack> editorItems = kit.getEditRules().getEditorItems();
        for (int i = 0; i < editorItems.size(); i++) {
            ItemStack item = editorItems.get(i);
            config.set(path + ".edit-rules.items." + i + ".material", item.getType().name());
            config.set(path + ".edit-rules.items." + i + ".durability", item.getDurability());
            config.set(path + ".edit-rules.items." + i + ".amount", item.getAmount());
        }
    }

    /**
     * Add a kit to the queue
     *
     * @param kit the kit to add
     */
    private void addKitToQueue(Kit kit) {
        if (!kit.isEnabled()) return;
        new Queue(kit, false);
        if (kit.getGameRules().isRanked()) new Queue(kit, true);
    }
}
