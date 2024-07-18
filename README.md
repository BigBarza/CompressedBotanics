## Compressed Engineering

This mod is an addon for Botania, implementing features needed for the Compression modpack.

## Implemented features:
### Floral Entropy
You can configure datapackable recipes to make mana generating flowers decay after a while, dropping some items.

USAGE:

```
{
  "type": "compressedbotanics:floral_entropy",
  "flower": "botania:hydroangeas",  # The flower that should decay, catches floating variants as well.
  "block" : "minecraft:soul_torch", # What block should the flower turn into (Optional)
  "result": [   # The items to drop (Optional)
    {
      "item": "minecraft:snowball",
      "count": 10,
      "chance": 0.5 # 10 rolls at 50% chance
    },
    {
      "item": "minecraft:ice",
      "count": 3,
      "chance": 0.2,
      "allOrNothing": true # 20% to get 3 ice, otherwise get nothing.
    }
  ],
  "minTalliedMana": 10000 # Must generate at least this much mana before it can decay (Optional)
  "minDecayTicks": 60000, # Even with enough mana generated, won't decay before this many ticks have passed since placing (Optional)
  "maxDecayTicks": 100000 # Can randomly decay before this many ticks have passed if conditions are met. Will always decay after this treshold.
}
```

Curseforge link: [TBA]

Compression modpack: https://www.curseforge.com/minecraft/modpacks/compression

Compression Discord: https://discord.gg/dz7UJ7sDmU

I do not plan to support other versions or modloaders beyond the needs of the modpack this is made for. Feel free to open pull requests though.
