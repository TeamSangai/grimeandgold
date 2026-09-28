package geoves.grimeandgold.data;

import geoves.grimeandgold.GrimeAndGold;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final DataComponentType<BlockTransformer> BURROW_TRANSFORMER = register("burrow_transformer",
            builder -> builder.persistent(BlockTransformer.DIRECT_CODEC));


    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name),
                builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void registerDataComponents() {
        GrimeAndGold.LOGGER.info("Registering Data Components for " + GrimeAndGold.MOD_ID);
    }
}
