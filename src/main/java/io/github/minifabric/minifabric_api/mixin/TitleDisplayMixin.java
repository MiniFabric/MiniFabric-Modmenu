package io.github.minifabric.minifabric_api.mixin;

import io.github.minifabric.minifabric_api.impl.ModsDisplay;
import minicraft.core.Game;
import minicraft.gfx.Color;
import minicraft.gfx.Point;
import minicraft.gfx.Screen;
import minicraft.screen.*;
import minicraft.screen.entry.BlankEntry;
import minicraft.screen.entry.SelectEntry;
import minicraft.screen.entry.StringEntry;
import minicraft.util.BookData;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.Collection;

@Mixin(TitleDisplay.class)
public class TitleDisplayMixin extends Display {
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lminicraft/screen/Display;<init>(ZZ[Lminicraft/screen/Menu;)V"))
    private static Menu[] yes(Menu[] menus) {
        return new Menu[]{new Menu.Builder(false, 2, RelPos.CENTER,
                new StringEntry("Checking for updates...", Color.BLUE),
                new BlankEntry(),
                new BlankEntry(),
                new SelectEntry("Play", () -> /*Game.setMenu(new PlayDisplay())*/{
                    if (WorldSelectDisplay.getWorldNames().size() > 0)
                        Game.setMenu(new Display(true, new Menu.Builder(false, 2, RelPos.CENTER,
                                new SelectEntry("Load World", () -> Game.setMenu(new WorldSelectDisplay())),
                                new SelectEntry("New World", () -> Game.setMenu(new WorldGenDisplay()))
                        ).createMenu()));
                    else Game.setMenu(new WorldGenDisplay());
                }),
                new SelectEntry("Options", () -> Game.setMenu(new OptionsMainMenuDisplay())),
                new SelectEntry("Mods", () -> Game.setMenu(new ModsDisplay())),
                new SelectEntry("Skins", () -> Game.setMenu(new SkinDisplay())),
                new SelectEntry("Achievements", () -> Game.setMenu(new AchievementsDisplay())),
                new SelectEntry("Help", () ->
                        Game.setMenu(new Display(true, new Menu.Builder(false, 1, RelPos.CENTER,
                                new BlankEntry(),
                                new SelectEntry("Instructions", () -> Game.setMenu(new BookDisplay(BookData.instructions))),
                                new SelectEntry("Storyline Guide", () -> Game.setMenu(new BookDisplay(BookData.storylineGuide))),
                                new SelectEntry("About", () -> Game.setMenu(new BookDisplay(BookData.about))),
                                new SelectEntry("Credits", () -> Game.setMenu(new BookDisplay(BookData.credits)))
                        ).setTitle("Help").createMenu()))
                ),
                new SelectEntry("Quit", Game::quit)
        )
                .setPositioning(new Point(Screen.w / 2, Screen.h * 3 / 5), RelPos.CENTER)
                .createMenu()};
    }


}
