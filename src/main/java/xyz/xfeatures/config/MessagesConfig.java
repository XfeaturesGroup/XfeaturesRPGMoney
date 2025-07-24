package xyz.xfeatures.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import xyz.xfeatures.XfeaturesRPGMoney;
import xyz.xfeatures.util.CurrencyFormatter;

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

        configFile = new File(messagesDir, "messages-" + currentLanguage + ".yml");

        if (!configFile.exists()) {
            if (plugin.getResource("messages/messages-" + currentLanguage + ".yml") != null) {
                plugin.saveResource("messages/messages-" + currentLanguage + ".yml", false);
            } else {
                plugin.getLogger().warning("Language file for '" + currentLanguage + "' not found. Using English as fallback.");
                configFile = new File(messagesDir, "messages-en.yml");
                
                if (!configFile.exists() && plugin.getResource("messages/messages-en.yml") != null) {
                    plugin.saveResource("messages/messages-en.yml", false);
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
        String message = config.getString(path, "Missing message: " + path);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String format(String path, Object... replacements) {
        String message = get(path);
        
        if (replacements.length % 2 != 0) {
            plugin.getLogger().warning("Неверное количество аргументов для форматирования сообщения: " + path);
            return message;
        }
        
        Map<String, Object> replacementMap = new HashMap<>();
        for (int i = 0; i < replacements.length; i += 2) {
            if (replacements[i] instanceof String) {
                String key = (String) replacements[i];
                Object value = replacements[i + 1];

                if ("amount".equals(key) && value instanceof Number) {
                    double amount = ((Number) value).doubleValue();
                    replacementMap.put(key, String.format("%.2f", amount));
                } else {
                    replacementMap.put(key, value);
                }
            }
        }
        
        for (Map.Entry<String, Object> entry : replacementMap.entrySet()) {
            message = message.replace("%" + entry.getKey() + "%", String.valueOf(entry.getValue()));
        }
        
        return message;
    }

    public Component formatAsComponent(String key, Object... replacements) {
        String formatted = format(key, replacements);
        return miniMessage.deserialize(convertToMiniMessage(formatted));
    }

    private String convertToMiniMessage(String input) {
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
                .replace("&l", "<bold>")
                .replace("&m", "<strikethrough>")
                .replace("&n", "<underlined>")
                .replace("&o", "<italic>")
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
        if (input == null) return "";

        Matcher matcher = hexPattern.matcher(input);
        StringBuffer sb = new StringBuffer();
        
        while (matcher.find()) {
            String hexColor = matcher.group(1);
            matcher.appendReplacement(sb, ChatColor.valueOf("#" + hexColor).toString());
        }
        matcher.appendTail(sb);

        return ChatColor.translateAlternateColorCodes('&', sb.toString());
    }
}