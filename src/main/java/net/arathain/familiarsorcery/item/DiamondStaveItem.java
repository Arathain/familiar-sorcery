package net.arathain.familiarsorcery.item;

import net.arathain.familiarsorcery.UnfamiliarUtil;
import net.arathain.familiarsorcery.enchantment.FamiliarSorceryEnchants;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import static net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer.BEAM_TEXTURE;

public class DiamondStaveItem extends SwordItem implements AbstractStaveItem {
    public static final DyeColor BEAM_COLOR = DyeColor.RED;

    public DiamondStaveItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof PlayerEntity) {
            int falvl = EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, user.getStackInHand(user.getActiveHand()));
            int boomlvl = EnchantmentHelper.getLevel(FamiliarSorceryEnchants.EXPLOSION, user.getStackInHand(user.getActiveHand()));
            int chalvl = EnchantmentHelper.getLevel(Enchantments.CHANNELING, user.getStackInHand(user.getActiveHand()));
            boolean fa = falvl > 0;
            HitResult hitResult = UnfamiliarUtil.hitscanBlock(world, user, 50, RaycastContext.FluidHandling.NONE, (target) -> !target.is(Blocks.AIR));
            EntityHitResult hit = UnfamiliarUtil.hitscanEntity(world, user, 50, (target) -> target instanceof LivingEntity && !target.isSpectator() && user.canSee(target));
            if (fa && boomlvl == 0) {
                FireballEntity fireball = new FireballEntity(world, user, user.getRotationVector().x, user.getRotationVector().y, user.getRotationVector().z);
                fireball.setOwner(user);
                fireball.setPos(fireball.getX(), fireball.getY() + 1, fireball.getZ());

                world.playSound(null, user.getBlockPos(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.HOSTILE, 1, 1);
                world.spawnEntity(fireball);
                stack.damage(1, user, stackUser -> stackUser.sendToolBreakStatus(user.getActiveHand()));
            }
            if (boomlvl > 0) {
                if (hit != null) {
                    world.createExplosion(user, DamageSource.explosion(user), null, hit.getPos().x, hit.getPos().y, hit.getPos().z, (boomlvl * 2), fa, Explosion.DestructionType.DESTROY);

                }
                if (hit == null) {
                    world.createExplosion(user, DamageSource.explosion(user), null, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z, (boomlvl * 2), fa, Explosion.DestructionType.DESTROY);
                }
            }
            if (chalvl > 0) {
                if (hit != null) {
                    LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                    lightningEntity.updatePosition(hit.getPos().x, hit.getPos().y, hit.getPos().z);
                    world.spawnEntity(lightningEntity);
                }
                if (hit == null) {
                    LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                    lightningEntity.updatePosition(hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z);
                    world.spawnEntity(lightningEntity);
                }
            }

        }
        return stack;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 48;
    }
    
    @Override
    public boolean isEffectiveOn(BlockState state) {
        return true;
    }

}
