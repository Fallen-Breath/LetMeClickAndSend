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

package me.fallenbreath.letmeclickandsend.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Screen.class)
public abstract class ScreenMixin
{
	@Shadow
	@Nullable
	protected MinecraftClient client;

	@SuppressWarnings("ConstantConditions")
	@Redirect(
			method = "handleTextClick",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "stringValue=/"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V",
					remap = false,
					ordinal = 1
			),
			require = 1,
			remap = true
	)
	private void justSendTheChat$letmeclickandsend(Logger logger, String loggingMessage, Object clickEventContent)
	{
		// message.charAt(0) != '/'
		String message = (String)clickEventContent;

		// reference: net.minecraft.client.gui.screen.ChatScreen.sendMessage
		// for how to send a chat message

		//#if MC >= 11903
		//$$ this.client.player.networkHandler.sendChatMessage(message);
		//#else
		this.client.player.sendChatMessage(message, null);
		//#endif
	}
}
