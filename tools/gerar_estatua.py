#!/usr/bin/env python3
"""Gera StatueLayout.java (camadas da estatua) e imagens de pre-visualizacao.
Deterministico: semente fixa. Uso: python tools/gerar_estatua.py [--preview DIR]"""
import random, sys, os

W, D, H = 13, 10, 31
rnd = random.Random(20260611)
V = {}  # (c, d, y) -> char ; c = coluna (esq->dir visto de frente), d = profundidade (0 = frente)

def put(c, d, y, ch, over=True):
    if 0 <= c < W and 0 <= d < D and 0 <= y < H and (over or (c, d, y) not in V):
        V[(c, d, y)] = ch

def box(c1, c2, d1, d2, y1, y2, chars):
    for c in range(c1, c2 + 1):
        for d in range(d1, d2 + 1):
            for y in range(y1, y2 + 1):
                put(c, d, y, rnd.choice(chars) if len(chars) > 1 else chars[0])

PINK = "PPPPPPPM"      # P = concreto rosa, M = la magenta
PANTS = "KKKKKC"       # K = concreto preto, C = bloco de carvao
HAIR = "HHHHHCCBg"     # H la preta, C carvao, B blackstone, g concreto cinza
SKIN = "S"

# --- base de pedra (y=0)
for c in range(1, 12):
    for d in range(1, 9):
        put(c, d, 0, rnd.choice("TTTTTUU"))
put(11, 1, 0, "O"); put(2, 8, 0, "O")

# --- sapatos (y=1 sola branca, y=2 cano cinza claro/branco)
for c0 in (3, 7):
    box(c0, c0 + 2, 2, 5, 1, 1, "W")
    box(c0, c0 + 2, 2, 5, 2, 2, "wwwwz")
    for c in range(c0, c0 + 3): put(c, 2, 2, "W")      # ponta branca
put(9, 1, 1, "W")  # pequena irregularidade: pe direito um pouco mais largo
# --- pernas
box(3, 5, 3, 5, 3, 9, PANTS); box(7, 9, 3, 5, 3, 9, PANTS)
box(6, 6, 3, 5, 9, 9, PANTS)                       # entrepernas
# --- quadril (9 de largura) e bumbum
box(2, 10, 3, 5, 10, 11, PANTS)
box(4, 8, 6, 6, 10, 10, PANTS); box(5, 7, 6, 6, 11, 11, PANTS)
# --- blusa rosa: barra, cintura fina, peito, ombros
box(3, 9, 3, 5, 12, 12, PINK)
box(4, 8, 3, 5, 13, 13, PINK)
box(3, 9, 3, 5, 14, 18, PINK)
for c in (4, 5, 7, 8): put(c, 2, 16, "P")          # busto discreto
put(4, 2, 17, "P"); put(8, 2, 17, "P")
box(1, 11, 3, 5, 19, 19, PINK)                     # linha dos ombros
for c in (5, 6, 7): put(c, 3, 19, SKIN)            # decote redondo
# --- bracos: manga curta rosa + antebraco/mao de pele
for c0 in (1, 10):
    box(c0, c0 + 1, 3, 5, 16, 18, PINK)
    box(c0, c0 + 1, 3, 5, 12, 15, SKIN)
put(1, 2, 12, SKIN)                                 # mao esquerda levemente a frente
# --- pescoco e cabeca
box(5, 7, 3, 5, 20, 21, SKIN)
box(4, 8, 2, 5, 22, 22, SKIN)                       # queixo
box(3, 9, 2, 4, 23, 26, SKIN)                       # rosto
box(3, 9, 5, 6, 23, 26, HAIR)                       # nuca coberta de cabelo
box(3, 9, 2, 6, 27, 27, HAIR)                       # topo
# rosto: boca, olhos, nariz, orelha com brinco
for c in (5, 6, 7): put(c, 2, 23, "L")
put(4, 2, 25, "E"); put(8, 2, 25, "E")
put(6, 1, 24, SKIN)                                 # nariz
put(2, 3, 24, SKIN); put(2, 4, 24, SKIN); put(2, 3, 23, "R")   # orelha + brinco vermelho
put(10, 3, 24, "H"); put(10, 4, 24, "H")
# --- oculos grandes e redondos (aro a frente do rosto, d=1)
def lens(c0):
    for c in (c0 + 1, c0 + 2):
        put(c, 1, 26, "F"); put(c, 1, 23, "F")
    for y in (24, 25):
        put(c0, 1, y, "F"); put(c0 + 3, 1, y, "F")
