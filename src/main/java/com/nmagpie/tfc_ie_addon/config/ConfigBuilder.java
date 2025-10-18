package com.nmagpie.tfc_ie_addon.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * {@link net.dries007.tfc.config.ConfigBuilder}
 */
@SuppressWarnings("unused")
public class ConfigBuilder
{
    private final ModConfigSpec.Builder builder;
    private final Factory factory;
    private final String translationKeyPrefix;
    private boolean emptyLineAdded;

    public ConfigBuilder(ModConfigSpec.Builder builder, Factory factory, String translationKeyPrefix)
    {
        this.builder = builder;
        this.factory = factory;
        this.translationKeyPrefix = translationKeyPrefix;
        this.emptyLineAdded = false;
    }

    public ConfigBuilder push(String path)
    {
        builder.push(path);
        return this;
    }

    public ConfigBuilder swap(String path)
    {
        builder.pop().push(path);
        return this;
    }

    public ConfigBuilder pop()
    {
        builder.pop();
        return this;
    }

    public ConfigBuilder pop(int n)
    {
        for (int i = 0; i < n; i++) pop();
        return this;
    }

    public ConfigBuilder comment(String... text)
    {
        if (!emptyLineAdded)
        {
            builder.comment("");
            emptyLineAdded = true;
        }
        for (String line : text)
        {
            builder.comment(" " + line);
        }
        return this;
    }

    public Supplier<Boolean> define(String path, boolean value)
    {
        return factory.create(begin(path).define(path, value));
    }

    public Supplier<Integer> define(String path, int value, int min, int max)
    {
        return factory.create(begin(path).defineInRange(path, value, min, max));
    }

    public Supplier<Integer> define(String path, int value)
    {
        return factory.create(begin(path).define(path, value));
    }

    public Supplier<Double> define(String path, double value, double min, double max)
    {
        return factory.create(begin(path).defineInRange(path, value, min, max));
    }

    public Supplier<String> define(String path, String value)
    {
        return factory.create(begin(path).define(path, value));
    }

    public <E extends Enum<E>> Supplier<E> define(String path, E value)
    {
        return factory.create(begin(path).defineEnum(path, value));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Supplier<List<String>> define(String path, List<String> value, Predicate<String> predicate)
    {
        return (Supplier) factory.create(begin(path).defineListAllowEmpty(path, new ArrayList<>(value), String::new, o -> o instanceof String s && predicate.test(s)));
    }

    private ModConfigSpec.Builder begin(String path)
    {
        builder.translation("tfc_ie_addon.config." + translationKeyPrefix + "." + path);
        emptyLineAdded = false;
        return builder;
    }

    interface Factory
    {
        <T, V extends ModConfigSpec.ConfigValue<T>> Supplier<T> create(V value);
    }

    record ServerValue<T>(ModConfigSpec.ConfigValue<T> value) implements Supplier<T>
    {
        @Override
        public T get()
        {
            return Config.SERVER.spec().isLoaded() ? value.get() : value.getDefault();
        }
    }
}
