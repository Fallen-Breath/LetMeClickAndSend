## Let me click and send

[![MC Versions](https://cf.way2muchnoise.eu/versions/For%20MC_694888_all.svg)](https://www.curseforge.com/minecraft/mc-mods/let-me-click-and-send)
[![CurseForge](https://cf.way2muchnoise.eu/full_694888_downloads.svg)](https://legacy.curseforge.com/minecraft/mc-mods/let-me-click-and-send)
[![Modrinth](https://img.shields.io/modrinth/dt/pGbwwB5d?label=Modrinth%20Downloads)](https://modrinth.com/mod/let-me-click-and-send)

A simple Minecraft client-side mod, that reverts the `run_command` click event restriction introduced in 1.19.1-rc1,
so non-command messages can be sent freely as a chat message via `run_command` click event again

If you want a solution without user awareness, here's a server-side only mod that does the same thing:
[LetMeClickAndSendForServer](https://github.com/Fallen-Breath/LetMeClickAndSendForServer)

| Mod                                                                                       | Side   | Advantage                                 | Disadvantage                                  |
|-------------------------------------------------------------------------------------------|--------|-------------------------------------------|-----------------------------------------------|
| [LetMeClickAndSend](https://github.com/Fallen-Breath/LetMeClickAndSend)                   | client | Exactly the same behavior as pre mc1.19.1 | Needs to be installed on all players' clients |        
| [LetMeClickAndSendForServer](https://github.com/Fallen-Breath/LetMeClickAndSendForServer) | server | No need to install on client              | Reduces maximum chat message length by 7      |     

### Example

Let's run the following command, and then click the shown text

```bash
# Minecraft [1.7, 1.21.5)
/tellraw @a {"text":"click me to send \"hi\"","clickEvent":{"action":"run_command","value":"hi"}}
```

In vanilla 1.19.1 ~ 1.21.4, after clicking, you are not able to say anything since `hi` is not a valid command (not starts with `/`)

With this mod, after clicking, you will automatically send a `hi` chat message to the server, which is the same behavior as the previous Minecraft versions

### MC 1.21.5+

Since MC 1.21.5, the `run_command` behavior has changed a lot

First is the change in command syntax, which has little impact:

```bash
# Minecraft [1.21.5, ~)
/tellraw @a {"text":"click me to send \"hi\"","click_event":{"action":"run_command","command":"hi"}}
```

Next is the change in behavior, which has a greater impact:

- The `command` value is always valid, regardless of whether it starts with a `/` or not
- The client will strip the `/` prefix from the `command` value and send the remaining string as a command

It's no longer possible to correctly distinguish between "a run_command for sending chat message" and "a run_command for sending command"

As a workaround, LetMeClickAndSendForServer for MC >= 1.21.5 will only replace certain `command` with the `/lmcas` command.
By default, only `command` value starting with `!!`, which is a commonly-used command prefix
in [MCDReforged](https://github.com/MCDReforged/MCDReforged) plugin ecosystem, will be replaced

A config file located at `./config/letmeclickandsendforserver/config.json` is added for customizing the replacing behavior

```json
{
    "sendChatPattern": "!!.*"
}
```

The `sendChatPattern` should be a valid regex pattern. All `command` values that fully match the pattern will be as chat messages

To test with the default configuration, you can use:

```bash
/tellraw @a {"text":"click me to send \"!!MCDR\"","click_event":{"action":"run_command","command":"!!MCDR"}}
```

### Requirements

It's a client-side only mod. It requires 0 extra dependency

Requirements:

- Minecraft >= 1.19.1
