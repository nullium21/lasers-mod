package moe.lina.lasers.datagen;

import java.util.Optional;

import moe.lina.lasers.base.HasIdentifier;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;

import static moe.lina.lasers.LasersMod.*;

public class LasersDatagen implements DataGeneratorEntrypoint {

    private static <T extends Block & HasIdentifier> FabricDataGenerator.Pack.Factory<FabricModelProvider> simpleBlock(T block) {
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

    private static <T extends Block & HasIdentifier> FabricDataGenerator.Pack.Factory<FabricModelProvider> directionalBlock(T block) {
        return out -> new FabricModelProvider(out) {
            @Override
            public String getName() {
                return block.getClass().getSimpleName();
            }

            @Override
            public void generateBlockStateModels(BlockStateModelGenerator bsgen) {
                bsgen.blockStateCollector.accept(VariantsBlockStateSupplier.create(block, BlockStateVariant.create()
                        .put(VariantSettings.MODEL, block.getId().withPrefixedPath("block/")))
                        .coordinate(BlockStateModelGenerator.createNorthDefaultRotationStates())
                );
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
        pack.addProvider(simpleBlock(LASER_BLOCK));
        pack.addProvider(directionalBlock(MIRROR_BLOCK));
    }
}
