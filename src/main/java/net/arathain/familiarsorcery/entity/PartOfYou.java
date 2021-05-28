package net.arathain.familiarsorcery.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class PartOfYou extends HostileEntity implements Minion {
    private static final TrackedData<Byte> TAMEABLE = DataTracker.registerData(TameableEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(TameableEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    protected static final TrackedData<Byte> FLAGS;
    private BlockPos bounds;

    public PartOfYou(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(4, new PartOfYou.ChargeTargetGoal());
        this.goalSelector.add(8, new PartOfYou.LookAtTargetGoal());
        this.targetSelector.add(1, new PartOfYou.PartOfYouTrackAttackerGoal(this));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.add(2, new PartOfYou.TrackOwnerTargetGoal(this));;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 14.0D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0D);
    }

    public void move(MovementType type, Vec3d movement) {
        super.move(type, movement);
        this.checkBlockCollision();
    }

    public void tick() {
        this.noClip = true;
        super.tick();
        this.noClip = false;
        this.setNoGravity(true);

    }

    public void writeCustomDataToTag(CompoundTag tag) {
        if (this.getOwnerUuid() != null) {
            tag.putUuid("Binder", this.getOwnerUuid());
        }
        if (this.bounds != null) {
            tag.putInt("BoundX", this.bounds.getX());
            tag.putInt("BoundY", this.bounds.getY());
            tag.putInt("BoundZ", this.bounds.getZ());
        }

    }

    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        UUID ownerUUID;
        if (tag.containsUuid("Binder")) {
            ownerUUID = tag.getUuid("Binder");
        } else {
            String string = tag.getString("Binder");
            ownerUUID = ServerConfigHandler.getPlayerUuidByName(this.getServer(), string);
        }

        if (ownerUUID != null) {
            try {
                this.setOwnerUuid(ownerUUID);
                this.setTamed(true);
            } catch (Throwable var4) {
                this.setTamed(false);
            }
        }
        if (tag.contains("BoundX")) {
            this.bounds = new BlockPos(tag.getInt("BoundX"), tag.getInt("BoundY"), tag.getInt("BoundZ"));
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(TAMEABLE, (byte) 0);
        dataTracker.startTracking(OWNER_UUID, Optional.empty());
        this.dataTracker.startTracking(FLAGS, (byte) 0);
    }


    @Override
    public UUID getOwnerUuid() {
        return (UUID) ((Optional) this.dataTracker.get(OWNER_UUID)).orElse(null);
    }


    @Override
    public void setOwnerUuid(@Nullable UUID uuid) {
        this.dataTracker.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Override
    public void setOwner(PlayerEntity player) {
        this.setTamed(true);
        this.setOwnerUuid(player.getUuid());
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        try {
            UUID uUID = this.getOwnerUuid();
            return uUID == null ? null : this.world.getPlayerByUuid(uUID);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    @Override
    public boolean isOwner(LivingEntity entity) {
        return entity == this.getOwner();
    }

    @Override
    public boolean isTamed() {
        return (this.dataTracker.get(TAMEABLE) & 4) != 0;
    }

    @Override
    public void setTamed(boolean tamed) {
        byte b = this.dataTracker.get(TAMEABLE);
        if (tamed) {
            this.dataTracker.set(TAMEABLE, (byte) (b | 4));
        } else {
            this.dataTracker.set(TAMEABLE, (byte) (b & -5));
        }

        this.onTamedChanged();
    }

    public void reset() {
        this.setTarget(null);
        this.navigation.stop();
    }

    @Nullable
    public BlockPos getBounds() {
        return this.bounds;
    }

    public void setBounds(@Nullable BlockPos pos) {
        this.bounds = pos;
    }

    private boolean areFlagsSet(int mask) {
        int i = (Byte) this.dataTracker.get(FLAGS);
        return (i & mask) != 0;
    }

    private void setVexFlag(int mask, boolean value) {
        int i = (Byte) this.dataTracker.get(FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.dataTracker.set(FLAGS, (byte) (i & 255));
    }

    public boolean isCharging() {
        return this.areFlagsSet(1);
    }

    public void setCharging(boolean charging) {
        this.setVexFlag(1, charging);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_VEX_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VEX_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_VEX_HURT;
    }

    public float getBrightnessAtEyes() {
        return 1.0F;
    }

    static {
        FLAGS = DataTracker.registerData(VexEntity.class, TrackedDataHandlerRegistry.BYTE);
    }

    class TrackOwnerTargetGoal extends TrackTargetGoal {
        private final TargetPredicate TRACK_OWNER_PREDICATE = (new TargetPredicate()).includeHidden().ignoreDistanceScalingFactor();

        public TrackOwnerTargetGoal(PathAwareEntity mob) {
            super(mob, false);
        }

        public boolean canStart() {
            return PartOfYou.this.getOwner() != null && PartOfYou.this.getOwner().getAttacking() != null && this.canTrack(PartOfYou.this.getOwner().getAttacking(), this.TRACK_OWNER_PREDICATE);
        }

        public void start() {
            PartOfYou.this.setTarget(Objects.requireNonNull(PartOfYou.this.getOwner()).getAttacking());
            super.start();
        }
    }

    class LookAtTargetGoal extends Goal {
        public LookAtTargetGoal() {
            this.setControls(EnumSet.of(Control.MOVE));
        }

        public boolean canStart() {
            return !PartOfYou.this.getMoveControl().isMoving() && PartOfYou.this.random.nextInt(7) == 0;
        }

        public boolean shouldContinue() {
            return false;
        }

        public void tick() {
            BlockPos blockPos = PartOfYou.this.getBounds();
            if (blockPos == null) {
                blockPos = PartOfYou.this.getBlockPos();
            }

            for (int i = 0; i < 3; ++i) {
                BlockPos blockPos2 = blockPos.add(PartOfYou.this.random.nextInt(15) - 7, PartOfYou.this.random.nextInt(11) - 5, PartOfYou.this.random.nextInt(15) - 7);
                if (PartOfYou.this.world.isAir(blockPos2)) {
                    PartOfYou.this.moveControl.moveTo((double) blockPos2.getX() + 0.5D, (double) blockPos2.getY() + 0.5D, (double) blockPos2.getZ() + 0.5D, 0.25D);
                    if (PartOfYou.this.getTarget() == null) {
                        PartOfYou.this.getLookControl().lookAt((double) blockPos2.getX() + 0.5D, (double) blockPos2.getY() + 0.5D, (double) blockPos2.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }

    class ChargeTargetGoal extends Goal {
        public ChargeTargetGoal() {
            this.setControls(EnumSet.of(Control.MOVE));
        }

        public boolean canStart() {
            if (PartOfYou.this.getTarget() != null && !PartOfYou.this.getMoveControl().isMoving() && PartOfYou.this.random.nextInt(7) == 0) {
                return PartOfYou.this.squaredDistanceTo(PartOfYou.this.getTarget()) > 4.0D;
            } else {
                return false;
            }
        }

        public boolean shouldContinue() {
            return PartOfYou.this.getMoveControl().isMoving() && PartOfYou.this.isCharging() && PartOfYou.this.getTarget() != null && PartOfYou.this.getTarget().isAlive();
        }

        public void start() {
            LivingEntity livingEntity = PartOfYou.this.getTarget();
            Vec3d vec3d = livingEntity.getCameraPosVec(1.0F);
            PartOfYou.this.moveControl.moveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
            PartOfYou.this.setCharging(true);
            PartOfYou.this.playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0F, 1.0F);
        }

        public void stop() {
            PartOfYou.this.setCharging(false);
        }

        public void tick() {
            LivingEntity livingEntity = PartOfYou.this.getTarget();
            if (PartOfYou.this.getBoundingBox().intersects(livingEntity.getBoundingBox())) {
                PartOfYou.this.tryAttack(livingEntity);
                PartOfYou.this.setCharging(false);
            } else {
                double d = PartOfYou.this.squaredDistanceTo(livingEntity);
                if (d < 9.0D) {
                    Vec3d vec3d = livingEntity.getCameraPosVec(1.0F);
                    PartOfYou.this.moveControl.moveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
                }
            }

        }
    }

    class PartOfYouMoveControl extends MoveControl {
        public PartOfYouMoveControl(PartOfYou owner) {
            super(owner);
        }

        public void tick() {
            if (this.state == State.MOVE_TO) {
                Vec3d vec3d = new Vec3d(this.targetX - PartOfYou.this.getX(), this.targetY - PartOfYou.this.getY(), this.targetZ - PartOfYou.this.getZ());
                double d = vec3d.length();
                if (d < PartOfYou.this.getBoundingBox().getAverageSideLength()) {
                    this.state = State.WAIT;
                    PartOfYou.this.setVelocity(PartOfYou.this.getVelocity().multiply(0.5D));
                } else {
                    PartOfYou.this.setVelocity(PartOfYou.this.getVelocity().add(vec3d.multiply(this.speed * 0.05D / d)));
                    if (PartOfYou.this.getTarget() == null) {
                        Vec3d vec3d2 = PartOfYou.this.getVelocity();
                        PartOfYou.this.yaw = -((float) MathHelper.atan2(vec3d2.x, vec3d2.z)) * 57.295776F;
                        PartOfYou.this.bodyYaw = PartOfYou.this.yaw;
                    } else {
                        double e = PartOfYou.this.getTarget().getX() - PartOfYou.this.getX();
                        double f = PartOfYou.this.getTarget().getZ() - PartOfYou.this.getZ();
                        PartOfYou.this.yaw = -((float) MathHelper.atan2(e, f)) * 57.295776F;
                        PartOfYou.this.bodyYaw = PartOfYou.this.yaw;
                    }
                }

            }
        }
    }

    class PartOfYouTrackAttackerGoal extends TrackTargetGoal {
        private LivingEntity attacker;
        private int lastAttackedTime;

        public PartOfYouTrackAttackerGoal(Minion tameable) {
            super((MobEntity) tameable, false);
            this.setControls(EnumSet.of(Control.TARGET));
        }

        public boolean canStart() {
            if (PartOfYou.this.isTamed()) {
                LivingEntity livingEntity = PartOfYou.this.getOwner();
                if (livingEntity == null) {
                    return false;
                } else {
                    this.attacker = livingEntity.getAttacker();
                    if (this.attacker != null) {
                        int i = livingEntity.getLastAttackedTime();
                        return i != this.lastAttackedTime && this.canTrack(this.attacker, TargetPredicate.DEFAULT) && PartOfYou.this.canAttackWithOwner(this.attacker, livingEntity);
                    }
                }
            }
            return false;
        }

        public void start() {
            this.mob.setTarget(this.attacker);
            LivingEntity livingEntity = PartOfYou.this.getOwner();
            if (livingEntity != null) {
                this.lastAttackedTime = livingEntity.getLastAttackedTime();
            }

            super.start();
        }
    }
}
