package net.arathain.familiarsorcery.entity;


import net.arathain.familiarsorcery.pain.UnfamiliarPackets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MagikBeamEntity extends Entity {
    public static DyeColor BEAM_COLOR = DyeColor.RED;
    private UUID ownerUuid;
    private int ownerEntityId;

    public MagikBeamEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker() {

    }

    public void setOwner(@Nullable Entity entity) {
        if (entity != null) {
            this.ownerUuid = entity.getUuid();
            this.ownerEntityId = entity.getEntityId();
        }

    }

    @Nullable
    public Entity getOwner() {
        if (this.ownerUuid != null && this.world instanceof ServerWorld) {
            return ((ServerWorld)this.world).getEntity(this.ownerUuid);
        } else {
            return this.ownerEntityId != 0 ? this.world.getEntityById(this.ownerEntityId) : null;
        }
    }

    protected void writeCustomDataToTag(CompoundTag tag) {
        if (this.ownerUuid != null) {
            tag.putUuid("Owner", this.ownerUuid);
        }

    }

    protected void readCustomDataFromTag(CompoundTag tag) {
        if (tag.containsUuid("Owner")) {
            this.ownerUuid = tag.getUuid("Owner");
        }

    }

    @Override
    public Packet<?> createSpawnPacket() {
        return UnfamiliarPackets.newSpawnPacket(this);
    }
    public World getWorld() {
        return this.world;
    }
    public static DyeColor getColor() {
        return BEAM_COLOR;
    }

    public void setColor(DyeColor dyeColor) {
        BEAM_COLOR = dyeColor;
    }
}
