package io.github.minifabric.minifabric_api.mixin;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.screen.AboutMenu;
import com.mojang.ld22.screen.InstructionsMenu;
import com.mojang.ld22.screen.Menu;
import com.mojang.ld22.screen.TitleMenu;
import com.mojang.ld22.sound.Sound;
import io.github.minifabric.minifabric_api.impl.ModsDisplay;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.*;

@Mixin(TitleMenu.class)
public class TitleDisplayMixin extends Menu {
    @Shadow @Final @Mutable private static String[] options;
    @Shadow private int selected;

    /**
     * @author PseudoDistant
     * @reason Fuck it, not like anyone else would edit this.
     */
    @Overwrite
    public void tick() {
        if (this.input.up.clicked) {
            --this.selected;
        }

        if (this.input.down.clicked) {
            ++this.selected;
        }

        int len = options.length;
        if (this.selected < 0) {
            this.selected += len;
        }

        if (this.selected >= len) {
            this.selected -= len;
        }

        if (this.input.attack.clicked || this.input.menu.clicked) {
            if (this.selected == 0) {
                Sound.test.play();
                this.game.resetGame();
                this.game.setMenu((Menu) null);
            }

            if (this.selected == 1) {
                this.game.setMenu(new InstructionsMenu(this));
            }

            if (this.selected == 2) {
                this.game.setMenu(new AboutMenu(this));
            }

            if (this.selected == 3) {
                this.game.setMenu(new ModsDisplay(this));
            }
        }
    }

    /**
     * @author PseudoDistant
     * @reason See tick
     */
    @Overwrite
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

        for(i = 0; i < 4; ++i) {
            String msg = options[i];
            int col = Color.get(0, 222, 222, 222);
            if (i == this.selected) {
                msg = "> " + msg + " <";
                col = Color.get(0, 555, 555, 555);
            }

            Font.draw(msg, screen, (screen.w - msg.length() * 8) / 2, (8 + i) * 8, col);
        }

        Font.draw("(Arrow keys,X and C)", screen, 0, screen.h - 8, Color.get(0, 111, 111, 111));
    }

    static {
        options = ArrayUtils.add(options, "Mods");
    }
}
