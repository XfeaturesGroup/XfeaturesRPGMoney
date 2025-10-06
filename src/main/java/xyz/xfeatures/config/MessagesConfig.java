package xyz.xfeatures.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.xfeatures.XfeaturesRPGMoney;
import xyz.xfeatures.util.CurrencyFormatter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessagesConfig {
    private final XfeaturesRPGMoney plugin;
    private FileConfiguration config;
    private File configFile;
    private final Map<String, String> cachedMessages = new HashMap<>();
    private final Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private String currentLanguage;

    public MessagesConfig(XfeaturesRPGMoney plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        currentLanguage = plugin.mainConfig.getLanguage();

        File messagesDir = new File(plugin.getDataFolder(), "messages");
        if (!messagesDir.exists()) {
            messagesDir.mkdirs();
        }

        String messagesFileName = "messages-" + currentLanguage + ".yml";
        configFile = new File(messagesDir, messagesFileName);

        String resourcePath = "messages/" + messagesFileName;

        if (!configFile.exists()) {
            if (plugin.getResource(resourcePath) != null) {
                plugin.saveResource(resourcePath, false);
            } else {
                plugin.getLogger().warning("Language file for '" + currentLanguage + "' not found. Using English as fallback.");
                currentLanguage = "en";
                messagesFileName = "messages-en.yml";
                configFile = new File(messagesDir, messagesFileName);
                resourcePath = "messages/" + messagesFileName;

                if (!configFile.exists() && plugin.getResource(resourcePath) != null) {
                    plugin.saveResource(resourcePath, false);
                }
            }
        }

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                plugin.getLogger().warning("Created empty messages file: " + configFile.getName());
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create messages file: " + e.getMessage());
            }
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        InputStream defaultConfigStream = plugin.getResource("messages/messages-en.yml");
        if (defaultConfigStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defaultConfigStream, StandardCharsets.UTF_8));

            boolean needsSave = false;
            for (String key : defaultConfig.getKeys(true)) {
                if (!defaultConfig.isConfigurationSection(key) && !config.contains(key)) {
                    config.set(key, defaultConfig.get(key));
                    needsSave = true;
                }
            }

            if (needsSave) {
                try {
                    config.save(configFile);
                } catch (IOException e) {
                    plugin.getLogger().severe("Could not save default messages to " + configFile.getName());
                    e.printStackTrace();
                }
            }
        }

        cachedMessages.clear();
        for (String key : config.getKeys(true)) {
            if (config.isString(key)) {
                cachedMessages.put(key, config.getString(key));
            }
        }
    }

    public void reloadConfig() {
        loadConfig();
    }

    public String get(String path) {
        return cachedMessages.getOrDefault(path, "Missing message: " + path);
    }

    public String formatNoColor(String path, Object... replacements) {
        String message = get(path);
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                String key = String.valueOf(replacements[i]);
                String value;
                if (replacements[i+1] instanceof Double || replacements[i+1] instanceof Float) {
                    value = CurrencyFormatter.format((Double) replacements[i+1]);
                } else {
                    value = String.valueOf(replacements[i+1]);
                }
                message = message.replace("%" + key + "%", value);
            }
        }
        return message;
    }

    public String format(String path, Object... replacements) {
        String message = formatNoColor(path, replacements);
        return colorize(message);
    }

    public Component formatAsComponent(String key, Object... replacements) {
        String messageWithPlaceholders = formatNoColor(key, replacements);

        String miniMessageString = convertToMiniMessage(messageWithPlaceholders);

        return miniMessage.deserialize(miniMessageString);
    }

    private String convertToMiniMessage(String input) {
        if (input == null) return "";
        String result = input.replace("&0", "<black>")
                .replace("&1", "<dark_blue>")
                .replace("&2", "<dark_green>")
                .replace("&3", "<dark_aqua>")
                .replace("&4", "<dark_red>")
                .replace("&5", "<dark_purple>")
                .replace("&6", "<gold>")
                .replace("&7", "<gray>")
                .replace("&8", "<dark_gray>")
                .replace("&9", "<blue>")
                .replace("&a", "<green>")
                .replace("&b", "<aqua>")
                .replace("&c", "<red>")
                .replace("&d", "<light_purple>")
                .replace("&e", "<yellow>")
                .replace("&f", "<white>")
                .replace("&k", "<obfuscated>")
                .replace("&l", "<bold>")
                .replace("&m", "<strikethrough>")
                .replace("&n", "<underline>")
                .replace("&r", "<reset>");

        Matcher matcher = hexPattern.matcher(result);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "<#" + matcher.group(1) + ">");
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    public String colorize(String input) {
        if (input == null) {
            return "";
        }
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}