package net.vg.sleepcycle.config;

/*
 * Copyright (c) 2021 magistermaks
 * Modified by VgLevelUp 2024
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class SimpleConfig {

    private static final Logger LOGGER = LogManager.getLogger("SimpleConfig");
    private final LinkedHashMap<String, ConfigEntry> config = new LinkedHashMap<>();
    private final ConfigRequest request;
    private boolean broken = false;

    public interface DefaultConfig {
        String get(String namespace);

        static String empty(String namespace) {
            return "";
        }
    }

    public static class ConfigRequest {

        private final File file;
        private final String filename;
        private DefaultConfig provider;

        private ConfigRequest(File file, String filename) {
            this.file = file;
            this.filename = filename;
            this.provider = DefaultConfig::empty;
        }

        public ConfigRequest provider(DefaultConfig provider) {
            this.provider = provider;
            return this;
        }

        public SimpleConfig request() {
            return new SimpleConfig(this);
        }

        private String getConfig() {
            return provider.get(filename) + "\n";
        }
    }

    public static ConfigRequest of(String filename) {
        Path path = FabricLoader.getInstance().getConfigDir();
        return new ConfigRequest(path.resolve(filename + ".properties").toFile(), filename);
    }

    private void createConfig() throws IOException {
        request.file.getParentFile().mkdirs();
        Files.createFile(request.file.toPath());

        PrintWriter writer = new PrintWriter(request.file, "UTF-8");
        writer.write(request.getConfig());
        writer.close();
    }

    private void loadConfig() throws IOException {
        Scanner reader = new Scanner(request.file);
        StringBuilder currentComment = new StringBuilder();
        for (int line = 1; reader.hasNextLine(); line++) {
            currentComment = parseConfigEntry(reader.nextLine(), line, currentComment);
        }
    }

    private StringBuilder parseConfigEntry(String entry, int line, StringBuilder currentComment) {
        if (entry.startsWith("#")) {
            currentComment.append(entry).append("\n");
        } else if (!entry.isEmpty()) {
            String[] parts = entry.split("=", 2);
            if (parts.length == 2) {
                config.put(parts[0], new ConfigEntry(parts[1].split(" #")[0], currentComment.toString()));
                currentComment.setLength(0);  // clear the currentComment
            } else {
                throw new RuntimeException("Syntax error in config file on line " + line + "!");
            }
        } else {
            currentComment.append("\n");
        }
        return currentComment;
    }

    private SimpleConfig(ConfigRequest request) {
        this.request = request;
        String identifier = "Config '" + request.filename + "'";

        if (!request.file.exists()) {
            LOGGER.info(identifier + " is missing, generating default one...");

            try {
                createConfig();
            } catch (IOException e) {
                LOGGER.error(identifier + " failed to generate!");
                LOGGER.trace(e);
                broken = true;
            }
        }

        if (!broken) {
            try {
                loadConfig();
            } catch (Exception e) {
                LOGGER.error(identifier + " failed to load!");
                LOGGER.trace(e);
                broken = true;
            }
        }
    }

    public String get(String key) {
        ConfigEntry entry = config.get(key);
        return entry != null ? entry.value : null;
    }

    public String getOrDefault(String key, String def) {
        String val = get(key);
        return val == null ? def : val;
    }

    public int getOrDefault(String key, int def) {
        try {
            return Integer.parseInt(get(key));
        } catch (Exception e) {
            return def;
        }
    }

    public boolean getOrDefault(String key, boolean def) {
        String val = get(key);
        if (val != null) {
            return val.equalsIgnoreCase("true");
        }
        return def;
    }

    public double getOrDefault(String key, double def) {
        try {
            return Double.parseDouble(get(key));
        } catch (Exception e) {
            return def;
        }
    }

    public boolean isBroken() {
        return broken;
    }

    public boolean delete() {
        LOGGER.warn("Config '" + request.filename + "' was removed from existence! Restart the game to regenerate it.");
        return request.file.delete();
    }

    public void set(String key, String value) {
        config.put(key, new ConfigEntry(value, config.get(key).comment));
    }

    public void set(String key, int value) {
        config.put(key, new ConfigEntry(Integer.toString(value), config.get(key).comment));
    }

    public void set(String key, boolean value) {
        config.put(key, new ConfigEntry(Boolean.toString(value), config.get(key).comment));
    }

    public void set(String key, double value) {
        config.put(key, new ConfigEntry(Double.toString(value), config.get(key).comment));
    }

    public void save() {
        try {
            PrintWriter writer = new PrintWriter(request.file, "UTF-8");
            for (Map.Entry<String, ConfigEntry> entry : config.entrySet()) {
                if (!entry.getValue().comment.isEmpty()) {
                    writer.println(entry.getValue().comment.trim());
                }
                writer.println(entry.getKey() + "=" + entry.getValue().value);
                writer.println();  // Ensure there's a blank line between entries
            }
            writer.close();
        } catch (IOException e) {
            LOGGER.error("Failed to save config!");
            LOGGER.trace(e);
        }
    }

    public void setComment(String key, String comment) {
        ConfigEntry entry = config.get(key);
        if (entry != null) {
            entry.comment = comment;
        } else {
            config.put(key, new ConfigEntry("", comment));
        }
    }

    private static class ConfigEntry {
        String value;
        String comment;

        ConfigEntry(String value, String comment) {
            this.value = value;
            this.comment = comment;
        }
    }
}
