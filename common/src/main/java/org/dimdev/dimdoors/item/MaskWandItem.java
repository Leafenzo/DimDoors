package org.dimdev.dimdoors.item;

import net.fabricmc.api.Environment;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.fabricmc.api.EnvType.CLIENT;

	public class MaskWandItem extends Item {
	private static final Logger LOGGER = LogManager.getLogger();

	public static final String ID = "rift_configuration_tool";

	public MaskWandItem(Properties settings) {
		super(settings);
	}

	@Override
	public boolean canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) { return false;}

//	@Override
//	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
//		ItemStack stack = player.getItemInHand(hand);
//		HitResult hit = player.pick(RaycastHelper.REACH_DISTANCE, 0, false);
//
//		if (world.isClientSide()) {
//			return InteractionResultHolder.fail(stack);
//		} else {
//			if(hit.getType().equals(HitResult.Type.BLOCK)) {
////				MaskEntity mask = ModEntityTypes.MASK.create((ServerWorld) world, null, LiteralText.EMPTY, player, ((BlockHitResult) hit).getBlockPos(), SpawnReason.SPAWNER, true, false);
////				world.spawnEntity(mask);
//			}
//		}
//
//		return InteractionResultHolder.success(stack);
//	}

//	@Override
//    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
//        ItemStack itemStack = user.getItemInHand(hand);
//        double x = user.getX() + user.getRotationVector().x * 4.5;
//        double y = user.getEyeY() + user.getRotationVector().y * 4.5;
//        double z = user.getZ() + user.getYHeadRot() * 4.5;
//        BlockPos pos = new BlockPos((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
//
//        if(!world.isOutsideBuildHeight(pos) && world.getWorldBorder().isWithinBounds(pos)) {
////            ItemPlacementContext context = new ItemPlacementContext(world, user, hand, itemStack, new BlockHitResult(pos.toCenterPos(), Direction.NORTH, pos, false));
//			UseOnContext context = new UseOnContext()
//			InteractionResult interactionResult = useOn(context);
//            if(interactionResult.consumesAction()) {
//                return InteractionResultHolder.success(itemStack);
//            }
//        }
//        return super.use(world, user, hand);
//    }
//
//	public InteractionResult useOn(UseOnContext context) {
//		InteractionResult interactionResult = this.place(new BlockPlaceContext(context));
//		if (!interactionResult.consumesAction() && this.isEdible()) {
//			InteractionResult interactionResult2 = this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
//			return interactionResult2 == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : interactionResult2;
//		} else {
//			return interactionResult;
//		}
//	}
//
//	public static void placePatrolNode(UseOnContext context) {
//
//	}

	@Environment(CLIENT)
	@Override
	public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
		if (I18n.exists(this.getDescriptionId() + ".info")) {
			list.add(Component.translatable(this.getDescriptionId() + ".info"));
		}
	}
}
