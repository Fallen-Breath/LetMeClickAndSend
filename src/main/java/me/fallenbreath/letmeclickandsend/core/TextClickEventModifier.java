/*
 * This file is part of the Let Me Click And Send project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Let Me Click And Send is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Let Me Click And Send is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Let Me Click And Send.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.fallenbreath.letmeclickandsend.core;

import me.fallenbreath.letmeclickandsend.LetMeClickAndSendMod;
import me.fallenbreath.letmeclickandsend.network.ServerNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;

public class TextClickEventModifier
{
	private static final Object lock = new Object();
	private static Text lastText = null;
	private static Text lastResult = null;

	public static Text modifyText(ServerPlayerEntity player, Text text)
	{
		int id = System.identityHashCode(text);
		LetMeClickAndSendMod.LOGGER.debug("Modifying click events in text@{} for player {} ({})", id, player.getName().getString(), player.getUuid());

		// If the player has LetMeClickAndSend on its client, it's not necessary to modify the text,
		// cuz the player can already send message via click event freely
		if (ServerNetworkHandler.doesPlayerHasMod(player))
		{
			LetMeClickAndSendMod.LOGGER.debug("Player {} ({}) has mod installed, skipping modification", player.getName().getString(), player.getUuid());
			return text;
		}

		// the modifyMessage is often invoked in batched
		// so cache and reuse the result
		synchronized (lock)
		{
			if (text == lastText && lastResult != null)
			{
				LetMeClickAndSendMod.LOGGER.debug("Cache hit for text@{}", id);
				return lastResult;
			}
		}

		LetMeClickAndSendMod.LOGGER.debug("Cache missed for text@{}", id);
		Text ret = modify(text);

		synchronized (lock)
		{
			lastText = text;
			return lastResult = ret;
		}
	}

	private static Text modify(Text text)
	{
		return modifyImpl(text, 0);
	}

	private static Text modifyImpl(Text text, int depth)
	{
		// depth limit ref: net.minecraft.text.Texts.parse
		if (depth > 100)
		{
			return text.copy();
		}

		MutableText copied = text.copyContentOnly();

		// args
		TextContent content = copied.getContent();
		if (content instanceof TranslatableTextContent translatableTextContent)
		{
			String key = translatableTextContent.getKey();
			Object[] args = translatableTextContent.getArgs().clone();
			for (int i = 0; i < args.length; i++)
			{
				if (args[i] instanceof Text)
				{
					args[i] = modifyImpl((Text)args[i], depth + 1);
				}
			}
			// TODO: split mc1.19.3 from mc1.19.4 branch, cuz fallback only works in 1.19.4
			//#if MC >= 11904
			//$$ copied = Text.translatableWithFallback(key, translatableTextContent.getFallback(), args);
			//#else
			copied = Text.translatable(key, args);
			//#endif
		}

		Style style = text.getStyle();

		// hover event
		HoverEvent hoverEvent = style.getHoverEvent();
		if (hoverEvent != null && hoverEvent.getAction() == HoverEvent.Action.SHOW_TEXT)
		{
			Text hovered = hoverEvent.getValue(HoverEvent.Action.SHOW_TEXT);
			if (hovered != null)
			{
				hovered = modifyImpl(hovered, depth + 1);
				style = style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hovered));
			}
		}

		// click event
		ClickEvent clickEvent = style.getClickEvent();
		if (clickEvent != null && clickEvent.getAction() == ClickEvent.Action.RUN_COMMAND)
		{
			String command = "/lmcas " + clickEvent.getValue();
			style = style.withClickEvent(new ClickEvent(clickEvent.getAction(), command));
			LetMeClickAndSendMod.LOGGER.debug("Wrapped message \"{}\"", clickEvent.getValue());
		}

		copied.setStyle(style);

		// children
		for (Text sibling : text.getSiblings())
		{
			copied.append(modifyImpl(sibling, depth + 1));
		}

		return copied;
	}
}
