package geoves.grimeandgold.blocks.enums;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum Output implements StringRepresentable {
    BONEMEAL,
    CALCITE,
    DIRT,
    GRIME,
    BIOMASS,
    TBD;

    /**
     * @return
     */
    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
