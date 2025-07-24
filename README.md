# 💰 XfeaturesRPGMoney

![Version](https://img.shields.io/badge/version-1.1.1-blue.svg)
![Minecraft](https://img.shields.io/badge/minecraft-1.13%2B-brightgreen.svg)
![Java](https://img.shields.io/badge/Java-21-orange.svg)
![API](https://img.shields.io/badge/API-available-informational)
![Vault](https://img.shields.io/badge/Vault-required-critical)
![License](https://img.shields.io/badge/license-MIT-yellow.svg)
![Discord](https://img.shields.io/discord/1252242781775335505?color=7289da&label=discord&logo=discord&logoColor=white)
## 📝 Description

**XfeaturesRPGMoney** is an innovative economic plugin for Minecraft servers that adds RPG elements to your server's economy. The plugin provides players with a variety of ways to earn in-game currency:

- 💀 **Mob hunting**: earn money for killing various creatures
- 🎣 **Fishing**: earn money by catching different types of fish
- ⛏️ **Block mining**: get a chance to earn money when mining certain blocks
- 🔍 **Archaeology**: get rewards for mining archaeological treasures

The plugin is fully integrated with the Vault API, ensuring compatibility with most economic systems.

## ✨ Key features

- **Flexible reward system**: customizable minimum and maximum amounts for each source of income
- **Luck and loot multipliers**: Fortune and Looting enchantments increase the reward received
- **Player statistics**: track money earned and create a ranking of the best players
- **Visual effects**: animated appearance of coins in the game world
- **Full message customization**: all notifications can be changed in the configuration
- **Easy management**: intuitive commands for players and administrators

## 📋 Requirements

- Spigot/Paper 1.13+
- Vault
- Any economic plugin compatible with Vault (e.g., EssentialsX, CMI)

## 🔧 Installation

1. Download the latest version of the plugin from the [Releases](https://github.com/XfeaturesGroup/XfeaturesRPGMoney/releases) section
2. Place the JAR file in the `plugins` folder of your server
3. Restart the server
4. Configure the configuration files as you see fit
5. Reload the plugin with the command `/rpgmoney reload`

<details>
<summary>

## 🌍 Supported Languages

</summary>

XfeaturesRPGMoney supports multiple languages for users from around the world. You can change the plugin language using the command `/rpgmoney language <language>`.

### Available Languages:

- English (en)
- Russian (ru)
- German (de)
- Polish (pl)
- Italian (it)
- Portuguese (pt)
- Turkish (tr)
- French (fr)
- Spanish (es)
- Lithuanian (lt)
- Latvian (lv)
- Estonian (et)
- Arabic (ar)
- Hindi (hi)
- Chinese (Simplified) (cn)

### Adding a New Language

If you want to add a new language or improve an existing translation:

1. Copy the `messages-en.yml` file from the `resources/messages/` folder
2. Rename it to `messages-XX.yml`, where XX is your language code
3. Translate all strings to your language
4. Submit the translation via Pull Request or contact the developers

We always welcome new translations and improvements to existing ones!
</details>

<details>
<summary>

## ⚙️ Configuration

</summary>

After the first launch, the plugin will create the following configuration files:

### config.yml
Basic plugin settings, including multipliers for enchantments:

```yaml
# All available languages can be found here:
# https://github.com/XfeaturesGroup/XfeaturesRPGMoney/tree/master/examples/messages
# Language settings for the plugin
language: "en"

# Coin drop rate from mobs spawned by spawners
spawner-multiplier: 0.6

# Main plugin settings
drop-chance: 0.75 # Probability of coins dropping when mining blocks and killing mobs
show-action-bar-messages: true # Show messages when picking up coins in the Action Bar
player-death-drop-percentage: 0.07 # Coin drop rate from killed players
max-money-drop: 1000.0 # Maximum amount of money in a single coin

# Settings for combining nearby drops
combine-nearby-drops: true
combine-radius: 1.5

# Messages about multipliers
show-fortune-multiplier-messages: false
show-looting-multiplier-messages: false

# Multipliers for the “Fortune” enchantment
fortune-multipliers:
  1: 1.25  # Fortune I - increases coin drop rate by 25%
  2: 1.5   # Fortune II - increases coin drop rate by 50%
  3: 1.75  # Fortune III - increases coin drop rate by 75%

# Multipliers for “Looting” enchantments
looting-multipliers:
  1: 1.25  # Looting I - increases coin drop rate by 25%
  2: 1.5   # Looting II - increases coin drop rate by 50%
  3: 1.75  # Looting III - increases coin drop rate by 75%
```

### mobs.yml
Setting killing mobs rewards:

```yaml
# ENTITY: [min, max]
ALLAY: [5, 30]
ARMADILLO: [4, 12]
AXOLOTL: [5, 30]
BAT: [5, 30]
BEE: [5, 30]
BLAZE: [5, 25]
BREEZE: [15, 30]
BOGGED: [10, 20]
CAMEL: [4, 12]
CAT: [5, 30]
CAVE_SPIDER: [2.5, 20]
CHICKEN: [4, 12]
COPPER_GOLEM: [15, 30]
COD: [4, 12]
COW: [4, 12]
CREEPER: [10, 15]
DOLPHIN: [4, 22]
DONKEY: [4, 12]
DROWNED: [10, 20]
ELDER_GUARDIAN: [50, 125]
ENDER_DRAGON: [100, 250]
ENDERMAN: [3, 20]
ENDERMITE: [10, 20]
EVOKER: [50, 100]
FOX: [4, 12]
FROG: [5, 12]
GHAST: [20, 35]
GLOW_SQUID: [4, 20]
GOAT: [4, 20]
GUARDIAN: [10, 25]
HOGLIN: [10, 25]
HORSE: [4, 12]
HUSK: [5, 15]
IRON_GOLEM: [25, 40]
LLAMA: [4, 12]
MAGMA_CUBE: [2, 7]
MOOSHROOM: [4, 14]
MULE: [4, 12]
OCELOT: [4, 12]
PANDA: [4, 12]
PARROT: [4, 12]
PHANTOM: [15, 20]
PIG: [4, 12]
PIGLIN: [4, 15]
PIGLIN_BRUTE: [4, 15]
PILLAGER: [7, 15]
POLAR_BEAR: [4, 12]
PUFFERFISH: [4, 12]
RABBIT: [4, 20]
RAVAGER: [50, 70]
SALMON: [4, 20]
SHEEP: [4, 20]
SHULKER: [10, 20]
SILVERFISH: [10, 20]
SKELETON: [10, 15]
SKELETON_HORSE: [10, 15]
SLIME: [4, 6]
SNIFFER: [10, 25]
SNOW_GOLEM: [10, 15]
SPIDER: [10, 15]
SQUID: [4, 12]
STRAY: [10, 15]
STRIDER: [10, 25]
TADPOLE: [10, 25]
TRADER_LLAMA: [10, 25]
TROPICAL_FISH: [10, 15]
TURTLE: [10, 15]
VEX: [10, 25]
VILLAGER: [10, 55]
VINDICATOR: [10, 25]
WANDERING_TRADER: [10, 25]
WARDEN: [10, 55]
WITCH: [10, 25]
WITHER: [10, 55]
WITHER_SKELETON: [10, 25]
WOLF: [4, 12]
ZOGLIN: [10, 25]
ZOMBIE: [10, 15]
ZOMBIE_HORSE: [10, 15]
ZOMBIE_VILLAGER: [10, 15]
ZOMBIFIED_PIGLIN: [5, 12]
```

### fishes.yml
Setting fishing rewards:

```yaml
# LOOT: [min, max]
COD: [10, 30]
SALMON: [10, 30]
PUFFERFISH: [15, 25]
TROPICAL_FISH: [10, 25]
ENCHANTED_BOOK: [25, 45]
LEATHER: [15, 35]
TRIPWIRE_HOOK: [15, 35]
ROTTEN_FLESH: [15, 35]
FISHING_ROD: [15, 35]
NAME_TAG: [15, 55]
NAUTILUS_SHELL: [55, 200]
SADDLE: [15, 35]
```

### blocks.yml
Setting block mining rewards:

```yaml
# BLOCK: [min, max]
CROPS: [3.5, 21]
WHEAT: [3.5, 21]
POTATOES: [3.5, 8.75]
CARROTS: [3.5, 8.75]
BEETROOTS: [7, 42]
NETHER_WART: [5.25, 35]
MELON: [8.75, 52.5]
PUMPKIN: [8.75, 52.5]
CACTUS: [1.75, 8.75]
SUGAR_CANE: [1.75, 35]
GLOW_BERRIES: [1.75, 35]
SWEET_BERRIES: [1.75, 35]
COCOA_BEANS: [1.75, 35]
COAL_ORE: [5.25, 10.5]
DEEPSLATE_COAL_ORE: [8.75, 26.25]
IRON_ORE: [8.75, 17.5]
DEEPSLATE_IRON_ORE: [14, 35]
COPPER_ORE: [12.25, 22.75]
DEEPSLATE_COPPER_ORE: [8.75, 21]
GOLD_ORE: [17.5, 21]
DEEPSLATE_GOLD_ORE: [17.5, 35]
NETHER_GOLD_ORE: [8.75, 14]
REDSTONE_ORE: [8.75, 21]
DEEPSLATE_REDSTONE_ORE: [8.75, 22.75]
LAPIS_ORE: [8.75, 14]
DEEPSLATE_LAPIS_ORE: [8.75, 22.75]
DIAMOND_ORE: [17.5, 70]
DEEPSLATE_DIAMOND_ORE: [70, 140]
EMERALD_ORE: [52.5, 105]
DEEPSLATE_EMERALD_ORE: [52.5, 105]
NETHER_QUARTZ_ORE: [8.75, 14]
ANCIENT_DEBRIS: [87.5, 262.5]
```
### archeology.yml
Setting archaeology rewards:

```yaml
# DROP: [min, max]
BROWN_CANDLE: [25, 40]
GREEN_CANDLE: [25, 40]
PURPLE_CANDLE: [25, 40]
RED_CANDLE: [25, 40]
LIGHT_BLUE_DYE: [15, 35]
ORANGE_DYE: [15, 35]
BLUE_DYE: [15, 35]
YELLOW_DYE: [15, 35]
WHITE_DYE: [15, 35]
BRICK: [10, 15]
EMERALD: [50, 75]
WHEAT: [20, 30]
WOODEN_HOE: [15, 20]
BLUE_STAINED_GLASS_PANE: [20, 35]
LIGHT_BLUE_STAINED_GLASS_PANE: [20, 35]
MAGENTA_STAINED_GLASS_PANE: [20, 35]
PINK_STAINED_GLASS_PANE: [20, 35]
PURPLE_STAINED_GLASS_PANE: [20, 35]
RED_STAINED_GLASS_PANE: [20, 35]
YELLOW_STAINED_GLASS_PANE: [20, 35]
BEETROOT_SEEDS: [25, 30]
WHEAT_SEEDS: [25, 30]
OAK_HANGING_SIGN: [25, 30]
SPRUCE_HANGING_SIGN: [25, 30]
DEAD_BUSH: [30, 35]
FLOWER_POT: [15, 30]
LEAD: [15, 30]
STRING: [15, 30]
GOLD_NUGGET: [35, 45]
COAL: [30, 35]
MUSIC_DISC_RELIC: [150, 175]
TRIM_PATTERN: [100, 125]
```

### messages.yml
Configuring all plugin messages:

```yaml
prefix: "&c[XfeaturesRPGMoney]&r"

# Command messages
no-permission: "&cYou don't have permission to use this command."
player-only: "&cThis command is only available for players."
config-reloaded: "&aConfiguration successfully reloaded."
invalid-page-format: "&cInvalid page format. Please use a number."
no-data-for-page: "&cThere is no data for this page."

# Stats messages
stats-header: "&7===== &cYour Statistics &7====="
stats-collected: "&eMoney collected: &6%amount%"

# Top players messages
top-header: "&7===== &cPlayer Rankings (Page %page%) &7====="
top-player-entry: "&7#%rank% &c%player% &7- &7%amount% money"
top-next-page: "&7Use &c/rpgmoney top %page% &7for the next page"

# Plugin info messages
info-header: "&7===== &cPlugin Information &7====="
info-version: "&cVersion: &7%version%"
info-author: "&cAuthors: &7%authors%"
info-fortune-header: "&cFortune Multipliers:"
info-fortune-entry: "&cLevel %level%: &7+%percent% money"
info-looting-header: "&cLooting Multipliers:"
info-looting-entry: "&cLevel %level%: &7+%percent% money"

# Help messages
help-header: "&7===== &cXfeaturesRPGMoney Help &7====="
help-reload: "&c/rpgmoney reload &7- Reload configuration"
help-stats: "&c/rpgmoney stats &7- Display your statistics"
help-top: "&c/rpgmoney top [page] &7- Display player rankings"
help-info: "&c/rpgmoney info &7- Plugin information"
help-language: "&c/rpgmoney language <language> &7- Change plugin language"

# Money collection messages
currency-name: Money
pickup: "&eYou picked up %amount% money!"
currency-pickup: "&eYou picked up %amount% money!"
money-item-name: "&e%amount% money"
money-drop: "&eYou dropped %amount% money!"
fishing-reward: "&eYou fished %amount% money!"
mining-reward: "&eYou mined %amount% money!"
entity-kill-reward: "&eYou received %amount% money for this kill!"
player-kill-reward: "&eYou received %amount% money for killing a player!"
player-death-drop: "&cYou lost %amount% money by dying!"
currency-format: "%amount% money"
currency-singular: money
currency-plural: money

# Language messages
language-usage: "&cUsage: /rpgmoney language <language>"
language-not-found: "&cLanguage %language% not found."
language-changed: "&aLanguage changed to %language%."
```
</details>

## 📜 Commands
| Command | Description | Permissions |
|---------|----------|-------|
| `/rpgmoney reload` | Reload configuration | `xfeatures.rpgmoney.reload` |
| `/rpgmoney stats` | Show your stats | - |
| `/rpgmoney top [page]` | Show top players | `xfeatures.rpgmoney.top` |
| `/rpgmoney info` | Plugin information | - |
| `/rpgmoney language <language>` | Change plugin language | `xfeatures.rpgmoney.language` |

## 🔒 Permissions
- xfeatures.rpgmoney.reload - Access to the reload command
- xfeatures.rpgmoney.top - Access to view the top players
- xfeatures.rpgmoney.admin - Full access to all plugin features
- xfeatures.rpgmoney.language - Access to change the plugin language

## 🛠️ API for developers
The plugin provides an API for integration with other plugins:

```java
// Getting a plugin instance
XfeaturesRPGMoney plugin = (XfeaturesRPGMoney) Bukkit.getPluginManager().getPlugin("XfeaturesRPGMoney");

// Working with mob configuration
MobConfig mobConfig = plugin.mobConfig;
List<Double> zombieReward = mobConfig.getReward("zombie");
double min = zombieReward.get(0);
double max = zombieReward.get(1);

// Working with the plugin API
RPGMoneyAPI api = plugin.getAPI();
// Get a random value in the range
double amount = api.getRandomInRange(10.0, 20.0);
// Drop money in the world
api.dropMoney(location, amount);
// Check if the block was placed by the player
boolean isPlayerPlaced = api.isPlayerPlacedBlock(location);
```

## 📊 Performance
### The plugin is optimized for minimal impact on server performance:
- Asynchronous data saving
- Optimized reward calculation algorithms
- Minimal memory usage

## 📝 Future plans
- ✅ More refined configurations **(Implemented)**
- ✅ Support for more languages **(Implemented)**
- Integration with PlaceholderAPI
- Graphical interface for managing settings
- Adding rewards for Archaeology, Research, Creation

## 🤝 Contributing to the project
### Contributions are welcome! If you have ideas for improving the plugin:
1. Fork the repository
2. Create a branch for your feature (git checkout -b feature/amazing-feature)
3. Commit your changes (git commit -m ‘Add some amazing feature’)
4. Push the changes to your fork (git push origin feature/amazing-feature)
5. Open a Pull Request

## 📊 Statistics
### The plugin collects anonymous statistics via bStats to improve functionality:
You can view the plugin statistics on the [bStats](https://bstats.org/plugin/bukkit/XfeaturesRPGMoney/26636) page.

![bStats](https://img.shields.io/bstats/servers/26636?label=servers)
![bStats](https://img.shields.io/bstats/players/26636?label=players)

### Data collected:
- Total amount of money in the economy
- Number of players with money records
- Use of messages in the action bar
- Use of Fortune and Looting multipliers
- Number of players and servers
- Online mode status
- Server, plugin, and Java versions

### Disabling statistics
If you want to disable statistics collection, you can do so in the `plugins/bStats/config.yml` file by setting `enabled: false`. Disabling statistics does not affect the functionality of the plugin.

## 📞 Support
### If you encounter any problems or have any questions:
- Create an issue in the repository
- Contact us via [Discord](https://discord.gg/KJU4DjGkeH)

![Downloads](https://img.shields.io/github/downloads/XfeaturesGroup/XfeaturesRPGMoney/total?color=orange)
![Issues](https://img.shields.io/github/issues/XfeaturesGroup/XfeaturesRPGMoney?color=red)
![Last Commit](https://img.shields.io/github/last-commit/XfeaturesGroup/XfeaturesRPGMoney?color=blueviolet)

<p align="center">
  Made with ❤️ by the XfeaturesGroup team
</p>
