package geoves.grimeandgold.worldgen.tree;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.worldgen.ModFeatures;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.grower.TreeGrower;

public class ModTreeGrowers {
    public static final TreeGrower BENTHIC = new TreeGrower(GrimeAndGold.MOD_ID + ":benthic",
            WeightedList.of(ModFeatures.BENTHIC_TREE_KEY), WeightedList.of(), WeightedList.of(), null);
}
