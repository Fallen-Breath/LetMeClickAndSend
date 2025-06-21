/*
 * This file is part of the Let Me Click And Send project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

import me.fallenbreath.letmeclickandsend.LmcasConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Screen.class)
public abstract class ScreenMixin
{
	@Shadow
	@Nullable
	protected MinecraftClient client;

	@Unique
	private boolean cancelThisCommandSendSinceChatWasSent = false;

	@ModifyVariable(
			method = "handleTextClick",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/text/ClickEvent$RunCommand;command()Ljava/lang/String;"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Ljava/lang/String;startsWith(Ljava/lang/String;)Z",
					args = "log=true"
			),
			ordinal = 1,
			require = 1,
			allow = 1
	)
	private String justSendTheChat$letmeclickandsend(String command)
	{
		this.cancelThisCommandSendSinceChatWasSent = false;
		if (this.client != null && this.client.player != null)
		{
			if (LmcasConfig.getInstance().getSendChatPattern().matcher(command).matches())
			{
				this.client.player.networkHandler.sendChatMessage(command);
				this.cancelThisCommandSendSinceChatWasSent = true;
			}
		}
		return command;
	}

	@Redirect(
			method = "handleTextClick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendCommand(Ljava/lang/String;)Z"
			),
			require = 1,
			allow = 1
	)
	private boolean skipIfChatWasSent$letmeclickandsend(ClientPlayNetworkHandler instance, String command)
	{
		if (this.cancelThisCommandSendSinceChatWasSent)
		{
			this.cancelThisCommandSendSinceChatWasSent = false;
			return true;
		}

		// vanilla
		return instance.sendCommand(command);
	}
}
