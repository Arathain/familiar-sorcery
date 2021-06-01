package net.arathain.familiarsorcery.item;

import net.arathain.familiarsorcery.UnfamiliarUtil;
import net.arathain.familiarsorcery.enchantment.FamiliarSorceryEnchants;
import net.arathain.familiarsorcery.entity.FamiliarEntities;
import net.arathain.familiarsorcery.entity.IcicleProjectile;
import net.arathain.familiarsorcery.entity.MagikBeamEntity;
import net.arathain.familiarsorcery.entity.PartOfYou;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.Objects;
import java.util.function.Predicate;


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



    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (getUsing(stack) && (EnchantmentHelper.getLevel(FamiliarSorceryEnchants.EXPLOSION, stack) > 0) && (user.getOffHandStack().getItem() == Items.LAPIS_LAZULI || user.getMainHandStack().getItem() == Items.LAPIS_LAZULI)) {
            if (beamEntity != null) {
                beamEntity.remove();
            }
            beamEntity = new MagikBeamEntity(FamiliarEntities.BEAM, world);
            HitResult hitResult = UnfamiliarUtil.hitscanBlock(world, user, 60, RaycastContext.FluidHandling.NONE, (target) -> !target.is(Blocks.AIR));
            EntityHitResult hit = UnfamiliarUtil.hitscanEntity(world, user, 60, (target) -> target instanceof LivingEntity && !target.isSpectator() && user.canSee(target));
            beamEntity.setColor(BEAM_COLOR);
            beamEntity.setOwner(user);
            if (hit !=null) {

                setTargetPos(stack, (float)hit.getPos().x, (float)hit.getPos().y, (float)hit.getPos().z);
                beamEntity.setPos(hit.getPos().x, hit.getPos().y, hit.getPos().z);
                world.spawnEntity(beamEntity);
                setUsing(stack, false);
            }
            if (hit == null) {
                setTargetPos(stack, (float)hitResult.getPos().x, (float)hitResult.getPos().y, (float)hitResult.getPos().z);
                beamEntity.setPos(hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z);
                world.spawnEntity(beamEntity);
                setUsing(stack, false);
            }
        }
        return ItemUsage.consumeHeldItem(world, user, hand);
    }


    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof PlayerEntity && (user.getOffHandStack().getItem() == Items.LAPIS_LAZULI || user.getMainHandStack().getItem() == Items.LAPIS_LAZULI)) {
            int falvl = EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, user.getStackInHand(user.getActiveHand()));
            int boomlvl = EnchantmentHelper.getLevel(FamiliarSorceryEnchants.EXPLOSION, user.getStackInHand(user.getActiveHand()));
            int chalvl = EnchantmentHelper.getLevel(Enchantments.CHANNELING, user.getStackInHand(user.getActiveHand()));
            int iclvl = EnchantmentHelper.getLevel(FamiliarSorceryEnchants.ICING, user.getStackInHand(user.getActiveHand()));
            int fraclvl = EnchantmentHelper.getLevel(FamiliarSorceryEnchants.FRACTURE, user.getStackInHand(user.getActiveHand()));
            int metlvl = EnchantmentHelper.getLevel(FamiliarSorceryEnchants.METALLICISE, user.getStackInHand(user.getActiveHand()));
            boolean fa = falvl > 0;
            assert stack.getTag() != null;
            CompoundTag targetPos = (CompoundTag) stack.getTag().get("targetPos");
            assert targetPos != null;


            if (fa && boomlvl == 0) {
                FireballEntity fireball = new FireballEntity(world, user, user.getRotationVector().x, user.getRotationVector().y, user.getRotationVector().z);
                fireball.setOwner(user);
                fireball.setPos(fireball.getX(), fireball.getY() + 1, fireball.getZ());

                world.playSound(null, user.getBlockPos(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.HOSTILE, 1, 1);
                world.spawnEntity(fireball);

            }
            if(iclvl>0 && !(boomlvl > 0)) {
                //TODO replicate multishot functionality
                for (int i = 0; i < (iclvl * 8); i++) {
                    IcicleProjectile icicleProjectile = new IcicleProjectile(world, user);
                    icicleProjectile.setOwner(user);
                    icicleProjectile.setProperties(user, user.pitch, user.yaw, 0.0F, 4.0F, 1.0F);
                    icicleProjectile.updatePosition(user.getX(), user.getEyeY(), user.getZ());
                    icicleProjectile.addVelocity(user.getRandom().nextDouble() / 10, 0, user.getRandom().nextGaussian() / 10);
                    world.spawnEntity(icicleProjectile);
                }
            }
            if (boomlvl > 0) {

                float targetX = targetPos.getFloat("X");
                float targetY = targetPos.getFloat("Y");
                float targetZ = targetPos.getFloat("Z");
                    world.createExplosion(user, DamageSource.explosion(user), null, targetX, targetY, targetZ, (boomlvl * 2), fa, Explosion.DestructionType.DESTROY);

            }
            if (chalvl > 0) {

                float targetX = targetPos.getFloat("X");
                float targetY = targetPos.getFloat("Y");
                float targetZ = targetPos.getFloat("Z");
                    LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                    lightningEntity.updatePosition(targetX, targetY, targetZ);
                    world.spawnEntity(lightningEntity);

            }
            if(iclvl > 0 && boomlvl > 0) {

                float targetX = targetPos.getFloat("X");
                float targetY = targetPos.getFloat("Y");
                float targetZ = targetPos.getFloat("Z");
                for (int i = 0; i < (iclvl * 50); i++) {
                        IcicleProjectile icicleProjectile = new IcicleProjectile(world, user);
                        icicleProjectile.setOwner(user);
                        icicleProjectile.setPos(targetX, targetY, targetZ);
                        icicleProjectile.updateTrackedPosition(targetX, targetY+0.5f, targetZ);
                        icicleProjectile.setVelocity(user.getRandom().nextGaussian(), user.getRandom().nextGaussian(), user.getRandom().nextGaussian());
                        world.spawnEntity(icicleProjectile);
                }

            }
            if(fraclvl > 0) {

                
                PartOfYou partOfYou = new PartOfYou(FamiliarEntities.PART_OF_YOU, world);
                partOfYou.initialize((ServerWorldAccess) world, world.getLocalDifficulty(user.getBlockPos()), SpawnReason.EVENT, null, null);
                partOfYou.updatePositionAndAngles(user.getX(), user.getY()+1, user.getZ(), world.random.nextFloat() * 360, 0);
                partOfYou.createSpawnPacket();
                partOfYou.setOwner((PlayerEntity) user);
                user.damage(DamageSource.MAGIC, user.getHealth() - 2*fraclvl);
                world.spawnEntity(partOfYou);

            }
            if(metlvl > 0) {
             user.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 96*metlvl, 1));
            }
            stack.damage(1, user, stackUser -> stackUser.sendToolBreakStatus(user.getActiveHand()));

            if (user.getOffHandStack().getItem() == Items.LAPIS_LAZULI) {
                user.getOffHandStack().decrement(1);
            }
            if (user.getMainHandStack().getItem() == Items.LAPIS_LAZULI) {
                user.getMainHandStack().decrement(1);
            }
        }
        if (beamEntity != null) {
            beamEntity.remove();
        }
        setUsing(user.getStackInHand(user.getActiveHand()), true);
        return stack;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
        if (beamEntity != null) {
            beamEntity.remove();
        }
        setUsing(user.getStackInHand(user.getActiveHand()), true);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 96;
    }
    
    @Override
    public boolean isEffectiveOn(BlockState state) {
        return true;
    }

}
