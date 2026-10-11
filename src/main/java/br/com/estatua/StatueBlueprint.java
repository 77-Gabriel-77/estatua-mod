package br.com.estatua;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Parte PURA (sem classes do Minecraft) da estatua: le as camadas de {@link StatueLayout}
 * e calcula, para cada bloco, o deslocamento no mundo conforme a direcao em que o jogador olha.
 * Por ser pura, pode ser testada sem o Minecraft.
 */
public final class StatueBlueprint {
    private StatueBlueprint() {}

    /** Um bloco da estatua: deslocamento (dx,dy,dz) a partir do centro da base e o simbolo da paleta. */
    public static final class Voxel {
        public final int dx, dy, dz;
        public final char symbol;
        Voxel(int dx, int dy, int dz, char symbol) { this.dx = dx; this.dy = dy; this.dz = dz; this.symbol = symbol; }
    }

    private static final int CENTER_C = StatueLayout.WIDTH / 2;   // coluna central
    private static final int CENTER_D = 4;                         // profundidade do centro do corpo

    public static int height() { return StatueLayout.HEIGHT; }

    /**
     * @param viewX,viewZ direcao horizontal em que o JOGADOR olha (um deles 0 e o outro +-1).
     *                    A estatua fica de frente para o jogador, ou seja, o rosto aponta para o jogador.
     */
    public static List<Voxel> voxels(int viewX, int viewZ) {
        if (Math.abs(viewX) + Math.abs(viewZ) != 1) throw new IllegalArgumentException("direcao horizontal invalida");
        int rightX = -viewZ, rightZ = viewX;   // vetor "direita" do observador
        List<Voxel> out = new ArrayList<Voxel>();
        for (int y = 0; y < StatueLayout.HEIGHT; y++) {
            String[] rows = StatueLayout.LAYERS[y];
            for (int d = 0; d < StatueLayout.DEPTH; d++) {
                String row = rows[d];
                for (int c = 0; c < StatueLayout.WIDTH; c++) {
                    char s = row.charAt(c);
                    if (s == '.') continue;
                    int lc = c - CENTER_C, ld = d - CENTER_D;
                    out.add(new Voxel(lc * rightX + ld * viewX, y, lc * rightZ + ld * viewZ, s));
                }
            }
        }
        return Collections.unmodifiableList(out);
    }
}
