package geoves.grimeandgold;

import geoves.grimeandgold.blocks.ModBlocks;
import geoves.grimeandgold.blocks.entities.ModBlockEntities;
import geoves.grimeandgold.creativeTabs.ModCreativeTabs;
import geoves.grimeandgold.entities.ModEntityTypes;
import geoves.grimeandgold.entities.custom.siftgrub.SiftGrubEntity;
import geoves.grimeandgold.entities.custom.siftfly.SiftFlyEntity;
import geoves.grimeandgold.items.ModItems;
import geoves.grimeandgold.sounds.ModSounds;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrimeAndGold implements ModInitializer {
	// whether to use SmartBrainLib, because it has problems with newer versions
	public static final boolean USE_SBL = false;
	public static final String MOD_ID = "grimeandgold";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModCreativeTabs.registerModCreativeModeTabs();

		ModEntityTypes.registerModEntityTypes();
		ModEntityTypes.registerAttributes();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModSounds.registerSounds();

		this.registerModEntities();
	}

	private void registerModEntities() {
		// Idk why the warning
		FabricDefaultAttributeRegistry.register(ModEntityTypes.SIFT_FLY, SiftFlyEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(ModEntityTypes.SIFT_GRUB, SiftGrubEntity.createAttributes());
	}

	// M: Yooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo do you see this?
	// G: Yes, but did you?
	// M: :eyes:
}
