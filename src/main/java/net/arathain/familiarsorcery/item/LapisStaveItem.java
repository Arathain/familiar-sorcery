package net.arathain.familiarsorcery.item;

import net.arathain.familiarsorcery.UnfamiliarUtil;
import net.arathain.familiarsorcery.enchantment.FamiliarSorceryEnchants;
import net.arathain.familiarsorcery.entity.FamiliarEntities;
import net.arathain.familiarsorcery.entity.IcicleProjectile;
import net.arathain.familiarsorcery.entity.MagikBeamEntity;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.Objects;


public class LapisStaveItem extends SwordItem implements AbstractStaveItem {
    public static final DyeColor BEAM_COLOR = DyeColor.BLUE;
    private MagikBeamEntity beamEntity;

    public LapisStaveItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    public static void setUsing(ItemStack stack, boolean using) {
        assert stack.getTag() != null;
        stack.getTag().putBoolean("using", using);
    }
    public static boolean getUsing(ItemStack stack) {
        assert stack.getTag() != null;
        return stack.getTag().getBoolean("using");
    }
    public static void setTargetPos(ItemStack stack, float x, float y, float z) {
        assert stack.getTag() != null;
        CompoundTag targetPos = new CompoundTag();
        targetPos.putFloat("X", x);
        targetPos.putFloat("Y", y);
        targetPos.putFloat("Z", z);
        stack.getTag().put("targetPos", targetPos);
    }


    //TODO this absolutely doesn't work
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (getUsing(user.getStackInHand(hand)) && (EnchantmentHelper.getLevel(FamiliarSorceryEnchants.EXPLOSION, user.getStackInHand(hand)) > 0)) {
            if (beamEntity != null) {
                beamEntity.remove();
            }
            beamEntity = new MagikBeamEntity(FamiliarEntities.BEAM, world);
            HitResult hitResult = UnfamiliarUtil.hitscanBlock(world, user, 60, RaycastContext.FluidHandling.NONE, (target) -> !target.is(Blocks.AIR));
            EntityHitResult hit = UnfamiliarUtil.hitscanEntity(world, user, 60, (target) -> target instanceof LivingEntity && !target.isSpectator() && user.canSee(target));
            beamEntity.setColor(BEAM_COLOR);
            beamEntity.setOwner(user);
            if (hit !=null) {

                setTargetPos(user.getStackInHand(hand), (float)hit.getPos().x, (float)hit.getPos().y, (float)hit.getPos().z);
                beamEntity.setPos(hit.getPos().x, hit.getPos().y, hit.getPos().z);
                world.spawnEntity(beamEntity);
                setUsing(user.getStackInHand(hand), false);
            }
            if (hit == null) {
                setTargetPos(user.getStackInHand(hand), (float)hitResult.getPos().x, (float)hitResult.getPos().y, (float)hitResult.getPos().z);
                beamEntity.setPos(hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z);
                world.spawnEntity(beamEntity);
                setUsing(user.getStackInHand(hand), false);
            }
        }
        return ItemUsage.consumeHeldItem(world, user, hand);
    }





    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof PlayerEntity) {
            int falvl = EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, user.getStackInHand(user.getActiveHand()));
            int boomlvl = EnchantmentHelper.getLevel(FamiliarSorceryEnchants.EXPLOSION, user.getStackInHand(user.getActiveHand()));
            int chalvl = EnchantmentHelper.getLevel(Enchantments.CHANNELING, user.getStackInHand(user.getActiveHand()));
            int iclvl = EnchantmentHelper.getLevel(FamiliarSorceryEnchants.ICING, user.getStackInHand(user.getActiveHand()));
            boolean fa = falvl > 0;
            assert stack.getTag() != null;
            CompoundTag targetPos = (CompoundTag) stack.getTag().get("targetPos");
            assert targetPos != null;
            float targetX = targetPos.getFloat("X");
            float targetY = targetPos.getFloat("Y");
            float targetZ = targetPos.getFloat("Z");
            if (fa && boomlvl == 0) {
                FireballEntity fireball = new FireballEntity(world, user, user.getRotationVector().x, user.getRotationVector().y, user.getRotationVector().z);
                fireball.setOwner(user);
                fireball.setPos(fireball.getX(), fireball.getY() + 1, fireball.getZ());

                world.playSound(null, user.getBlockPos(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.HOSTILE, 1, 1);
                world.spawnEntity(fireball);
                stack.damage(1, user, stackUser -> stackUser.sendToolBreakStatus(user.getActiveHand()));
            }
            if(iclvl>0 && boomlvl == 0) {
                //TODO replicate multishot functionality
                IcicleProjectile icicleProjectile = new IcicleProjectile(world, user);
                icicleProjectile.setOwner(user);
                icicleProjectile.setProperties(user, user.pitch, user.yaw, 0.0F, 4.0F, 1.0F);
                icicleProjectile.updatePosition(user.getX(), user.getEyeY(), user.getZ());
                icicleProjectile.addVelocity(user.getRandom().nextDouble() / 10, 0, user.getRandom().nextGaussian() / 10);
                world.spawnEntity(icicleProjectile);
                world.spawnEntity(icicleProjectile);
                world.spawnEntity(icicleProjectile);
            }
            if (boomlvl > 0) {
                    world.createExplosion(user, DamageSource.explosion(user), null, targetX, targetY, targetZ, (boomlvl * 2), fa, Explosion.DestructionType.DESTROY);

            }
            if (chalvl > 0) {
                    LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                    lightningEntity.updatePosition(targetX, targetY, targetZ);
                    world.spawnEntity(lightningEntity);

            }
            if(iclvl > 0 && boomlvl > 0) {

                for (int i = 0; i < (iclvl * 50); i++) {
                        IcicleProjectile icicleProjectile = new IcicleProjectile(world, user);
                        icicleProjectile.setOwner(user);
                        icicleProjectile.setPos(targetX, targetY, targetZ);
                        icicleProjectile.updateTrackedPosition(targetX, targetY+0.5f, targetZ);
                        icicleProjectile.setVelocity(user.getRandom().nextGaussian(), user.getRandom().nextGaussian(), user.getRandom().nextGaussian());
                        world.spawnEntity(icicleProjectile);
                }

            }


        }
        if (beamEntity != null) {
            beamEntity.remove();
        }
        setUsing(user.getStackInHand(user.getActiveHand()), true);
        return stack;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 128;
    }
    
    @Override
    public boolean isEffectiveOn(BlockState state) {
        return true;
    }

}