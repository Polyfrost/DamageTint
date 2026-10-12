# DamageTint

[![Modrinth downloads](https://img.shields.io/modrinth/dt/6zatK8W5?style=flat&logo=modrinth&label=Modrinth&color=1bd96a)](https://modrinth.com/mod/damagetint)
[![CurseForge downloads](https://img.shields.io/curseforge/dt/1147781?style=flat&logo=curseforge&label=CurseForge&color=f16436)](https://www.curseforge.com/minecraft/mc-mods/damagetint)

DamageTint recolors the flash that mobs and players show when they take damage. You pick the color and how strong it is.

## Features

- Pick the tint color and its opacity. The default is red at 30% opacity.
- Pick a chroma color to make the tint cycle through hues.
- Turn on "Fade Out Damage Tint" to fade the tint out at the end of the flash. You set the fade length between 1 and 10 ticks. This option is off by default.
- Turn on "Fade Out Dead Entities" to fade the tint on a dying entity too. This option is off by default and needs the fade out option.
- Turn on "Separate Colors Per Damage Type" to give melee, mace, ranged, explosion and magic damage and critical hits their own colors. This option is off by default. Critical hits start as gold and the other types start as red.
- The tint also applies to the ender dragon.

## Damage types on servers

Your game learns the damage type from the server. Some servers do not report it correctly, so a hit can show the wrong color or the main tint color.

On 1.8.9 the server does not send a damage type. There you can only set a color for critical hits and a color for hits on a player who is blocking with a sword.

DamageTint treats a hit as critical when the server sends the critical hit animation within one tick of the hit.

## Supported versions

- Minecraft 1.8.9 on Ornithe.
- Minecraft 1.21.1, 1.21.4, 1.21.5, 1.21.8, 1.21.10, 1.21.11, 26.1, 26.2 and 26.3 on Fabric.

DamageTint needs [OneConfig](https://modrinth.com/mod/oneconfig) and Fabric Language Kotlin.

## Support

- Report bugs and ask questions on the [Polyfrost Discord](https://polyfrost.org/discord).
- Check [Polyfrost Status](https://status.polyfrost.org/) if a Polyfrost service is down.

## Building

```sh
./gradlew ":26.3:build"
```

Replace `26.3` with any supported Minecraft version. The build needs JDK 21 and JDK 25 installed.
