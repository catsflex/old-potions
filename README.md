# Notice

**I've started doing Minecraft commissions in my [Discord server](https://discord.gg/Ce6Vty6khA)!**

# Old Potions

This mod brings back the potion visuals from older Minecraft versions.

## Features

- **Global Palette**:
  Choose one of three different eras for every effect: `1.19.3 and below`, `1.19.4`, or `1.20 and above` (vanilla).
  For example, selecting `1.19.3 and below` makes your potion visuals match those from Minecraft 1.19.3.
- **Per-Effect Overrides**:
  Override the palette individually for any effect listed in the config menu.
- **Enchantment Glint**:
  Toggle the enchantment glint for any potion that has an effect applied, just like it was in Minecraft 1.19.3 and below.

## Technical Problems

Effect particle colors are evaluated server-side and sent to the client as a final color, so the client never sees which effects resulted in that color.
This mod maps these known colors back to the palette colors you've chosen in the config menu.

However, if a server evaluates the color from a combination of effects in a single potion that doesn't exist in the vanilla (e.g., Speed + Strength potion), it would be absent in the registry and cannot be changed even with mods.
See the table below with all the aspects that this mod covers.

| Color Aspect                       | Single-Effect Potions | Vanilla Multi-Effect Potions | Non-Vanilla Multi-Effect Potions |
|------------------------------------|:---------------------:|:----------------------------:|:--------------------------------:|
| Potion Color                       |          ✅           |              ✅              |                ✅                |
| Splash Potion Smash Particle Color |          ✅           |              ✅              |                ❌                |
| Area Effect Cloud Particle Color   |          ✅           |              ✅              |                ❌                |
| Tipped Arrow Color                 |          ✅           |              ✅              |                ✅                |
| Tipped Arrow Particle Color        |          ✅           |              ✅              |                ❌                |

## Credit

The color data used to restore the old potion colors is sourced from [the official Minecraft Wiki comparison table](https://minecraft.wiki/w/Effect_colors/Java_Edition_potion_color_changes_in_1.19.4).
