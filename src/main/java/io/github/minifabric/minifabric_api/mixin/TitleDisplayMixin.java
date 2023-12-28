package io.github.minifabric.minifabric_api.mixin;

import com.mojang.ld22.GameState;
import com.mojang.ld22.screen.controlmenu.*;
import com.mojang.ld22.sound.Sound;
import io.github.minifabric.minifabric_api.impl.ModsDisplay;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleMenu.class)
public class TitleDisplayMixin extends SelectControlMenu {
    @Final @Mutable @Shadow private static String[] defaultOptions;

    public TitleDisplayMixin(ControlMenu parent) {
        super(parent);
    }

    /**
     * @author PseudoDistant
     * @reason Fuck it, not like anyone else would edit this.
     */
    @Overwrite
    public void optionSelected(int selected) {
        if (selected == 8) {
            this.gameControl.gameState.respawn();
            this.gameControl.setMenu((ControlMenu)null);
        }

        if (selected == 7) {
            this.gameControl.setMenu((ControlMenu)null);
        }

        if (selected == 6) {
            Sound.test.play();
            this.gameControl.gameState = new GameState(this.gameControl);
            this.gameControl.gameState.resetGame();
            this.gameControl.setMenu((ControlMenu)null);
        }

        if (selected == 5) {
            this.gameControl.setMenu(new SaveMenu(this));
        }

        if (selected == 4) {
            this.gameControl.setMenu(new LoadMenu(this));
        }

        if (selected == 3) {
            this.gameControl.setMenu(new HighScoreMenu(this));
        }

        if (selected == 2) {
            this.gameControl.setMenu(new InstructionsMenu(this));
        }

        if (selected == 1) {
            this.gameControl.setMenu(new AboutMenu(this));
        }

        if (selected == 0) {
            this.gameControl.setMenu(new ModsDisplay(this));
        }

    }

    static {
        defaultOptions = ArrayUtils.add(defaultOptions,0, "Mods");
    }
}
