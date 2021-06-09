package net.arathain.familiarsorcery.item;

import net.arathain.familiarsorcery.FamiliarSorcery;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class FamiliarSorceryItems {
    private static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();
    public static final Item LAPIS_STAVE = create("lapis_stave", new LapisStaveItem(ToolMaterials.GOLD,-1, -1f, new Item.Settings().group(ItemGroup.COMBAT).rarity(Rarity.UNCOMMON).maxDamage(128)));


    private static <T extends Item> T create(String name, T item) {
        ITEMS.put(item, new Identifier(FamiliarSorcery.MODID, name));
        return item;
    }

    public static void init() {
        ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
    }
}
