package io.github.minifabric.minifabric_api.impl;

import com.mojang.ld22.screen.controlmenu.ControlMenu;
import com.mojang.ld22.screen.controlmenu.TextControlMenu;
import com.mojang.ld22.gfx.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingleModDisplay extends TextControlMenu {
	public SingleModDisplay(ControlMenu parent, String mod_name, String content, String version) {
		super(parent);
		this.title = mod_name;
		List matchList = new ArrayList();
		Pattern regex = Pattern.compile("(.{1,24}(?:\\s|$))|(.{0,10})", Pattern.DOTALL);
		Matcher regexMatcher = regex.matcher(content);
		while (regexMatcher.find()) {
			matchList.add(regexMatcher.group());
		}
		matchList.forEach(phrase -> {
			this.addTextLine((String) phrase, Color.get(0, 333, 333, 333));
		});

		this.addTextLine(version, Color.get(0, 50, 50, 50));
	}
}
