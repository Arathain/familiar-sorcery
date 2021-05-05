package net.arathain.familiarsorcery.item;

import net.arathain.familiarsorcery.enchantment.FamiliarSorceryEnchants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.Objects;
import java.util.Set;

import static net.arathain.familiarsorcery.enchantment.FamiliarSorceryEnchants.EXPLOSION;

public class DiamondStaveItem extends SwordItem implements AbstractStaveItem {
    public DiamondStaveItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof PlayerEntity) {
            Hand hand = user.getActiveHand();
            int boomlvl = EnchantmentHelper.getLevel(FamiliarSorceryEnchants.EXPLOSION, user.getStackInHand(hand));
            MinecraftClient client = MinecraftClient.getInstance();
            HitResult hit = client.crosshairTarget;

            switch(Objects.requireNonNull(hit).getType()) {
                case MISS:
                    //nothing near enough
                    break;
                case BLOCK:
                    BlockHitResult blockHit = (BlockHitResult) hit;
                    BlockPos blockPos = blockHit.getBlockPos();
                    BlockState blockState = client.world.getBlockState(blockPos);
                    Block block = blockState.getBlock();
                    if (this.getDefaultStack().getEnchantments().contains(EXPLOSION) && (boomlvl <= 8)) {

                        world.createExplosion(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), boomlvl, Explosion.DestructionType.BREAK);
                        stack.damage(1, user, stackUser -> stackUser.sendToolBreakStatus(hand));

                    }
                    if (this.getDefaultStack().getEnchantments().contains(Enchantments.CHANNELING) && (boomlvl <= 8)) {
                        LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                        lightningEntity.updatePosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                        world.spawnEntity(lightningEntity);
                        stack.damage(1, user, stackUser -> stackUser.sendToolBreakStatus(hand));

                    }
                    if (this.getDefaultStack().getEnchantments().contains(Enchantments.CHANNELING) && (boomlvl <= 8)) {
                        LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                        lightningEntity.updatePosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                        world.spawnEntity(lightningEntity);
                        stack.damage(1, user, stackUser -> stackUser.sendToolBreakStatus(hand));

                    }
                    break;
                case ENTITY:
                    EntityHitResult entityHit = (EntityHitResult) hit;
                    Entity entity = entityHit.getEntity();
                    BlockPos blockPos1 = entityHit.getEntity().getBlockPos();
                    if (this.getDefaultStack().getEnchantments().contains(EXPLOSION) && (boomlvl <= 8)) {

                        world.createExplosion(null, blockPos1.getX(), blockPos1.getY(), blockPos1.getZ(), boomlvl, Explosion.DestructionType.BREAK);
                        stack.damage(1, user, stackUser -> stackUser.sendToolBreakStatus(hand));


                    }
                    if (this.getDefaultStack().getEnchantments().contains(Enchantments.CHANNELING) && (boomlvl <= 8)) {
                        LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                        lightningEntity.updatePosition(blockPos1.getX(), blockPos1.getY(), blockPos1.getZ());
                        world.spawnEntity(lightningEntity);
                        stack.damage(1, user, stackUser -> stackUser.sendToolBreakStatus(hand));

                    }
                    break;
            }

        }

        return stack;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }


    @Override
    public boolean isEffectiveOn(BlockState state) {
        return true;
    }

}
