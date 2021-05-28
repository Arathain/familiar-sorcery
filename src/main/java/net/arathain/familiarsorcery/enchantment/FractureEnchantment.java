package net.arathain.familiarsorcery.enchantment;

import net.arathain.familiarsorcery.item.AbstractStaveItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.MultishotEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class FractureEnchantment extends Enchantment {

    protected FractureEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof AbstractStaveItem;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != FamiliarSorceryEnchants.EXPLOSION && other != FamiliarSorceryEnchants.ICING;
    }


}
