package geoves.grimeandgold.entities;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.entities.custom.siftgrub.SiftGrub;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.Identifier;

public class ModEntityDataSerializers {
    public static final EntityDataSerializer<SiftGrub.State> SIFT_GRUB_STATE = EntityDataSerializer.forValueType(SiftGrub.State.STREAM_CODEC);

    public static void register() {
        FabricEntityDataRegistry.register(Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "sift_grub_state"), SIFT_GRUB_STATE);
    }
}
