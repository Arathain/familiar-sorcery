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

import static net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer.BEAM_TEXTURE;

public class DiamondStaveItem extends SwordItem implements AbstractStaveItem {
    public static final DyeColor BEAM_COLOR = DyeColor.BLUE;
    private boolean bees = true;
    private Vec3d targetPos;
    private MagikBeamEntity beamEntity;

    public DiamondStaveItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (bees && (EnchantmentHelper.getLevel(FamiliarSorceryEnchants.EXPLOSION, user.getStackInHand(hand)) > 0)) {
            beamEntity = new MagikBeamEntity(FamiliarEntities.BEAM, world);
            HitResult hitResult = UnfamiliarUtil.hitscanBlock(world, user, 50, RaycastContext.FluidHandling.NONE, (target) -> !target.is(Blocks.AIR));
            EntityHitResult hit = UnfamiliarUtil.hitscanEntity(world, user, 50, (target) -> target instanceof LivingEntity && !target.isSpectator() && user.canSee(target));
            beamEntity.setColor(BEAM_COLOR);
            beamEntity.setOwner(user);
            if (hit !=null) {
                targetPos = hit.getPos();
                beamEntity.setPos(targetPos.x, targetPos.y, targetPos.z);
                world.spawnEntity(beamEntity);
                bees = false;
            }
            if (hit ==null) {
                targetPos = hitResult.getPos();
                beamEntity.setPos(targetPos.x, targetPos.y, targetPos.z);
                world.spawnEntity(beamEntity);
                bees = false;
            }
        }
        return ItemUsage.consumeHeldItem(world, user, hand);
    }




    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof PlayerEntity && targetPos != null) {
            int falvl = EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, user.getStackInHand(user.getActiveHand()));
            int boomlvl = EnchantmentHelper.getLevel(FamiliarSorceryEnchants.EXPLOSION, user.getStackInHand(user.getActiveHand()));
            int chalvl = EnchantmentHelper.getLevel(Enchantments.CHANNELING, user.getStackInHand(user.getActiveHand()));
            int iclvl = EnchantmentHelper.getLevel(FamiliarSorceryEnchants.ICING, user.getStackInHand(user.getActiveHand()));
            boolean fa = falvl > 0;
            if (fa && boomlvl == 0) {
                FireballEntity fireball = new FireballEntity(world, user, user.getRotationVector().x, user.getRotationVector().y, user.getRotationVector().z);
                fireball.setOwner(user);
                fireball.setPos(fireball.getX(), fireball.getY() + 1, fireball.getZ());

                world.playSound(null, user.getBlockPos(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.HOSTILE, 1, 1);
                world.spawnEntity(fireball);
                stack.damage(1, user, stackUser -> stackUser.sendToolBreakStatus(user.getActiveHand()));
            }
            if(iclvl>0 && boomlvl == 0) {
                IcicleProjectile icicleProjectile = new IcicleProjectile(world, user);
                icicleProjectile.setOwner(user);
                icicleProjectile.setProperties(user, user.pitch, user.yaw, 0.0F, 4.0F, 1.0F);
                icicleProjectile.updatePosition(user.getX(), user.getEyeY(), user.getZ());
                icicleProjectile.addVelocity(user.getRandom().nextDouble() / 10, 0, user.getRandom().nextGaussian() / 10);
                world.spawnEntity(icicleProjectile);
            }
            if (boomlvl > 0 && targetPos != null) {
                    world.createExplosion(user, DamageSource.explosion(user), null, targetPos.x, targetPos.y, targetPos.z, (boomlvl * 2), fa, Explosion.DestructionType.DESTROY);

            }
            if (chalvl > 0  && targetPos != null) {
                    LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                    lightningEntity.updatePosition(targetPos.x, targetPos.y, targetPos.z);
                    world.spawnEntity(lightningEntity);

            }
            if(iclvl>0 && boomlvl > 0  && targetPos != null) {

                for (int i = 0; i < (iclvl * 80); i++) {
                        IcicleProjectile icicleProjectile = new IcicleProjectile(world, user);
                        icicleProjectile.setOwner(user);
                        icicleProjectile.setPos(targetPos.x, targetPos.y, targetPos.z);
                        icicleProjectile.updateTrackedPosition(targetPos.x, targetPos.y+0.5f, targetPos.z);
                        icicleProjectile.setVelocity(user.getRandom().nextGaussian(), user.getRandom().nextGaussian(), user.getRandom().nextGaussian());
                        world.spawnEntity(icicleProjectile);
                }

            }


        }
        if (beamEntity != null) {
            beamEntity.remove();
        }
        targetPos = null;
        bees = true;
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
