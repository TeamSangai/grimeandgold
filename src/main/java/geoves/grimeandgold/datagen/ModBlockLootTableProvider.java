package geoves.grimeandgold.datagen;

import geoves.grimeandgold.blocks.ModBlocks;
import geoves.grimeandgold.items.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.ItemIds;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders.between;

public class ModBlockLootTableProvider extends FabricBlockLootSubProvider {
    public ModBlockLootTableProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture);
    }

    @Override
    public void generate() {
        dropSelf(ModBlocks.IRON_SLAG);dropSelf(ModBlocks.COPPER_SLAG);dropSelf(ModBlocks.GOLD_SLAG);dropSelf(ModBlocks.BENTHIC_SAPLING);
        dropSelf(ModBlocks.BENTHIC_LOG);dropSelf(ModBlocks.BENTHIC_WOOD);dropSelf(ModBlocks.GLOBE_THISTLE);dropSelf(ModBlocks.DESERT_PRIMROSE);
        dropSelf(ModBlocks.DESERT_LAVENDER);dropSelf(ModBlocks.ANCHOR_BLOSSOM);dropSelf(ModBlocks.AQUATIC_SPIN_ROSE);dropSelf(ModBlocks.SPIRAL_DAFFODIL);
        dropSelf(ModBlocks.GRIME_BRICKS);dropSelf(ModBlocks.GRIMEBARREL);

        dropOther(ModBlocks.BENTHIC_BRANCH, ModItems.BENTHIC_BRANCH_ITEM);dropOther(ModBlocks.BENTHIC_WALL_BRANCH, ModItems.BENTHIC_BRANCH_ITEM);
        createSilkTouchDispatchTable(ModBlocks.DRY_TALL_FERN, this.applyExplosionDecay(ModBlocks.DRY_TALL_FERN, LootItem.lootTableItem(ModBlocks.DRY_TALL_FERN.asItem())));
        createSilkTouchDispatchTable(ModBlocks.DRY_FERN, this.applyExplosionDecay(ModBlocks.DRY_FERN, LootItem.lootTableItem(ModBlocks.DRY_FERN.asItem())));
        createSilkTouchDispatchTable(ModBlocks.DRY_BUSH, this.applyExplosionDecay(ModBlocks.DRY_BUSH, LootItem.lootTableItem(ModBlocks.DRY_BUSH.asItem())));

        createMultipleOreDrops(ModBlocks.COOLED_GOLD_SLAG, Items.GOLD_NUGGET, 1, 4);createMultipleOreDrops(ModBlocks.COOLED_IRON_SLAG, Items.IRON_NUGGET, 1, 3);
        createMultipleOreDrops(ModBlocks.COOLED_COPPER_SLAG, Items.COPPER_NUGGET, 2, 6);

    }
    public LootTable.Builder createMultipleOreDrops(final Block block, Item item, int minDrops, int maxDrops) {

        return this.createSilkTouchDispatchTable(block, this.applyExplosionDecay(
                block, LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(between(minDrops, maxDrops)))
                        .apply(ApplyBonusCount.addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))));
    }
}
