package io.github.minifabric.minifabric_api.mixin;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.gfx.SpriteSheet;
import com.mojang.ld22.screen.controlmenu.ControlMenu;
import com.mojang.ld22.screen.controlmenu.SelectControlMenu;
import com.mojang.ld22.screen.controlmenu.TitleMenu;
import io.github.minifabric.minifabric_api.impl.ModsDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.mojang.ld22.screen.controlmenu.SelectControlMenu.infoCol;

@Mixin(SelectControlMenu.class)
public class SelectControlMenuMixin extends ControlMenu {
	@Shadow protected int selected;
	public SelectControlMenuMixin(ControlMenu parent) {
		super(parent);
	}
	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lcom/mojang/ld22/screen/controlmenu/SelectControlMenu;optionSelected(I)V"))
	public void removeClose(SelectControlMenu instance, int i) {
		if (this.input.attack.clicked || this.input.menu.clicked) {
			((SelectControlMenu)(Object)this).optionSelected(selected);
		} else if (this.input.close.clicked && !((SelectControlMenu)(Object) this instanceof TitleMenu)) {
			this.gameControl.setMenu(this.parent);
		}
	}

	@Redirect(method = "render(Lcom/mojang/ld22/gfx/Screen;Z)V", at = @At(value = "INVOKE", target = "Lcom/mojang/ld22/gfx/Font;draw(Ljava/lang/String;Lcom/mojang/ld22/gfx/Screen;III)V", ordinal = 2))
	private void addCredits(String msg, Screen screen, int x, int y, int col) {
		if ((SelectControlMenu)(Object)this instanceof ModsDisplay) {
			Font.draw("ModMenu by PseudoDistant!", screen, 0, screen.h - SpriteSheet.spriteSize, Color.get(0, 50, 50, 50));
		} else {
			Font.draw("(§/°/[/], X and C)", screen, 0, screen.h - SpriteSheet.spriteSize, infoCol);
		}
	}
}
