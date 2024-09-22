## Let me click and send

[![MC Versions](https://cf.way2muchnoise.eu/versions/For%20MC_694888_all.svg)](https://www.curseforge.com/minecraft/mc-mods/let-me-click-and-send)
[![CurseForge](https://cf.way2muchnoise.eu/full_694888_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/let-me-click-and-send)
[![Modrinth](https://img.shields.io/modrinth/dt/pGbwwB5d?label=Modrinth%20Downloads)](https://modrinth.com/mod/letmeclickandsend)

A simple Minecraft client-side mod, that reverts the `run_command` click event restriction introduced in 1.19.1-rc1,
so non-command messages can be sent freely via `run_command` click event again

If you want a solution without user awareness, here's a server-side only mod that does the same thing:
[LetMeClickAndSendForServer](https://github.com/Fallen-Breath/LetMeClickAndSendForServer)

| Mod                                                                                       | Side   | Advantage                                 | Disadvantage                                  |
|-------------------------------------------------------------------------------------------|--------|-------------------------------------------|-----------------------------------------------|
| [LetMeClickAndSend](https://github.com/Fallen-Breath/LetMeClickAndSend)                   | client | Exactly the same behavior as pre mc1.19.1 | Needs to be installed on all players' clients |        
| [LetMeClickAndSendForServer](https://github.com/Fallen-Breath/LetMeClickAndSendForServer) | server | No need to install on client              | Reduces maximum chat message length by 7      |     

### Example

Let's run the following command, and then click the shown text

```
/tellraw @a {"text":"click me to send \"hi\"","clickEvent":{"action":"run_command","value":"hi"}}
```

In vanilla 1.19.1+, after clicking, you are not able to say anything since `hi` is not a valid command (not starts with `/`)

With this mod, after clicking, you will automatically send a `hi` message to the server, which is the same behavior as the previous Minecraft versions

### Requirements

It's a client-side only mod. It requires 0 extra dependency

Requirements:

- Minecraft >= 1.19.1
