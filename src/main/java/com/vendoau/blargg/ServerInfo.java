package com.vendoau.blargg;

import java.net.InetSocketAddress;

public record ServerInfo(String name, InetSocketAddress address, int playerCount, boolean online) {
}
