package geoves.grimeandgold.entities.ai;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.Optional;

public class MemoryModuleTypes<U> extends MemoryModuleType<U> {
    public static final MemoryModuleType<Integer> SIFT_COOLDOWN = register("sift_cooldown", Codec.INT);
//    public static final MemoryModuleType<Unit> SIFT_COOLDOWN = register("sift_cooldown", Unit.CODEC);

    public MemoryModuleTypes(Optional optional) {
        super(optional);
    }

    private static <U> MemoryModuleType<U> register(final String name, final Codec<U> codec) {
        return Registry.register(BuiltInRegistries.MEMORY_MODULE_TYPE, Identifier.withDefaultNamespace(name), new MemoryModuleType<>(Optional.of(codec)));
    }

    private static <U> MemoryModuleType<U> register(final String name) {
        return Registry.register(BuiltInRegistries.MEMORY_MODULE_TYPE, Identifier.withDefaultNamespace(name), new MemoryModuleType<>(Optional.empty()));
    }

    public static void register() {

    }
}
