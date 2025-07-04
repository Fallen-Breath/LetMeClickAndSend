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

package me.fallenbreath.letmeclickandsend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//#if FORGE
//$$ @net.minecraftforge.fml.common.Mod("letmeclickandsend")
//#elseif NEOFORGE
//$$ @net.neoforged.fml.common.Mod("letmeclickandsend")
//#endif
public class LetMeClickAndSendMod
		//#if FABRIC
		implements net.fabricmc.api.ModInitializer
		//#endif
{
	public static final String MOD_ID = "letmeclickandsend";

	public static final Logger LOGGER = LogManager.getLogger();

	//#if FABRIC
	@Override public void onInitialize()
	//#elseif FORGE_LIKE
	//$$ public LetMeClickAndSendMod()
	//#endif
	{
		LOGGER.info("Let me click and send!");

		//#if MC >= 12105
		//$$ LmcasConfig.getInstance().load();
		//#endif
	}
}
