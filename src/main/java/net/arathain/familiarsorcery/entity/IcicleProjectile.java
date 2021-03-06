package net.arathain.familiarsorcery.entity;

import net.arathain.familiarsorcery.pain.UnfamiliarPackets;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class IcicleProjectile extends PersistentProjectileEntity {

    public IcicleProjectile(EntityType<? extends IcicleProjectile> entityType, World world) {
        super(entityType, world);
        this.setSound(SoundEvents.BLOCK_GLASS_BREAK);
    }
    public IcicleProjectile(World world, double x, double y, double z) {
        this(FamiliarEntities.ICICLE, world);
        this.setPos(x, y, z);
    }

    public IcicleProjectile(World world, LivingEntity owner) {
        super(FamiliarEntities.ICICLE, world);
        this.setOwner(owner);
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_GLASS_BREAK;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age <= 1) {
            this.world.addParticle(ParticleTypes.POOF, this.getX() + random.nextGaussian() / 20f, this.getY() + random.nextGaussian() / 20f, this.getZ() + random.nextGaussian() / 20f, random.nextGaussian() / 20f, 0.2D + random.nextGaussian() / 20f, random.nextGaussian() / 20f);
        }

        if (this.inGround) {
            for (int i = 0; i < 8; ++i) {
                this.world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Items.PACKED_ICE, 1)), this.getX() + random.nextGaussian() / 20f, this.getY() + random.nextGaussian() / 20f, this.getZ() + random.nextGaussian() / 20f, random.nextGaussian() / 20f, 0.2D + random.nextGaussian() / 20f, random.nextGaussian() / 20f);
            }
            this.remove();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        this.world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, SoundCategory.NEUTRAL, 1.0f, 1.5f);
    }

    @Override
    public byte getPierceLevel() {
        return 2;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return UnfamiliarPackets.newSpawnPacket(this);
    }
}
