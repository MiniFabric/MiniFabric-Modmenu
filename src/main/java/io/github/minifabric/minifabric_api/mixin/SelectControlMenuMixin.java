package io.github.minifabric.minifabric_api.mixin;

import com.mojang.ld22.screen.controlmenu.ControlMenu;
import com.mojang.ld22.screen.controlmenu.SelectControlMenu;
import com.mojang.ld22.screen.controlmenu.TitleMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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
}
