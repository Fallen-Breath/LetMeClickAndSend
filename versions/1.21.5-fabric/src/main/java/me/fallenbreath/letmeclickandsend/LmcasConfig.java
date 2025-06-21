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

package me.fallenbreath.letmeclickandsend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class LmcasConfig
{
	private static final LmcasConfig INSTANCE = new LmcasConfig();

	private Pattern sendChatPattern;

	private static class ConfigData
	{
		public String sendChatPattern = "!!.*";
	}

	public static LmcasConfig getInstance()
	{
		return INSTANCE;
	}

	public void load()
	{
		Path configDir = Paths.get("config", LetMeClickAndSendMod.MOD_ID);
		Path configFile = configDir.resolve("config.json");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		try
		{
			Files.createDirectories(configDir);

			if (!Files.exists(configFile))
			{
				try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("assets/letmeclickandsend/default_config.json"))
				{
					if (inputStream == null)
					{
						throw new IOException("Default config file not found in resources");
					}
					Files.copy(inputStream, configFile);
				}
			}

			ConfigData configData;
			try (Reader reader = Files.newBufferedReader(configFile, StandardCharsets.UTF_8))
			{
				configData = gson.fromJson(reader, ConfigData.class);
			}
			this.readConfigData(configData);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Failed to load or create config file: " + configFile, e);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Invalid config format: " + e.getMessage(), e);
		}
	}

	private void readConfigData(ConfigData configData)
	{
		LetMeClickAndSendMod.LOGGER.info("LMCAS sendChatPattern: `{}`", configData.sendChatPattern);
		this.sendChatPattern = Pattern.compile(configData.sendChatPattern, Pattern.DOTALL);
	}

	public Pattern getSendChatPattern()
	{
		return this.sendChatPattern;
	}
}
