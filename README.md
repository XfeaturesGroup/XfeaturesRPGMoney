# 💰 XfeaturesRPGMoney

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Spigot](https://img.shields.io/badge/spigot-1.13+-green.svg)
![License](https://img.shields.io/badge/license-MIT-yellow.svg)

## 📝 Описание

**XfeaturesRPGMoney** - это инновационный экономический плагин для Minecraft серверов, который добавляет RPG-элементы в экономику вашего сервера. Плагин предоставляет игрокам разнообразные способы заработка внутриигровой валюты:

- 💀 **Охота на мобов**: получайте деньги за убийство различных существ
- 🎣 **Рыбалка**: зарабатывайте, ловя разные виды рыбы
- ⛏️ **Добыча блоков**: шанс получить деньги при добыче определенных блоков

Плагин полностью интегрируется с Vault API, обеспечивая совместимость с большинством экономических систем.

## ✨ Основные возможности

- **Гибкая система наград**: настраиваемые минимальные и максимальные суммы для каждого источника дохода
- **Множители удачи и добычи**: зачарования Fortune и Looting увеличивают получаемую награду
- **Статистика игроков**: отслеживание заработанных денег и создание рейтинга лучших игроков
- **Визуальные эффекты**: анимированное появление монет в игровом мире
- **Полная настройка сообщений**: все уведомления можно изменить в конфигурации
- **Простое управление**: интуитивно понятные команды для игроков и администраторов

## 📋 Требования

- Spigot/Paper 1.16+
- Vault
- Любой экономический плагин, совместимый с Vault (например, EssentialsX, CMI)

## 🔧 Установка

1. Скачайте последнюю версию плагина из раздела [Releases](https://github.com/XfeaturesGroup/XfeaturesRPGMoney/releases)
2. Поместите JAR-файл в папку `plugins` вашего сервера
3. Перезапустите сервер
4. Настройте конфигурационные файлы по вашему усмотрению
5. Перезагрузите плагин командой `/rpgmoney reload`

## ⚙️ Конфигурация

После первого запуска плагин создаст следующие конфигурационные файлы:

### config.yml
Основные настройки плагина, включая множители для зачарований:

```yaml
# Коэффициент выпадения из мобов из спавнера
spawner-percent: 0.6

# Основные настройки плагина
drop-chance: 0.75 # Шанс выпадения денег из чего либо
show-action-bar-messages: true # Сообщения о подборе монет
player-death-drop-percentage: 0.07 # Коэффициент выпадения из убитого игрока

# Множители для зачарования "Удача"
fortune-multipliers:
  1: 1.25  # Удача I - увеличивает выпадение монет на 25%
  2: 1.5   # Удача II - увеличивает выпадение монет на 50%
  3: 1.75  # Удача III - увеличивает выпадение монет на 75%

# Множители для зачарования "Добыча"
looting-multipliers:
  1: 1.25  # Добыча I - увеличивает выпадение монет на 25%
  2: 1.5   # Добыча II - увеличивает выпадение монет на 50%
  3: 1.75  # Добыча III - увеличивает выпадение монет на 75%
```

### mobs.yml
Настройка наград за убийство мобов:

```yaml
ALLAY: [5, 30]
AXOLOTL: [5, 30]
BAT: [5, 30]
BEE: [5, 30]
BLAZE: [5, 25]
CAMEL: [4, 12]
CAT: [5, 30]
CAVE_SPIDER: [2.5, 20]
CHICKEN: [4, 12]
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

### fish.yml
Настройка наград за рыбалку:

```yaml
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
Настройка наград за добычу блоков:

```yaml
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

### messages.yml
Настройка всех сообщений плагина:

```yaml
pickup: "&eВы подняли %amount% монет!"
```

## 📜 Команды
| Команда | Описание | Права |
|---------|----------|-------|
| `/rpgmoney reload` | Перезагрузить конфигурацию | `xfeatures.rpgmoney.reload` |
| `/rpgmoney stats` | Показать вашу статистику | - |
| `/rpgmoney top [страница]` | Показать топ игроков | `xfeatures.rpgmoney.top` |
| `/rpgmoney info` | Информация о плагине | - |

## 🔒 Права доступа
- xfeatures.rpgmoney.reload - Доступ к команде перезагрузки
- xfeatures.rpgmoney.top - Доступ к просмотру топа игроков
- xfeatures.rpgmoney.admin - Полный доступ ко всем функциям плагина

## 🛠️ API для разработчиков
Плагин предоставляет API для интеграции с другими плагинами:

```java
// Получение экземпляра плагина
XfeaturesRPGMoney plugin = (XfeaturesRPGMoney) Bukkit.getPluginManager().getPlugin("XfeaturesRPGMoney");

// Работа с конфигурацией мобов
MobConfig mobConfig = plugin.mobConfig;
List<Double> zombieReward = mobConfig.getReward("zombie");
double min = zombieReward.get(0);
double max = zombieReward.get(1);

// Работа с API плагина
RPGMoneyAPI api = plugin.getAPI();
// Получить случайное значение в диапазоне
double amount = api.getRandomInRange(10.0, 20.0);
// Сбросить деньги в мире
api.dropMoney(location, amount);
// Проверить, был ли блок размещен игроком
boolean isPlayerPlaced = api.isPlayerPlacedBlock(location);
```

## 📊 Производительность
### Плагин оптимизирован для минимального влияния на производительность сервера:
- Асинхронное сохранение данных
- Оптимизированные алгоритмы расчета наград
- Минимальное использование памяти

## 📝 Планы на будущее
- Более тонкие конфигурации
- Интеграция с PlaceholderAPI
- Графический интерфейс для управления настройками
- Добавление наград за Археологию, Исследования, Создание.

## 🤝 Вклад в проект
### Вклады приветствуются! Если у вас есть идеи по улучшению плагина:
1. Создайте форк репозитория
2. Создайте ветку для вашей функции (git checkout -b feature/amazing-feature)
3. Зафиксируйте изменения (git commit -m 'Add some amazing feature')
4. Отправьте изменения в ваш форк (git push origin feature/amazing-feature)
5. Откройте Pull Request

## 📞 Поддержка
### Если у вас возникли проблемы или вопросы:
- Создайте Issue в репозитории
- Свяжитесь с нами через [Discord](https://discord.gg/KJU4DjGkeH)




<p align="center">
  Сделано с ❤️ командой XfeaturesGroup
</p>
