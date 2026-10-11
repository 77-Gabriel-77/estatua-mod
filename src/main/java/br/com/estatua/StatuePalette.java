package br.com.estatua;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

/** Traduz os simbolos de {@link StatueLayout} em blocos vanilla 1.16.5 (nenhum cai por gravidade). */
public final class StatuePalette {
    private StatuePalette() {}

    public static BlockState stateFor(char symbol) {
        return blockFor(symbol).defaultBlockState();
    }

    static Block blockFor(char symbol) {
        switch (symbol) {
            case 'T': return Blocks.STONE_BRICKS;          // base
            case 'U': return Blocks.SMOOTH_STONE;
            case 'O': return Blocks.MOSSY_STONE_BRICKS;
            case 'W': return Blocks.WHITE_CONCRETE;        // sola / detalhes dos tenis
            case 'w': return Blocks.LIGHT_GRAY_CONCRETE;   // tenis
            case 'z': return Blocks.GRAY_CONCRETE;
            case 'K': return Blocks.BLACK_CONCRETE;        // calca
            case 'C': return Blocks.COAL_BLOCK;
            case 'P': return Blocks.PINK_CONCRETE;         // blusa
            case 'M': return Blocks.MAGENTA_WOOL;
            case 'S': return Blocks.WHITE_TERRACOTTA;      // pele
            case 'L': return Blocks.PINK_WOOL;             // labios
            case 'E': return Blocks.BROWN_TERRACOTTA;      // olhos
            case 'F': return Blocks.BLACK_CONCRETE;        // armacao dos oculos
            case 'R': return Blocks.RED_CONCRETE;          // brinco
            case 'H': return Blocks.BLACK_WOOL;            // cabelo
            case 'B': return Blocks.BLACKSTONE;
            case 'g': return Blocks.GRAY_CONCRETE;
            default: throw new IllegalArgumentException("simbolo desconhecido: " + symbol);
        }
    }
}
