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
    //private static final String[] defaultOptions = new String[]{"Highscore", "Load game", "Save game", "Start new game", "Resume game", "Respawn"};
    private static String[] defaultOptions;
    public static final int titleColor = Color.get(0, 8, 131, 551);

    private final Collection<ModContainer> modsList = FabricLoader.getInstance().getAllMods();
    private static String modName = "";

    public static String getModName() { return modName; }

    private static ArrayList<String> modNames;
    private static final ArrayList<String> modVersions = new ArrayList<>();

    public ModsDisplay(SelectControlMenu parent) {
        super(parent);
        this.parent = parent;
    }

    private static ArrayList<String> getModNames() {
        Collection<ModContainer> modsList = FabricLoader.getInstance().getAllMods();
        ArrayList<String> modNames = new ArrayList<>();

        if(ModsDisplay.modNames != null) {
            modNames.addAll(ModsDisplay.modNames);
            return modNames;
        }

        if(modsList.isEmpty()) {
            System.err.println("ERROR: No mods are detected by Fabric Loader somehow...");
            return new ArrayList<>();
        }

        modVersions.clear();

        // Iterate between every mod loaded by Fabric.
        for (ModContainer container : modsList) {
            if (container != null) {
                String name = container.getMetadata().getName();
                if (!container.getMetadata().getId().equals("java")) {
                    modNames.add(name);
                    modVersions.add(container.getMetadata().getVersion().toString());
                }
            }
        }

        if(ModsDisplay.modNames == null)
            ModsDisplay.modNames = new ArrayList<>();
        else
            ModsDisplay.modNames.clear();

        ModsDisplay.modNames.addAll(modNames);

        return modNames;
    }

    public void init(GameControl gameControl, InputHandler input) {
        defaultOptions = getModNames().toArray(new String[0]);
        super.init(gameControl, input);
        int[] new_selectable = new int[defaultOptions.length];

        int amount;
        for(amount = 0; amount < defaultOptions.length; ++amount) {
            new_selectable[amount] = 1;
        }

        amount = defaultOptions.length;

        this.setOptions(Arrays.copyOf(defaultOptions, amount));
        this.selected = this.options.length - 1;
    }

    public void optionSelected(int selected) {
        String description = modsList.stream()
            .filter(mod -> Objects.equals(mod.getMetadata().getName(), defaultOptions[selected]))
            .findFirst().get().getMetadata().getDescription() == null ?
            "" : modsList.stream()
            .filter(mod -> Objects.equals(mod.getMetadata().getName(), defaultOptions[selected]))
            .findFirst().get().getMetadata().getDescription();
        this.gameControl.setMenu(new SingleModDisplay(this, defaultOptions[selected], description, modVersions.get(selected)));

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
        Font.draw(GameControl.VERSION, screen, screen.w - GameControl.VERSION.length() * SpriteSheet.spriteSize, screen.h - SpriteSheet.spriteSize, infoCol);
    }
}
/*
    private final Collection<ModContainer> modsList = FabricLoader.getInstance().getAllMods();
    private static String modName = "";

    public static String getModName() { return modName; }

    private static ArrayList<String> modNames = null;
    private static final ArrayList<String> modVersions = new ArrayList<>();

    ControlMenu parent;

    private static ArrayList<String> getModNames() {
        Collection<ModContainer> modsList = FabricLoader.getInstance().getAllMods();
        ArrayList<String> modNames = new ArrayList<>();

        if(ModsDisplay.modNames != null) {
            modNames.addAll(ModsDisplay.modNames);
            return modNames;
        }

        if(modsList.isEmpty()) {
            System.err.println("ERROR: No mods are detected by Fabric Loader somehow...");
            return new ArrayList<>();
        }

        modVersions.clear();

        // Iterate between every mod loaded by Fabric.
        for (ModContainer container : modsList) {
            if (container != null) {
                String name = container.getMetadata().getName();
                if (!container.getMetadata().getId().equals("java")) {
                    modNames.add(name);
                    modVersions.add(container.getMetadata().getVersion().toString());
                }
            }
        }

        if(ModsDisplay.modNames == null)
            ModsDisplay.modNames = new ArrayList<>();
        else
            ModsDisplay.modNames.clear();

        ModsDisplay.modNames.addAll(modNames);

        return modNames;
    }

    public ModsDisplay(ControlMenu parent) {
        super(parent);
        this.parent = parent;
    }

    @Override
    public void init(GameControl gameControl, InputHandler input) {
        super.init(gameControl, input);
        modName = "";

		ArrayList<String> modNames = getModNames();

        int[] entries = new int[modNames.size()];

        for(int i = 0; i < entries.length; i++) {
            entries[i] = 1;
            String name = modNames.get(i);
            final String version = modVersions.get(i);
            String description = modsList.stream()
                    .filter(mod -> Objects.equals(mod.getMetadata().getName(), name))
                    .findFirst().get().getMetadata().getDescription() == null ?
                    "" : modsList.stream()
                    .filter(mod -> Objects.equals(mod.getMetadata().getName(), name))
                    .findFirst().get().getMetadata().getDescription();


            entries[i] = new SelectEntry(modNames.get(i), () -> {
                Game.setDisplay(new BookDisplay(description, false));
                }, false) {};
        }



        menus = new Menu[] {
                new Menu.Builder(false, 0, RelPos.CENTER, entries)
                        .setDisplayLength(5)
                        .setScrollPolicies(1, true)
                        .createMenu()
        };
    }

    @Override
    public void render(Screen screen) {
        super.render(screen);

        int sel = ((MenuDuck)menus[0]).minifabric_modmenu$selectionGetter();
        if(sel >= 0 && sel < modVersions.size()) {
            String name = modNames.get(sel);
            String version = modVersions.get(sel);
            int col = Color.WHITE;
            Font.drawCentered(Localization.getLocalized("Name:") + " " + name, screen, Font.textHeight() * 2, col);
            Font.drawCentered(Localization.getLocalized("Mod version:") + " " + version, screen, Font.textHeight() * 7/2, col);
        }

        Font.drawCentered(Game.input.getMapping("select") + Localization.getLocalized(" to confirm"), screen, Screen.h - 60, Color.GRAY);
        Font.drawCentered(Game.input.getMapping("exit") + Localization.getLocalized(" to return"), screen, Screen.h - 40, Color.GRAY);

        String title = Localization.getLocalized("Fabric Mod Menu");
        int color = Color.WHITE;

        int y = Screen.h - Font.textHeight();

        Font.drawCentered(title, screen, 0, color);
    }
}
*/