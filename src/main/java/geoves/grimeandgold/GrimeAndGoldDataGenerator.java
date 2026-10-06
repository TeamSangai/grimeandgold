package geoves.grimeandgold;

import geoves.grimeandgold.datagen.*;
import geoves.grimeandgold.worldgen.ModFeatures;
import geoves.grimeandgold.worldgen.ModPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class GrimeAndGoldDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();

		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModBlockTagsProvider::new);
		pack.addProvider(ModBlockLootTableProvider::new);
		pack.addProvider(ModItemTagsProvider::new);
		pack.addProvider(ModSoundsProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModEntityTagsProvider::new);
		pack.addProvider(ModRegistryDataProvider::new);

	}
	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap);
		registryBuilder.add(Registries.FEATURE, ModFeatures::bootstrap);
		registryBuilder.add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap);

	}
}
