package geoves.grimeandgold.entities;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.entities.custom.siftgrub.SiftGrub;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.animal.sniffer.Sniffer;

public class ModEntityDataSerializers {
    public static final EntityDataSerializer<SiftGrub.State> SIFT_GRUB_STATE = EntityDataSerializer.forValueType(SiftGrub.State.STREAM_CODEC);

    public static void register() {
        EntityDataSerializers.registerSerializer(SIFT_GRUB_STATE);
    }
}
