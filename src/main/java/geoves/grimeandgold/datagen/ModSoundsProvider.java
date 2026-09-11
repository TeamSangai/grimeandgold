package geoves.grimeandgold.datagen;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.sounds.ModSounds;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModSoundsProvider extends FabricSoundsProvider {
    public ModSoundsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registryLookup, SoundExporter exporter) {
        exporter.add(ModSounds.FLY_AMBIENT, SoundTypeBuilder.of(ModSounds.FLY_AMBIENT).subtitle("sounds.grimeandgold.fly_ambient")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "fly_ambient_1"))));

    }

    @Override
    public String getName() {
        return "Grime And Gold Sounds";
    }
}
