package io.github.minifabric.minifabric_api.impl;

import minicraft.core.Game;
import minicraft.core.io.InputHandler;
import minicraft.core.io.Localization;
import minicraft.gfx.Color;
import minicraft.gfx.Font;
import minicraft.gfx.Screen;
import minicraft.screen.*;
import minicraft.screen.Menu;
import minicraft.screen.entry.SelectEntry;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.Person;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class ModsDisplay extends Display {

    private final Collection<ModContainer> modsList = FabricLoader.getInstance().getAllMods();

    private static ArrayList<String> modNames;
    private static ArrayList<String> modDescriptions;
    private static ArrayList<String> modVersions;
    private static ArrayList<String> modAuthors;

    Display parent;

    private static ArrayList<String> getModNames() {
        Collection<ModContainer> modsList = FabricLoader.getInstance().getAllMods();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> descs = new ArrayList<>();
        ArrayList<String> versions = new ArrayList<>();
        ArrayList<String> authors = new ArrayList<>();

        if(ModsDisplay.modNames != null) {
            names.addAll(ModsDisplay.modNames);
            return names;
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
                    names.add(name);
                    descs.add(container.getMetadata().getDescription() == null ? "" : container.getMetadata().getDescription());
                    versions.add(container.getMetadata().getVersion().toString());
                    String author;
                    if (container.getMetadata().getAuthors() != null) {
                        StringBuilder authorNames = new StringBuilder();
                        for (Person person : container.getMetadata().getAuthors()) {
                            if (!authorNames.isEmpty()) {
                                authorNames.append(", ");
                            }
                            authorNames.append(person.getName());
                        }
                        author = authorNames.toString();
                    } else {
                        author = "Unknown";
                    }
                    authors.add(author);
                }
            }
        }

        if(ModsDisplay.modNames == null) {
            ModsDisplay.modNames = new ArrayList<>();
            ModsDisplay.modDescriptions = new ArrayList<>();
            ModsDisplay.modVersions = new ArrayList<>();
            ModsDisplay.modAuthors = new ArrayList<>();
        } else {
            ModsDisplay.modNames.clear();
            ModsDisplay.modDescriptions.clear();
            ModsDisplay.modVersions.clear();
            ModsDisplay.modAuthors.clear();
        }

        ModsDisplay.modNames.addAll(names);
        ModsDisplay.modDescriptions.addAll(descs);
        ModsDisplay.modVersions.addAll(versions);
        ModsDisplay.modAuthors.addAll(authors);

        return names;
    }

    public ModsDisplay() {
        super(true, true);
    }

    @Override
    public void tick(InputHandler input) {
        super.tick(input);
        if (input.getMappedKey("exit").isClicked()) {
            Game.setDisplay(parent);
        } else if (input.getMappedKey("info").isClicked()) {
			try {
				Desktop.getDesktop().open(new File("mods/"));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
    }

    @Override
    public void init(Display parent) {
        this.parent = parent;

        ArrayList<String> modNames = getModNames();

        SelectEntry[] entries = new SelectEntry[ModsDisplay.modNames.size()];

        for(int i = 0; i < entries.length; i++) {
            String description = modDescriptions.get(i);

            entries[i] = new SelectEntry(modNames.get(i), () -> {
                Game.setDisplay(new BookDisplay(description, false));
                }, false) {};
        }



        menus = new Menu[] {
                new Menu.Builder(false, 0, RelPos.CENTER, entries)
                        .setDisplayLength(6)
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
            String author = modAuthors.get(sel);
            int col = Color.WHITE;
            Font.drawCentered(Localization.getLocalized("Name:") + " " + name, screen, Font.textHeight() * 2, col);
            Font.drawCentered(Localization.getLocalized("Version:") + " " + version, screen, Font.textHeight() * 7/2, col);
            Font.drawCentered(Localization.getLocalized("Authors:") + " " + author, screen, Font.textHeight() * 5, col);
        }

        Font.drawCentered(Game.input.getMapping("info") + Localization.getLocalized(" to open mods folder"), screen, Screen.h - 30, Color.GRAY);
        Font.drawCentered(Game.input.getMapping("select") + Localization.getLocalized(" to confirm"), screen, Screen.h - 20, Color.GRAY);
        Font.drawCentered(Game.input.getMapping("exit") + Localization.getLocalized(" to return"), screen, Screen.h - 10, Color.GRAY);

        String title = Localization.getLocalized("Fabric Mod Menu");
        int color = Color.WHITE;

        int y = Screen.h - Font.textHeight();

        Font.drawCentered(title, screen, 0, color);
    }
}
