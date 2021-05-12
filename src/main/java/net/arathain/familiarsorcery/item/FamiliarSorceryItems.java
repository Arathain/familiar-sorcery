package net.arathain.familiarsorcery.item;

import net.arathain.familiarsorcery.FamiliarSorcery;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
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
    public static final Item DIAMOND_STAVE = create("lapis_stave", new DiamondStaveItem(ToolMaterials.DIAMOND,0, -2f, new Item.Settings().group(ItemGroup.TOOLS).rarity(Rarity.UNCOMMON).maxDamage(128).fireproof()));
    public static final ArmorMaterial LIVINGWOOD_ARMOUR = new LivingwoodArmorMaterial();
    public static final Item LIVINGWOOD_HELMET = create( "livingwood_helmet", new LivingwoodArmorItem(LIVINGWOOD_ARMOUR, EquipmentSlot.HEAD, new Item.Settings().group(ItemGroup.COMBAT)));
    public static final Item LIVINGWOOD_CHESTPLATE = create( "livingwood_chestplate", new LivingwoodArmorItem(LIVINGWOOD_ARMOUR, EquipmentSlot.CHEST, new Item.Settings().group(ItemGroup.COMBAT)));
    public static final Item LIVINGWOOD_LEGGINGS = create( "livingwood_leggings", new LivingwoodArmorItem(LIVINGWOOD_ARMOUR, EquipmentSlot.LEGS, new Item.Settings().group(ItemGroup.COMBAT)));
    public static final Item LIVINGWOOD_BOOTS = create( "livingwood_boots", new LivingwoodArmorItem(LIVINGWOOD_ARMOUR, EquipmentSlot.FEET, new Item.Settings().group(ItemGroup.COMBAT)));


    private static <T extends Item> T create(String name, T item) {
        ITEMS.put(item, new Identifier(FamiliarSorcery.MODID, name));
        return item;
    }

    public static void init() {
        ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
    }
}
