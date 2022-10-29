## Let me click and send

A simple Minecraft client-side mod, that reverts the `run_command` click event restriction introduced in 1.19.1-rc1,
so non-command messages can be sent freely via `run_command` click event again

### Example

Let's run the following command, and then click the shown text

```
/tellraw @a {"text":"click me to send \"hi\"","clickEvent":{"action":"run_command","value":"hi"}}
```

In vanilla 1.19.1+, after clicking, you are not able to say anything since `hi` is not a valid command (not starts with `/`)

With this mod, after clicking, you will automatically send a `hi` message to the server, which is the same behavior as the previous Minecraft versions

### Requirements

It's a client-side only mod

- Fabric loader
- Minecraft >= 1.19.1
