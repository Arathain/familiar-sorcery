package net.arathain.familiarsorcery.enchantment;

import net.arathain.familiarsorcery.FamiliarSorcery;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class FamiliarSorceryEnchants {
    public static final Map<Enchantment, Identifier> ENCHANTS = new LinkedHashMap<>();

    public static final Enchantment EXPLOSION = create("explosion", new ExplosionEnchantment());
    public static final Enchantment ICING = create("icing", new IcicleEnchantment());
    public static final Enchantment FRACTURE = create("fracture", new FractureEnchantment());
    public static final Enchantment METALLICISE = create("metallicise", new MetalliciseEnchantment());

    private static <T extends Enchantment> T create(String name, T enchant) {
        ENCHANTS.put(enchant, new Identifier(FamiliarSorcery.MODID, name));
        return enchant;
    }
    public static void init() {
        ENCHANTS.keySet().forEach(enchant -> Registry.register(Registry.ENCHANTMENT, ENCHANTS.get(enchant), enchant));
    }
}
