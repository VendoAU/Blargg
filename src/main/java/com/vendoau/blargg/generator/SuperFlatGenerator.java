package com.vendoau.blargg.generator;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import org.jetbrains.annotations.NotNull;

public class SuperFlatGenerator implements Generator {

    @Override
    public void generate(@NotNull GenerationUnit unit) {
        final Point start = unit.absoluteStart();
        final Point size = unit.size();

        for (int x = 0; x < size.blockX(); x++) {
            for (int z = 0; z < size.blockZ(); z++) {
                unit.modifier().setBlock(start.add(x, 3, z), Block.GRASS_BLOCK);
                unit.modifier().setBlock(start.add(x, 2, z), Block.DIRT);
                unit.modifier().setBlock(start.add(x, 1, z), Block.DIRT);
                unit.modifier().setBlock(start.add(x, 0, z), Block.BEDROCK);
            }
        }
    }
}
