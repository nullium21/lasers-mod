package moe.lina.lasers.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

import static moe.lina.lasers.LasersMod.*;

public class LasersDatagen implements DataGeneratorEntrypoint {

    private static FabricDataGenerator.Pack.Factory<FabricModelProvider> block(Block block) {
        return out -> new FabricModelProvider(out) {
            @Override
            public String getName() {
                return block.getClass().getSimpleName();
            }

            @Override
            public void generateBlockStateModels(BlockStateModelGenerator bsgen) {
                bsgen.registerSimpleState(block);
            }

            @Override
            public void generateItemModels(ItemModelGenerator imgen) {
                imgen.register(block.asItem(), Models.GENERATED);
            }
        };
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        var pack = gen.createPack();
        pack.addProvider(block(LASER_BLOCK));
    }
}
