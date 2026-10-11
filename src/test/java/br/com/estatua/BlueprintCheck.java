package br.com.estatua;

import java.util.*;

/** Verificacoes executaveis sem Minecraft:  javac + java BlueprintCheck */
public class BlueprintCheck {
    static int fails = 0;
    static void check(boolean ok, String msg) { System.out.println((ok ? "[OK]   " : "[FALHA] ") + msg); if (!ok) fails++; }

    public static void main(String[] a) {
        // 1. dimensoes do layout
        boolean dims = StatueLayout.LAYERS.length == StatueLayout.HEIGHT;
        for (String[] l : StatueLayout.LAYERS) { dims &= l.length == StatueLayout.DEPTH; for (String r : l) dims &= r.length() == StatueLayout.WIDTH; }
        check(dims, "layout " + StatueLayout.WIDTH + "x" + StatueLayout.DEPTH + "x" + StatueLayout.HEIGHT + " consistente");

        int[][] dirs = {{0,-1},{1,0},{0,1},{-1,0}};
        List<StatueBlueprint.Voxel> ref = StatueBlueprint.voxels(0, -1);
        check(ref.size() == StatueBlueprint.voxels(0, -1).size() && ref.size() > 500, "blocos totais: " + ref.size() + " (deterministico)");

        String known = "TUOWwzKCPMSLEFRHBg";
        Set<Character> used = new TreeSet<Character>();
        for (StatueBlueprint.Voxel v : ref) used.add(v.symbol);
        boolean allKnown = true; for (char c : used) allKnown &= known.indexOf(c) >= 0;
        check(allKnown, "simbolos usados: " + used + " (todos mapeados na paleta)");

        for (int[] d : dirs) {
            String nome = "dir(" + d[0] + "," + d[1] + ")";
            List<StatueBlueprint.Voxel> vs = StatueBlueprint.voxels(d[0], d[1]);
            check(vs.size() == ref.size(), nome + ": mesma quantidade de blocos");
            int minY = 99, maxY = -99; Set<String> pos = new HashSet<String>();
            double fx = 0, fz = 0, hx = 0, hz = 0; int nf = 0, nh = 0;
            for (StatueBlueprint.Voxel v : vs) {
                minY = Math.min(minY, v.dy); maxY = Math.max(maxY, v.dy);
                pos.add(v.dx + "," + v.dy + "," + v.dz);
                if (v.symbol == 'F') { fx += v.dx; fz += v.dz; nf++; }
                if (v.symbol == 'H' && v.dy >= 15) { hx += v.dx; hz += v.dz; nh++; }
            }
            check(minY == 0 && maxY == 30, nome + ": y de 0 a 30 (nada enterrado, 31 de altura)");
            check(pos.size() == vs.size(), nome + ": sem blocos sobrepostos");
            // rosto (oculos) tem que estar do lado do jogador: a projecao na direcao em que o jogador olha deve ser MENOR que a do cabelo de tras
            double pf = (fx / nf) * d[0] + (fz / nf) * d[1], ph = (hx / nh) * d[0] + (hz / nh) * d[1];
            check(pf < ph, nome + String.format(": oculos virados para o jogador (%.2f < %.2f)", pf, ph));
            // conectividade: todo bloco ligado a base
            Deque<String> st = new ArrayDeque<String>(); Set<String> seen = new HashSet<String>();
            for (StatueBlueprint.Voxel v : vs) if (v.dy == 0) { String k = v.dx+","+v.dy+","+v.dz; if (seen.add(k)) st.push(k); }
            while (!st.isEmpty()) {
                String[] p = st.pop().split(","); int x = Integer.parseInt(p[0]), y = Integer.parseInt(p[1]), z = Integer.parseInt(p[2]);
                int[][] n = {{1,0,0},{-1,0,0},{0,1,0},{0,-1,0},{0,0,1},{0,0,-1}};
                for (int[] o : n) { String k = (x+o[0])+","+(y+o[1])+","+(z+o[2]); if (pos.contains(k) && seen.add(k)) st.push(k); }
            }
            check(seen.size() == pos.size(), nome + ": nenhum bloco flutuando");
        }
        // 2. rotacao: girar 90 graus preserva a figura (mesmo conjunto de blocos girado)
        Set<String> s1 = new TreeSet<String>();
        for (StatueBlueprint.Voxel v : StatueBlueprint.voxels(0, -1)) s1.add(v.dz * -1 + "," + v.dy + "," + v.dx + ":" + v.symbol); // (x,z)->(-z,x)
        Set<String> s2 = new TreeSet<String>();
        for (StatueBlueprint.Voxel v : StatueBlueprint.voxels(1, 0)) s2.add(v.dx + "," + v.dy + "," + v.dz + ":" + v.symbol);
        check(s1.equals(s2), "rotacao norte->leste e uma rotacao rigida de 90 graus (sem espelhar)");
        System.out.println(fails == 0 ? "\nTODOS OS TESTES PASSARAM" : "\nFALHAS: " + fails);
        System.exit(fails == 0 ? 0 : 1);
    }
}
