package net.arathain.familiarsorcery.enchantment;

import net.arathain.familiarsorcery.item.AbstractStaveItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class ExplosionEnchantment extends Enchantment {
    //it's megumin time
    public ExplosionEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof AbstractStaveItem;
    }

    @Override
    public int getMaxLevel() {
        return 9;
    }
}
