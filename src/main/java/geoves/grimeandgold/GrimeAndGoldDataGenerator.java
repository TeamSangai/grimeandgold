package geoves.grimeandgold;

import geoves.grimeandgold.datagen.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

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
	}
}
