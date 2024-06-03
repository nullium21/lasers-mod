package moe.lina.lasers.content;

import moe.lina.lasers.LasersMod;
import net.minecraft.component.DataComponentType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

import static moe.lina.lasers.LasersMod.id;

public class LaserItem extends BlockItem {

    public static final DataComponentType<BlockPos> TARGETED_BLOCK = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            id("targeted_block"),
            DataComponentType.<BlockPos>builder()
                    .codec(BlockPos.CODEC).packetCodec(BlockPos.PACKET_CODEC)
                    .build()
    );

    public LaserItem() {
        super(LasersMod.LASER_BLOCK, new Settings()
                .component(TARGETED_BLOCK, null));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getPlayer() != null && !context.getPlayer().isSneaking()) {
            LasersMod.LOGGER.info("laser item used on {}", context.getBlockPos());

            context.getStack().set(TARGETED_BLOCK, context.getBlockPos());
            return ActionResult.SUCCESS_NO_ITEM_USED;
        }

        return super.useOnBlock(context);
    }
}
