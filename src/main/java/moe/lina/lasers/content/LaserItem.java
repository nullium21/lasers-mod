package moe.lina.lasers.content;

import moe.lina.lasers.LasersMod;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

//? if >=1.20.6 {
/*import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.component.DataComponentType;
import net.minecraft.client.item.TooltipType;
import static moe.lina.lasers.LasersMod.id;
*///?} else {
import net.minecraft.client.item.TooltipContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import net.minecraft.nbt.NbtOps;
//?}

import java.util.List;

public class LaserItem extends BlockItem {

    //? if >=1.20.6 {
    /*public static final DataComponentType<BlockPos> TARGETED_BLOCK = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            id("targeted_block"),
            DataComponentType.<BlockPos>builder()
                    .codec(BlockPos.CODEC).packetCodec(BlockPos.PACKET_CODEC)
                    .build());
    *///?}

    public LaserItem() {
        super(LasersMod.LASER_BLOCK, new Settings()
                /*? if >=1.20.6 >>*//*.component(TARGETED_BLOCK,null)*/ );
    }

    @Override
    //? if >=1.20.6 {
    /*public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
    *///?} else
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        //? if >=1.20.6 {
        /*super.appendTooltip(stack, context, tooltip, type);
        *///?} else {
        super.appendTooltip(stack, world, tooltip, context);
        //?}

        var currentTarget = getTargetedBlock(stack);

        tooltip.add(Text.literal("Shift-RMB to point at a block"));
        if (currentTarget != null) tooltip.add(Text.literal("Current target: ").append(currentTarget.toShortString()));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getPlayer() != null && context.getPlayer().isSneaking()) {
            LasersMod.LOGGER.info("laser item used on {}", context.getBlockPos());

            setTargetedBlock(context.getStack(), context.getBlockPos());
            return ActionResult.CONSUME_PARTIAL;
        }

        return super.useOnBlock(context);
    }

    private static BlockPos getTargetedBlock(ItemStack stack) {
        //? if >=1.20.6 {
        /*return stack.get(TARGETED_BLOCK);
        *///?} else {
        return BlockPos.CODEC.parse(NbtOps.INSTANCE, stack.getNbt().get("TargetedBlockPos")).result().orElse(null);
        //?}
    }

    private static void setTargetedBlock(ItemStack stack, BlockPos target) {
        //? if >=1.20.6 {
        /*stack.set(TARGETED_BLOCK, target);
        *///?} else {
        stack.setSubNbt("TargetedBlockPos", BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, target).result().orElse(null));
        //?}
    }
}