lens(2); lens(7); put(6, 1, 25, "F")
for d in (2, 3, 4):
    put(2, d, 25, "F"); put(10, d, 25, "F")         # hastes
# --- cabelo: laterais, franja cacheada, volume atras e fios sobre os ombros
for d in range(1, 8):
    for y in range(22, 28):
        for c in (2, 10):
            if (c, d, y) not in V and rnd.random() < 0.92: put(c, d, y, rnd.choice(HAIR))
for d in range(2, 8):
    for y in range(21, 28):
        for c in (1, 11):
            if rnd.random() < 0.75: put(c, d, y, rnd.choice(HAIR), over=False)
for y in range(22, 27):
    for c in (0, 12):
        if rnd.random() < 0.30: put(c, rnd.randint(3, 6), y, rnd.choice(HAIR), over=False)
# franja (curtain bangs) + cachos na testa
for c in range(3, 10):
    if c != 6 or rnd.random() < 0.5: put(c, 2, 27, "H")
    if rnd.random() < 0.30: put(c, 1, 27, rnd.choice(HAIR))
put(5, 2, 26, "C"); put(8, 2, 26, "H")
put(4, 0, 27, "H"); put(8, 0, 27, "B")
put(5, 1, 27, "H"); put(7, 1, 27, "C")
# cabelo caido nas laterais do pescoco
for c in (3, 4, 8, 9):
    for y in (20, 21):
        for d in range(3, 7):
            if rnd.random() < 0.85: put(c, d, y, rnd.choice(HAIR), over=False)
# mechas na frente dos ombros
for c, ylow in ((2, 17), (3, 18), (10, 16), (9, 18)):
    for y in range(ylow, 22):
        put(c, 2, y, rnd.choice(HAIR), over=False)
