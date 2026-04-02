package com.nutonmod.compat;

import com.nutonmod.NutonMod;
import com.nutonmod.block.ModBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class PolishingMachineCategory implements DisplayCategory<BasicDisplay> {
    public static final Identifier TEXTURE = Identifier.of(NutonMod.MOD_ID, "textures/gui/polishing_machine_gui.png");
    private static final int DISPLAY_WIDTH = 176;
    private static final int DISPLAY_HEIGHT = 78;
    public static final CategoryIdentifier<PolishingMachineDisplay> POLISHING_MACHINE =
            CategoryIdentifier.of(NutonMod.MOD_ID, "polishing_machine");
    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return POLISHING_MACHINE;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("container.polishing_machine");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModBlocks.POLISHING_MACHINE.asItem().getDefaultStack());
    }

    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        final Point startPoint = new Point(bounds.getCenterX() - DISPLAY_WIDTH / 2, bounds.getCenterY() - DISPLAY_HEIGHT / 2);
        List<Widget> widgets = new ArrayList<>();

        widgets.add(Widgets.createTexturedWidget(TEXTURE, new Rectangle(startPoint.x, startPoint.y, DISPLAY_WIDTH, DISPLAY_HEIGHT)));
        if (!display.getInputEntries().isEmpty()){
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 80, startPoint.y + 11)).entries(display.getInputEntries().get(0)));
        }
        if (!display.getOutputEntries().isEmpty()){
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 80, startPoint.y + 59)).entries(display.getOutputEntries().get(0)));
        }
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return DISPLAY_HEIGHT;
    }
}
