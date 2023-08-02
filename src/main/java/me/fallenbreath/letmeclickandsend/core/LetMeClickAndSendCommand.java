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

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.fallenbreath.letmeclickandsend.mixins.core.server.MinecraftServerAccessor;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class LetMeClickAndSendCommand
{
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher)
	{
		dispatcher.register(literal("lmcas").
				then(argument("message", greedyString()).
						executes(c -> sendChatMessage(c.getSource(), getString(c, "message")))
				)
		);
	}

	private static int sendChatMessage(ServerCommandSource source, String message) throws CommandSyntaxException
	{
		// ref: net.minecraft.server.network.ServerPlayNetworkHandler.onChatMessage

		ServerPlayerEntity player = source.getPlayerOrThrow();
		Text text = Text.translatable("chat.type.text", player.getDisplayName(), message);

		MinecraftServerAccessor.getLogger().info(text.getString());
		for (ServerPlayerEntity serverPlayerEntity : source.getServer().getPlayerManager().getPlayerList())
		{
			serverPlayerEntity.sendMessage(text);
		}

		return 1;
	}
}