put(2, 1, 20, "H"); put(10, 1, 19, "C")
# massa de cabelo atras (comprimento irregular ate os ombros/meio das costas)
for c in range(1, 12):
    edge = abs(c - 6)
    for d in (6, 7, 8):
        ylow = {6: 16, 7: 15, 8: 17}[d] + (edge // 3) * 2 + rnd.randint(-1, 2)
        for y in range(ylow, 28):
            if d == 8 and y < 18 and rnd.random() < 0.5: continue
            if rnd.random() < 0.9: put(c, d, y, rnd.choice(HAIR), over=False)
for c in range(3, 10):                              # topo com volume e cachos
    for d in range(2, 7): put(c, d, 28, rnd.choice(HAIR))
for c in range(2, 11):
    for d in range(3, 8):
        if rnd.random() < 0.75: put(c, d, 28, rnd.choice(HAIR), over=False)
for c in range(3, 10):
    for d in range(3, 7):
        if rnd.random() < 0.65: put(c, d, 29, rnd.choice(HAIR))
for c in range(4, 9):
    for d in range(4, 6):
        if rnd.random() < 0.4: put(c, d, 30, rnd.choice(HAIR))
for c, d in ((2, 4), (10, 5), (1, 5), (11, 4)): put(c, d, 28, "H", over=False)

# --- remove blocos soltos (nao ligados a base) para evitar pedacos flutuando
seen, stack = set(), [k for k in V if k[2] == 0]
seen.update(stack)
while stack:
    c, d, y = stack.pop()
    for n in ((c+1,d,y),(c-1,d,y),(c,d+1,y),(c,d-1,y),(c,d,y+1),(c,d,y-1)):
        if n in V and n not in seen: seen.add(n); stack.append(n)
removed = [k for k in V if k not in seen]
for k in removed: del V[k]

def layer_rows(y):
    return ["".join(V.get((c, d, y), ".") for c in range(W)) for d in range(D)]

def write_java(path):
    out = ["package br.com.estatua;", "",
           "/** ARQUIVO GERADO por tools/gerar_estatua.py - nao edite a mao. */",
           "final class StatueLayout {",
           "    private StatueLayout() {}", "",
           "    static final int WIDTH = %d, DEPTH = %d, HEIGHT = %d;" % (W, D, H),
           "    /** LAYERS[y][d].charAt(c): y=altura, d=profundidade (0=frente), c=coluna (esq->dir vista de frente). */",
           "    static final String[][] LAYERS = {"]
    for y in range(H):
        out.append("        { // y=%d" % y)
        out.append(",\n".join('            "%s"' % r for r in layer_rows(y)))
        out.append("        },")
    out += ["    };", "}", ""]
    open(path, "w").write("\n".join(out))

COL = {'T':(125,125,125),'U':(167,167,167),'O':(110,130,100),'W':(235,235,235),'w':(155,155,150),'z':(75,80,84),
 'K':(10,12,18),'C':(20,20,24),'P':(214,101,143),'M':(180,70,170),'S':(209,178,161),'L':(240,150,170),
 'E':(77,51,36),'F':(8,8,10),'R':(180,30,30),'H':(25,25,30),'B':(55,50,52),'g':(60,64,68)}

def preview(outdir):
    from PIL import Image, ImageDraw
    os.makedirs(outdir, exist_ok=True)
    s = 22
    def shade(col, f): return tuple(max(0, min(255, int(v * f))) for v in col)
    def view(name, axis):
        # axis: 'front','back','left','right'
        if axis in ('front','back'):
            w = W
        else: w = D
        img = Image.new("RGB", (w * s, H * s), (200, 220, 240)); dr = ImageDraw.Draw(img)
        for u in range(w):
            for y in range(H):
                best = None
                rng = range(D) if axis == 'front' else range(D-1, -1, -1) if axis == 'back' else range(W) if axis == 'left' else range(W-1, -1, -1)
                for t in rng:
                    key = {'front':(u,t,y),'back':(W-1-u,t,y),'left':(t,u,y),'right':(t,D-1-u,y)}[axis]
                    # left/right: u along depth. left view: viewer on statue's right side? just consistent
                    if key in V: best = V[key]; break
                if best:
                    x0, y0 = u * s, (H - 1 - y) * s
                    dr.rectangle([x0, y0, x0 + s - 1, y0 + s - 1], fill=COL[best], outline=shade(COL[best], 0.7))
        img.save(os.path.join(outdir, name + ".png"))
    for a in ('front','back','left'): view(a, a)
    # isometrico simples (3/4 frente-esquerda)
    sc = 20; img = Image.new("RGB", (1000, 1000), (200, 220, 240)); dr = ImageDraw.Draw(img)
    def proj(c, d, y):
        x = (c - d) * sc * 0.87 + 500; yy = 900 - y * sc - (c + d) * sc * 0.5
        return x, yy
    # camera olha de frente-direita-do-espectador: pontos mais distantes tem d grande e c grande
    for (c, d, y) in sorted(V, key=lambda k: (-(k[0] + k[1]), k[2])):
        col = COL[V[(c, d, y)]]
        x0, y0 = proj(c, d, y)
        top = [proj(c, d, y+1), proj(c+1, d, y+1), proj(c+1, d+1, y+1), proj(c, d+1, y+1)]
        left = [proj(c, d, y), proj(c, d+1, y), proj(c, d+1, y+1), proj(c, d, y+1)]
        right = [proj(c, d, y), proj(c+1, d, y), proj(c+1, d, y+1), proj(c, d, y+1)]
        dr.polygon(top, fill=shade(col, 1.15)); dr.polygon(left, fill=shade(col, 0.75)); dr.polygon(right, fill=shade(col, 0.95))
    img.save(os.path.join(outdir, "iso.png"))

if __name__ == "__main__":
    here = os.path.dirname(os.path.abspath(__file__))
    write_java(os.path.join(here, "..", "src", "main", "java", "br", "com", "estatua", "StatueLayout.java"))
    print("blocos:", len(V), "removidos soltos:", len(removed))
    if "--preview" in sys.argv: preview(sys.argv[sys.argv.index("--preview") + 1])
