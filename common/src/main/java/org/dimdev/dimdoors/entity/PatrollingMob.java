package org.dimdev.dimdoors.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class PatrollingMob extends PathfinderMob {
    protected PatrollingMob(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    // This is for all mobs that follow Patrol Nodes.

    // if next is null, start going in reverse (only going to previous paths) until previous is null, and reverse again
    // if next and previous is null, enter wander mode I guess?


}
