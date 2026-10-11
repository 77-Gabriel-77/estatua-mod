# Estátua Personalizada — Forge 1.16.5 (36.2.39)

**STATUS: código-fonte pronto, mas o JAR AINDA NÃO FOI COMPILADO.** O ambiente em que este projeto foi
corrigido não tem acesso à internet, então o Gradle não conseguiu baixar o Forge. Nenhum teste dentro do jogo foi feito.

## Como gerar o JAR (escolha UMA opção)
**A) Windows:** instale o JDK 8, coloque `gradlew`, `gradlew.bat` e `gradle/wrapper/gradle-wrapper.jar` do MDK
do Forge 1.16.5-36.2.39 nesta pasta (ou instale o Gradle 7.4.2) e dê dois cliques em `compilar.bat`.
**B) Sem instalar nada:** suba esta pasta para um repositório no GitHub; a aba *Actions* roda
`.github/workflows/build.yml` e disponibiliza o JAR como artefato.
Resultado: `build/libs/estatuapersonalizada-1.0.0.jar`  (use o arquivo SEM `-sources`/`-slim`).

## Instalar
Instale o Forge 1.16.5-36.2.39 (ou 36.2.x mais novo), copie o JAR para `%appdata%\.minecraft\mods`.

## Uso
Criativo → aba Decoração → "Estátua Personalizada" (ou receita: 3 lã preta / terracota branca + lã rosa + terracota branca / 3 tijolos de pedra).
Clique direito num bloco: a estátua (13×10×31 blocos, com base) nasce em cima dele, de frente para você.
Não consome no Criativo; consome 1 no Sobrevivência. Só constrói se TODO o espaço estiver livre (ar/plantas), dentro
do limite de altura e onde você tem permissão (spawn protection, modo aventura, borda do mundo).

## Estrutura
- `StatueLayout.java` — GERADO por `tools/gerar_estatua.py` (python3 tools/gerar_estatua.py --preview pasta). Edite o script, não o arquivo.
- `StatueBlueprint.java` — rotação/posições (sem Minecraft, testável). `StatuePalette.java` — símbolo → bloco vanilla.
- `StatueItem.java` — lógica do clique. `EstatuaPersonalizada.java` — registro.
- `src/test/.../BlueprintCheck.java` — testes executáveis sem Minecraft.
