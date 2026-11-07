package dev.tralala.module;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dev.tralala.module.impl.AutoTotem;
import dev.tralala.module.impl.Fullbright;
import dev.tralala.module.impl.Sprint;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final Gson GSON = new Gson();
    private final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        modules.add(new AutoTotem());
        modules.add(new Sprint());
        modules.add(new Fullbright());
    }

    public List<Module> getAll() {
        return modules;
    }

    public List<Module> getByCategory(Category category) {
        List<Module> result = new ArrayList<>();
        for (Module m : modules) {
            if (m.getCategory() == category) {
                result.add(m);
            }
        }
        return result;
    }

    public List<Module> getEnabled() {
        List<Module> result = new ArrayList<>();
        for (Module m : modules) {
            if (m.isEnabled()) {
                result.add(m);
            }
        }
        return result;
    }

    public void tick(MinecraftClient mc) {
        for (Module m : modules) {
            if (m.isEnabled()) {
                m.onTick();
            }
        }
    }

    private Path configFile() {
        return FabricLoader.getInstance().getConfigDir().resolve("tralala-client.json");
    }

    public void save() {
        try {
            JsonArray array = new JsonArray();
            for (Module m : modules) {
                if (m.isEnabled()) {
                    array.add(m.getName());
                }
            }
            Files.writeString(configFile(), GSON.toJson(array));
        } catch (IOException | RuntimeException e) {
            System.err.println("[tralala] could not save config: " + e);
        }
    }

    public void load() {
        Path file = configFile();
        if (!Files.exists(file)) {
            return;
        }
        try {
            JsonArray array = JsonParser.parseString(Files.readString(file)).getAsJsonArray();
            for (JsonElement element : array) {
                String name = element.getAsString();
                for (Module m : modules) {
                    if (m.getName().equals(name)) {
                        m.setEnabled(true);
                    }
                }
            }
        } catch (IOException | RuntimeException e) {
            System.err.println("[tralala] could not load config: " + e);
        }
    }
}
