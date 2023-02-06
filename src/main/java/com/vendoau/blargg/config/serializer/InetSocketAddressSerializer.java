package com.vendoau.blargg.config.serializer;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;

public final class InetSocketAddressSerializer implements TypeSerializer<InetSocketAddress> {

    public static final InetSocketAddressSerializer INSTANCE = new InetSocketAddressSerializer();

    private InetSocketAddressSerializer() {}

    @Override
    public InetSocketAddress deserialize(Type type, ConfigurationNode source) {
        if (source.childrenMap().isEmpty()) {
            final String[] split = source.getString().split(":");
            final String ip = split[0];
            final int port = Integer.parseInt(split[1]);
            return new InetSocketAddress(ip, port);
        }
        final String ip = source.node("ip").getString();
        final int port = source.node("port").getInt();
        return new InetSocketAddress(ip, port);
    }

    @Override
    public void serialize(Type type, @Nullable InetSocketAddress address, ConfigurationNode target) throws SerializationException {
        target.node("ip").set(address.getHostString());
        target.node("port").set(address.getPort());
    }
}
