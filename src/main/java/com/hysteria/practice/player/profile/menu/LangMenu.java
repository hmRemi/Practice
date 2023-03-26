package com.hysteria.practice.player.profile.menu;

import com.google.common.collect.Maps;
import com.hysteria.practice.HyPractice;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.file.language.Lang;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.chat.CC;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

public class LangMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&4Select Language";
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(HyPractice.get().getMenuConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(HyPractice.get().getMainConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();
        this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);

        buttons.put(getSlot(3, 1), new SpanishButton());
        buttons.put(getSlot(5, 1), new EnglishButton());
        return buttons;
    }


    private static class SpanishButton extends Button{

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(getSkull("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJiZDQ1MjE5ODMzMDllMGFkNzZjMWVlMjk4NzQyODc5NTdlYzNkOTZmOGQ4ODkzMjRkYThjODg3ZTQ4NWVhOCJ9fX0="))
                .name("&4Spanish")
                .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            Profile profile = Profile.get(player.getUniqueId());
            profile.setLocale(Lang.SPANISH);
            player.sendMessage(CC.translate("&7Ahora todos los mensajes estaran en español."));
        }
    }

    private static class EnglishButton extends Button{

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(getSkull("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNhYzk3NzRkYTEyMTcyNDg1MzJjZTE0N2Y3ODMxZjY3YTEyZmRjY2ExY2YwY2I0YjM4NDhkZTZiYzk0YjQifX19"))
                .name("&4English")
                .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            Profile profile = Profile.get(player.getUniqueId());
            profile.setLocale(Lang.ENGLISH);
            player.sendMessage(CC.translate("&7All messages will now be in English."));
        }
    }

    public static ItemStack getSkull(String url) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        if (url == null || url.isEmpty())
            return skull;
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        profileField.setAccessible(true);
        try {
            profileField.set(skullMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        skull.setItemMeta(skullMeta);
        return skull;
    }
}