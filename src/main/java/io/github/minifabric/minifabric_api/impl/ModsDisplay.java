package io.github.minifabric.minifabric_api.impl;

import minicraft.core.Game;
import minicraft.core.io.InputHandler;
import minicraft.core.io.Localization;
import minicraft.gfx.Color;
import minicraft.gfx.Font;
import minicraft.gfx.Screen;
import minicraft.screen.*;
import minicraft.screen.entry.SelectEntry;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class ModsDisplay extends Display {

    private final Collection<ModContainer> modsList = FabricLoader.getInstance().getAllMods();
    private static String modName = "";

    public static String getModName() { return modName; }

    private static ArrayList<String> modNames = null;
    private static final ArrayList<String> modVersions = new ArrayList<>();

    Display parent;

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

    public ModsDisplay() {
        super(true, true);
    }

    @Override
    public void tick(InputHandler input) {
        super.tick(input);
        if (input.getKey("exit").clicked) {
            Game.setDisplay(parent);
        }
    }

    @Override
    public void init(Display parent) {
        modName = "";
        this.parent = parent;

        ArrayList<String> modNames = getModNames();

        SelectEntry[] entries = new SelectEntry[modNames.size()];

        for(int i = 0; i < entries.length; i++) {
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
