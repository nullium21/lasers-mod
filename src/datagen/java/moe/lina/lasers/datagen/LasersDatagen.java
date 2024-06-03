package moe.lina.lasers.datagen;

import java.util.Optional;

import moe.lina.lasers.base.HasIdentifier;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;

import static moe.lina.lasers.LasersMod.*;

public class LasersDatagen implements DataGeneratorEntrypoint {

    private static <T extends Block & HasIdentifier> FabricDataGenerator.Pack.Factory<FabricModelProvider> block(T block) {
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
                var model = new Model(Optional.of(block.getId().withPrefixedPath("block/")), Optional.empty());
                imgen.register(block.asItem(), model);
            }
        };
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        var pack = gen.createPack();
        pack.addProvider(block(LASER_BLOCK));
    }
}
