package net.minecraftforge.common.config;
import java.io.File;
public class Configuration {
    public Configuration() {}
    public Configuration(File file) {}
    public void load() {}
    public void save() {}
    public Property get(String category, String key, String defaultValue) { return new Property(defaultValue); }
    public Property get(String category, String key, boolean defaultValue) { return new Property(String.valueOf(defaultValue)); }
    public Property get(String category, String key, int defaultValue) { return new Property(String.valueOf(defaultValue)); }
    public Property get(String category, String key, double defaultValue) { return new Property(String.valueOf(defaultValue)); }
}