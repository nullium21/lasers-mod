package moe.lina.lasers.content;

import moe.lina.lasers.LasersMod;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

import java.util.List;

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
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        var currentTarget = stack.get(TARGETED_BLOCK);

        tooltip.add(Text.literal("Shift-RMB to point at a block"));
        tooltip.add(Text.literal("Current target: ").append(currentTarget == null ? "---" : currentTarget.toShortString()));
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
