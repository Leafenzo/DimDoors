package org.dimdev.dimdoors.item;

import com.sk89q.worldedit.world.World;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class PathingDeviceItem extends Item {
    public PathingDeviceItem(Properties properties) {
        super(properties);
    }

    public static final String ID = "pathing_device";

    @Override
    public boolean canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) { return false;}

//    @Override
//    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
//        ItemStack itemStack = user.getItemInHand(hand);
//        double x = user.getX() + user.getRotationVector().x * 4.5;
//        double y = user.getEyeY() + user.getRotationVector().y * 4.5;
//        double z = user.getZ() + user.getYHeadRot() * 4.5;
//        BlockPos pos = new BlockPos((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
//
//        if(!world.isOutsideBuildHeight(pos) && world.getWorldBorder().isWithinBounds(pos) && world.getBlockState(pos).canBeReplaced()) {
//            ItemPlacementContext context = new ItemPlacementContext(world, user, hand, itemStack, new BlockHitResult(pos.toCenterPos(), Direction.NORTH, pos, false));
//            ActionResult actionResult = useOnBlock(context);
//            if  (actionResult.isAccepted()) {
//                return TypedActionResult.success(itemStack, !world.isClient());
//            }
//        }
//        return super.use(world, user, hand);
//    }

}
