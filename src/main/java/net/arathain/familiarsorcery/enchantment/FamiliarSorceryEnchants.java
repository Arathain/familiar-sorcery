package net.arathain.familiarsorcery.enchantment;

import jdk.jfr.internal.Utils;
import net.arathain.familiarsorcery.FamiliarSorcery;
import net.arathain.familiarsorcery.item.DiamondStaveItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FamiliarSorceryEnchants {
    public static final Map<Enchantment, Identifier> ENCHANTS = new LinkedHashMap<>();
    public static final Enchantment EXPLOSION = create("explosion", new ExplosionEnchantment());

    private static <T extends Enchantment> T create(String name, T enchant) {
        ENCHANTS.put(enchant, new Identifier(FamiliarSorcery.MODID, name));
        return enchant;
    }
    public static void init() {
        ENCHANTS.keySet().forEach(enchant -> Registry.register(Registry.ENCHANTMENT, ENCHANTS.get(enchant), enchant));
    }
}
