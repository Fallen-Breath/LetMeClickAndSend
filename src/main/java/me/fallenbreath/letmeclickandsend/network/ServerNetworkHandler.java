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

package me.fallenbreath.letmeclickandsend.network;

import com.google.common.collect.Sets;
import me.fallenbreath.letmeclickandsend.LetMeClickAndSendMod;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Set;
import java.util.UUID;

public class ServerNetworkHandler
{
	private static final Set<UUID> lmcasPlayers = Sets.newHashSet();

	// NOTE: will be invoked on network thread
	public static void handlePacket(MinecraftServer server, ServerPlayNetworkHandler handler, CustomPayloadC2SPacket packet)
	{
		if (packet.getChannel().equals(PacketIds.HI))
		{
			NbtCompound nbt = packet.getData().readNbt();
			server.execute(() -> onPlayerHi(handler.player));
		}
	}

	private static void onPlayerHi(ServerPlayerEntity player)
	{
		LetMeClickAndSendMod.LOGGER.debug("Received hi from player {} ({})", player.getName().getString(), player.getUuid());
		lmcasPlayers.add(player.getUuid());
	}

	public static void handlePlayerLeft(ServerPlayerEntity player)
	{
		LetMeClickAndSendMod.LOGGER.debug("Removing player {} ({})", player.getName().getString(), player.getUuid());
		lmcasPlayers.remove(player.getUuid());
	}

	public static boolean doesPlayerHasMod(ServerPlayerEntity player)
	{
		return lmcasPlayers.contains(player.getUuid());
	}
}
