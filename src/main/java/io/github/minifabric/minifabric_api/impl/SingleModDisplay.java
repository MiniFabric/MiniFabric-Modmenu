package io.github.minifabric.minifabric_api.impl;

import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.screen.Menu;
import com.mojang.ld22.gfx.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingleModDisplay extends Menu {
	private Menu parent;
	private String title;
	private String version;
	ArrayList<String> desc = new ArrayList<>();


	@Override
	public void tick() {
		if (this.input.attack.clicked || this.input.menu.clicked) {
			this.game.setMenu(this.parent);
		}

	}
	public SingleModDisplay(Menu parent, String mod_name, String content, String version) {
		this.parent = parent;
		this.title = mod_name;
		this.version = version;

		Pattern regex = Pattern.compile("(.{1,20}(?:\\s|$))|(.{0,10})", Pattern.DOTALL);
		Matcher regexMatcher = regex.matcher(content);
		while (regexMatcher.find()) {
			desc.add(regexMatcher.group());
		}
	}

	@Override
	public void render(Screen screen) {
		int screenPos = 24;
		screen.clear(0);
		Font.draw(title, screen, 4, 8, Color.get(0, 333, 333, 333));
		for (String s : desc) {
			Font.draw(s, screen, 0, screenPos, Color.get(0, 333, 333, 333));
			screenPos += 8;
		}

		Font.draw(version, screen, 4, screenPos, Color.get(0, 50, 50, 50));
	}
}
