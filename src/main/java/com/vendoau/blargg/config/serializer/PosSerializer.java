package com.vendoau.blargg.config.serializer;

import net.minestom.server.coordinate.Pos;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public final class PosSerializer implements TypeSerializer<Pos> {

    public static final PosSerializer INSTANCE = new PosSerializer();

    private PosSerializer() {}

    @Override
    public Pos deserialize(Type type, ConfigurationNode source) {
        if (source.childrenMap().isEmpty()) {
            return deserializeShortVersion(source);
        }
        return deserializeLongVersion(source);
    }

    private Pos deserializeShortVersion(ConfigurationNode source) {
        final String[] split = source.getString().split(",");

        if (split.length != 3 && split.length != 5) {
            throw new IllegalArgumentException();
        }

        final double x = Double.parseDouble(split[0]);
        final double y = Double.parseDouble(split[1]);
        final double z = Double.parseDouble(split[2]);
        if (split.length != 5) return new Pos(x, y, z);

        final float yaw = Float.parseFloat(split[3]);
        final float pitch = Float.parseFloat(split[4]);
        return new Pos(x, y, z, yaw, pitch);
    }

    private Pos deserializeLongVersion(ConfigurationNode source) {
        final double x = source.node("x").getDouble();
        final double y = source.node("y").getDouble();
        final double z = source.node("z").getDouble();
        final float yaw = source.node("yaw").getFloat();
        final float pitch = source.node("pitch").getFloat();
        return new Pos(x, y, z, yaw, pitch);
    }

    @Override
    public void serialize(Type type, @Nullable Pos pos, ConfigurationNode target) throws SerializationException {
        if (pos == null) {
            target.set(null);
            return;
        }

        target.node("x").set(pos.x());
        target.node("y").set(pos.y());
        target.node("z").set(pos.z());
        target.node("yaw").set(pos.yaw());
        target.node("pitch").set(pos.pitch());
    }
}
