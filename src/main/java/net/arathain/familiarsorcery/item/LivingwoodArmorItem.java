package net.arathain.familiarsorcery.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.ThornsEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Random;

public class LivingwoodArmorItem extends ArmorItem {
    public LivingwoodArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            Random random = world.getRandom();
            if (random.nextInt(16) == 0) {
                for (int i = 0; i < 8; i++) {
                    stack.damage(-10, random, player);
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }


}
