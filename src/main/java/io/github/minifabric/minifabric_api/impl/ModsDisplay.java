package io.github.minifabric.minifabric_api.impl;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.screen.AboutMenu;
import com.mojang.ld22.screen.InstructionsMenu;
import com.mojang.ld22.screen.Menu;
import com.mojang.ld22.sound.Sound;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class ModsDisplay extends Menu {
    private static String[] defaultOptions;

    private int selected = 0;
    public static final int titleColor = Color.get(0, 8, 131, 551);
    private Menu parent;

    private final Collection<ModContainer> modsList = FabricLoader.getInstance().getAllMods();

    private static ArrayList<String> modNames;
    private static ArrayList<String> modVersions;

    private static ArrayList<String> modDescriptions;

    public ModsDisplay(Menu parent) {
        this.parent = parent;
    }

    //TODO: Scrolling

    @Override
    public void tick() {
        if (this.input.up.clicked) {
            --this.selected;
        }

        if (this.input.down.clicked) {
            ++this.selected;
        }

        int len = modNames.size();
        if (this.selected < 0) {
            this.selected += len;
        }

        if (this.selected >= len) {
            this.selected -= len;
        }

        if (this.input.attack.clicked || this.input.menu.clicked) {
            if (this.selected == modNames.size() - 1) {
                this.game.setMenu(parent);
            } else {
                String description = modDescriptions.get(selected) == null ?
                        "" : modDescriptions.get(selected);
                this.game.setMenu(new SingleModDisplay(this, modNames.get(selected), description, modVersions.get(selected)));

            }
        }
    }

    private static ArrayList<String> getModNames() {
        Collection<ModContainer> modsList = FabricLoader.getInstance().getAllMods();
        ArrayList<String> modNames = new ArrayList<>();
        ArrayList<String> modDescs = new ArrayList<>();
        ArrayList<String> versions = new ArrayList<>();

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
                    versions.add(container.getMetadata().getVersion().toString());
                    modDescs.add(container.getMetadata().getDescription());
                }
            }
        }
        modNames.add("Back to menu");
        versions.add("");
        modDescs.add("");

        if(ModsDisplay.modNames == null || ModsDisplay.modVersions == null || ModsDisplay.modDescriptions == null) {
            ModsDisplay.modNames = new ArrayList<>();
            ModsDisplay.modDescriptions = new ArrayList<>();
            ModsDisplay.modVersions = new ArrayList<>();
        } else {
            ModsDisplay.modNames.clear();
            ModsDisplay.modDescriptions.clear();
            ModsDisplay.modVersions.clear();
        }


        ModsDisplay.modNames.addAll(modNames);
        ModsDisplay.modVersions.addAll(versions);
        ModsDisplay.modDescriptions.addAll(modDescs);

        return modNames;
    }

    public void init() {
        defaultOptions = getModNames().toArray(new String[0]);
        int[] new_selectable = new int[defaultOptions.length];

        int amount;
        amount = modNames.size();
        for(amount = 0; amount < defaultOptions.length; ++amount) {
            new_selectable[amount] = 1;
        }
    }

    public void render(Screen screen) {
        screen.clear(0);
        int h = 2;
        int w = 13;
        int titleColor = Color.get(0, 8, 131, 551);
        int xo = (screen.w - w * 8) / 2;
        int yo = 24;

        int i;
        for(i = 0; i < h; ++i) {
            for(int x = 0; x < w; ++x) {
                screen.render(xo + x * 8, yo + i * 8, x + (i + 6) * 32, titleColor, 0);
            }
        }

        for(i = 0; i < getModNames().size(); ++i) {
            String msg = getModNames().get(i);
            int col = Color.get(0, 222, 222, 222);
            //if (i == this.selected) {
            if (i == selected) {
                msg = "> " + msg + " <";
                col = Color.get(0, 555, 555, 555);
            }

            Font.draw(msg, screen, (screen.w - msg.length() * 8) / 2, (8 + i) * 8, col);
        }
    }
}