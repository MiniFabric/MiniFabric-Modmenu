package io.github.minifabric.minifabric_api.mixin;

import io.github.minifabric.minifabric_api.impl.ModsDisplay;
import minicraft.core.Game;
import minicraft.gfx.Color;
import minicraft.gfx.Point;
import minicraft.gfx.Screen;
import minicraft.screen.*;
import minicraft.screen.entry.BlankEntry;
import minicraft.screen.entry.ListEntry;
import minicraft.screen.entry.SelectEntry;
import minicraft.screen.entry.StringEntry;
import minicraft.util.BookData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TitleDisplay.class)
public class TitleDisplayMixin extends Display {
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lminicraft/screen/Display;<init>(ZZ[Lminicraft/screen/Menu;)V"), index = 2)
    private static Menu[] yes(Menu[] menus) {
        return new Menu[]{(new Menu.Builder(false, 2, RelPos.CENTER, new ListEntry[]{new StringEntry("minicraft.displays.title.display.checking", Color.BLUE), new BlankEntry(), new SelectEntry("minicraft.displays.title.play", () -> {
            if (WorldSelectDisplay.getWorldNames().size() > 0) {
                Game.setDisplay(new Display(true, new Menu[]{(new Menu.Builder(false, 2, RelPos.CENTER, new ListEntry[]{new SelectEntry("minicraft.displays.title.play.load_world", () -> {
                    Game.setDisplay(new WorldSelectDisplay());
                }), new SelectEntry("minicraft.displays.title.play.new_world", () -> {
                    Game.setDisplay(new WorldGenDisplay());
                })})).createMenu()}));
            } else {
                Game.setDisplay(new WorldGenDisplay());
            }

        }), new SelectEntry("minicraft.display.options_display", () -> {
            Game.setDisplay(new OptionsMainMenuDisplay());
        }), new SelectEntry("Mods", () -> Game.setDisplay(new ModsDisplay())),
            new SelectEntry("minicraft.displays.skin", () -> {
            Game.setDisplay(new SkinDisplay());
        }), new SelectEntry("minicraft.displays.achievements", () -> {
            Game.setDisplay(new AchievementsDisplay());
        }), new SelectEntry("minicraft.displays.title.help", () -> {
            Game.setDisplay(new Display(true, new Menu[]{(new Menu.Builder(false, 1, RelPos.CENTER, new ListEntry[]{new BlankEntry(), new SelectEntry("minicraft.displays.title.help.instructions", () -> {
                Game.setDisplay(new BookDisplay(BookData.instructions.collect()));
            }), new SelectEntry("minicraft.displays.title.help.storyline_guide", () -> {
                Game.setDisplay(new BookDisplay(BookData.storylineGuide.collect()));
            }), new SelectEntry("minicraft.displays.title.help.about", () -> {
                Game.setDisplay(new BookDisplay(BookData.about.collect()));
            }), new SelectEntry("minicraft.displays.title.help.credits", () -> {
                Game.setDisplay(new BookDisplay(BookData.credits.collect()));
            })})).setTitle("minicraft.displays.title.help").createMenu()}));
        }), new SelectEntry("minicraft.displays.title.quit", Game::quit)})).setPositioning(new Point(Screen.w / 2, Screen.h * 3 / 5), RelPos.CENTER).createMenu()};
    }


}
