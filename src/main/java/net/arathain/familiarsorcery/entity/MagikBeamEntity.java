package net.arathain.familiarsorcery.entity;


import net.arathain.familiarsorcery.pain.UnfamiliarPackets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.world.World;

public class MagikBeamEntity extends Entity {

    public MagikBeamEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromTag(CompoundTag tag) {

    }

    @Override
    protected void writeCustomDataToTag(CompoundTag tag) {

    }

    @Override
    public Packet<?> createSpawnPacket() {
        return UnfamiliarPackets.newSpawnPacket(this);
    }
    public World getWorld() {
        return this.world;
    }
}
