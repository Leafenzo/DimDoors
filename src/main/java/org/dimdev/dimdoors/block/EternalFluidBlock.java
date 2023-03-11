package org.dimdev.dimdoors.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.dimdev.dimdoors.api.rift.target.EntityTarget;
import org.dimdev.dimdoors.api.util.math.MathUtil;
import org.dimdev.dimdoors.entity.limbo.LimboExitReason;
import org.dimdev.dimdoors.fluid.ModFluids;
import org.dimdev.dimdoors.rift.targets.EscapeTarget;

public class EternalFluidBlock extends LiquidBlock {
	private static final EntityTarget TARGET = new EscapeTarget(true);

	public EternalFluidBlock(Block.Settings settings) {
		super(ModFluids.ETERNAL_FLUID, settings);
	}

	@Override
	public void entityInside(BlockState blockState, Level world, BlockPos blockPos, Entity entity) {
		if (world.isClientSide) {
			return;
		}

		try {
			if (TARGET.receiveEntity(entity, Vec3.ZERO, MathUtil.entityEulerAngle(entity), entity.getDeltaMovement())) {
				if (entity instanceof Player) {
					LimboExitReason.ETERNAL_FLUID.broadcast((Player) entity);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
