package org.dimdev.dimdoors.mixin;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.dimdev.dimdoors.api.block.CustomBreakBlock;

@Mixin(Level.class)
public abstract class WorldMixin {

	/*
	I thought about redirecting the entire break method to be handled by the block itself,
	but I am not quite sure what that would mean for compatibility with other mixins,
	since then a large part of the method would need to be canceled. This is rather hacky, but it should fulfill the purpose best
	~CreepyCre
	 */
	@ModifyVariable(method = "breakBlock(Lnet/minecraft/util/math/BlockPos;ZLnet/minecraft/entity/Entity;I)Z",
			at = @At(value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/world/World;getFluidState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/fluid/FluidState;",
					ordinal = 0))
	private FluidState replaceFluidStateWithCustomHackyFluidState(FluidState original, BlockPos pos, boolean drop, @Nullable Entity breakingEntity, int maxUpdateDepth) { //TODO: Fix
		Level world = (Level) (Object) this;
		BlockState blockState = world.getBlockState(pos);
		Block block = blockState.getBlock();
		if (!(block instanceof CustomBreakBlock)) {
			return original;
		}
		InteractionResultHolder<Tuple<BlockState, Consumer<BlockEntity>>> result = ((CustomBreakBlock) block).customBreakBlock(world, pos, blockState, breakingEntity);
		if (!result.getResult().consumesAction()) {
			return original;
		}
		Tuple<BlockState, Consumer<BlockEntity>> pair = result.getObject();
		return new CustomBreakBlock.HackyFluidState(pair.getA(), pair.getB());
	}

	@Inject(method = "breakBlock(Lnet/minecraft/util/math/BlockPos;ZLnet/minecraft/entity/Entity;I)Z",
			locals = LocalCapture.CAPTURE_FAILHARD,
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z",
					ordinal = 0,
					shift = At.Shift.AFTER))
	private void applyBlockEntityModification(BlockPos pos, boolean drop, Entity breakingEntity, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir, BlockState blockState, FluidState fluidState) {
		if (!(fluidState instanceof CustomBreakBlock.HackyFluidState)) {
			return;
		}
		Consumer<BlockEntity> blockEntityConsumer = ((CustomBreakBlock.HackyFluidState) fluidState).getBlockEntityConsumer();
		if (blockEntityConsumer == null) {
			return;
		}
		BlockEntity blockEntity = ((Level) (Object) this).getBlockEntity(pos);
		if (blockEntity != null) {
			blockEntityConsumer.accept(blockEntity);
		}
	}

	/*
	This is where I'd inject if it turns out the method used above does actually have an issue
	 */
//	@Inject(method = "Lnet/minecraft/world/World;breakBlock(Lnet/minecraft/util/math/BlockPos;ZLnet/minecraft/entity/Entity;I)Z",
//			cancellable = true,
//			at = @At(
//					value = "INVOKE",
//					target = "Lnet/minecraft/world/WorldAccess;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V",
//					ordinal = 0,
//					shift = At.Shift.BY,
//					by = 2
//			)
//	)


}
