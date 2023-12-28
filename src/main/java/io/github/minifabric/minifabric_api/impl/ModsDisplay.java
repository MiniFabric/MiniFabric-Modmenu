package io.github.minifabric.minifabric_api.impl;

import com.mojang.ld22.GameControl;
import com.mojang.ld22.GameState;
import com.mojang.ld22.InputHandler;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.gfx.SpriteSheet;
import com.mojang.ld22.screen.controlmenu.*;
import com.mojang.ld22.sound.Sound;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class ModsDisplay extends SelectControlMenu {
    public static final int titleColor = Color.get(0, 8, 131, 551);
    private static ArrayList<String> modNames;
    private static ArrayList<String> modDescriptions;
    private static ArrayList<String> modVersions;

    public ModsDisplay(SelectControlMenu parent) {
        super(parent);
        this.parent = parent;
    }

    private static ArrayList<String> getModNames() {
        Collection<ModContainer> modsList = FabricLoader.getInstance().getAllMods();
        ArrayList<String> modNames = new ArrayList<>();
        ArrayList<String> modVersions = new ArrayList<>();
        ArrayList<String> modDescs = new ArrayList<>();

        if(ModsDisplay.modNames != null) {
            modNames.addAll(ModsDisplay.modNames);
            return modNames;
        }

        if(modsList.isEmpty()) {
            System.err.println("ERROR: No mods are detected by Fabric Loader somehow...");
            return new ArrayList<>();
        }



        // Iterate between every mod loaded by Fabric.
        for (ModContainer container : modsList) {
            if (container != null) {
                String name = container.getMetadata().getName();
                if (!container.getMetadata().getId().equals("java")) {
                    modNames.add(name);
                    modVersions.add(container.getMetadata().getVersion().toString());
                    modDescs.add(container.getMetadata().getDescription() == null ? "" : container.getMetadata().getDescription());
                }
            }
        }

        if(ModsDisplay.modNames == null || ModsDisplay.modDescriptions == null || ModsDisplay.modVersions == null) {
            ModsDisplay.modNames = new ArrayList<>();
            ModsDisplay.modDescriptions = new ArrayList<>();
            ModsDisplay.modVersions = new ArrayList<>();
        } else {
            ModsDisplay.modNames.clear();
            ModsDisplay.modDescriptions.clear();
            ModsDisplay.modVersions.clear();
        }

        ModsDisplay.modNames.addAll(modNames);
        ModsDisplay.modVersions.addAll(modVersions);
        ModsDisplay.modDescriptions.addAll(modDescs);

        return modNames;
    }

    public void init(GameControl gameControl, InputHandler input) {
        super.init(gameControl, input);

        this.setOptions(Arrays.copyOf(getModNames().toArray(new String[0]), modNames.size()));
        this.selected = this.options.length - 1;
    }

    public void optionSelected(int selected) {
        this.gameControl.setMenu(new SingleModDisplay(this, modNames.get(selected), modDescriptions.get(selected), modVersions.get(selected)));

    }

    public void render(Screen screen) {
        screen.clear(0);
        int h = 2;
        int w = 13;
        int xo = (screen.w - w * SpriteSheet.spriteSize) / 2;
        int yo = 1;
        screen.renderSprite(xo, yo * SpriteSheet.spriteSize, 0, 28, titleColor, 0, w * SpriteSheet.spriteSize, h * SpriteSheet.spriteSize);
        xo = (screen.w - 4 * SpriteSheet.spriteSize) / 2;
        yo += h;
        screen.renderSprite(xo, yo * SpriteSheet.spriteSize, 13, 28, titleColor, 0, 4 * SpriteSheet.spriteSize, SpriteSheet.spriteSize);
        this.menuTop = yo + h + 1;
        super.render(screen, false);
    }
}