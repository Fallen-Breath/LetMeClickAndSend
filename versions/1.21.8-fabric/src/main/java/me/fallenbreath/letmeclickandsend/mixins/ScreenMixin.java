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
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin
{
	@Inject(
			method = "handleRunCommand",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void justSendTheChat$letmeclickandsend(ClientPlayerEntity player, String command, Screen screenAfterRun, CallbackInfo ci)
	{
		if (LmcasConfig.getInstance().getSendChatPattern().matcher(command).matches())
		{
			player.networkHandler.sendChatMessage(command);
			ci.cancel();
		}
	}
}
