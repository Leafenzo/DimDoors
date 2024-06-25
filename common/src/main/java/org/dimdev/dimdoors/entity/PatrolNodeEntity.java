package org.dimdev.dimdoors.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PatrolNodeEntity extends Entity {
    public static final int MAX_PATH_DISTANCE = 16; // I'm not sure if this needs to exist, it just felt right so that it wouldn't get scuffed on low render distances... if that would even happen? ah well I'll test it.
    private static final EntityDataAccessor<Optional<BlockPos>> NEXT_NODE = SynchedEntityData.defineId(PatrolNodeEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    private static final EntityDataAccessor<Optional<BlockPos>> PREVIOUS_NODE = SynchedEntityData.defineId(PatrolNodeEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    public PatrolNodeEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        this.setNoGravity(true);
        this.setInvulnerable(true);
    }

    public BlockPos getNextNode() {
        return getEntityData().get(NEXT_NODE).orElse((BlockPos) null);
    }
    public boolean setNextNode(BlockPos blockPos) {
        if(getNextNode() != null && blockPos.closerThan(getNextNode(), MAX_PATH_DISTANCE)) {
            getEntityData().set(NEXT_NODE, Optional.ofNullable(blockPos)); return true;
        }
        else { return false; }
    }

    public BlockPos getPreviousNode() {
        return getEntityData().get(PREVIOUS_NODE).orElse((BlockPos) null);
    }
    public boolean setPreviousNode(BlockPos blockPos) {
        if(getPreviousNode() != null && blockPos.closerThan(getPreviousNode(), MAX_PATH_DISTANCE)) {
            getEntityData().set(PREVIOUS_NODE, Optional.ofNullable(blockPos)); return true;
        }
        else { return false; }
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }
    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    protected void defineSynchedData() {
        //short circuits
        this.entityData.define(NEXT_NODE, Optional.empty());
        this.entityData.define(PREVIOUS_NODE, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        if (getNextNode() != null) {
            nbt.put("NextNode", NbtUtils.writeBlockPos(getNextNode()));
        }
        if (getPreviousNode() != null) {
            nbt.put("Previous", NbtUtils.writeBlockPos(getPreviousNode()));
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("NextNode", 10)) {
            setNextNode(NbtUtils.readBlockPos(compound.getCompound("NextNode")));
        }
        if (compound.contains("PreviousNode", 10)) {
            setPreviousNode(NbtUtils.readBlockPos(compound.getCompound("PreviousNode")));
        }
    }

}
