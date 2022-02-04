package io.github.minifabric.minifabric_api.mixin;

import io.github.minifabric.minifabric_api.impl.MenuDuck;
import minicraft.screen.Menu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Menu.class)
public class MenuAccessor implements MenuDuck {
    @Shadow private int selection;

    public int selectionGetter() {
        return selection;
    }
}
