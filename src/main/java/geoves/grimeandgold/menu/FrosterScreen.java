package geoves.grimeandgold.menu;

import geoves.grimeandgold.GrimeAndGold;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class FrosterScreen extends AbstractContainerScreen<FrosterMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "textures/gui/froster_ui.png");
    private final Identifier ProgressSprite = Identifier.withDefaultNamespace("container/furnace/burn_progress");

    public FrosterScreen(FrosterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int xo = this.leftPos;
        int yo = this.topPos;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

        int ProgressWidth = Mth.ceil((this.menu).getScaledArrowProgress() * 24.0F);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.ProgressSprite, 24, 16, 0, 0, xo + 79, yo + 34, ProgressWidth, 16);
    }

}
