package net.arathain.familiarsorcery.enchantment;

import net.arathain.familiarsorcery.item.AbstractStaveItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class IcicleEnchantment extends Enchantment {
        //bloons ice monki
        public IcicleEnchantment() {
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
    }

