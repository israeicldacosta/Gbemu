# GB Emulator — Estrutura do App

Este é o esqueleto de UI/navegação do app, conforme pedido:

1. **MainActivity** (tela inicial) → botão "Adicionar Jogo" abre o seletor de
   arquivos do Android para o usuário escolher a ROM já salva no celular.
2. **GameActivity** (tela do jogo) → mostra a `GameView` (onde os frames
   serão desenhados) + D-pad + botões A/B/Start/Select + botão central (☰).
3. **Botão central** → abre `SaveLoadDialogFragment`, com opções de
   Salvar Jogo, Carregar Save e Continuar Jogando.

## O que já funciona
- Navegação completa entre as telas
- Seleção de arquivo via Storage Access Framework
- Captura de toque nos botões (pressionar/soltar) repassada para o `EmulatorCore`
- Estrutura do menu de save/load (UI pronta, lógica de serialização pendente)

## O que ainda é placeholder (marcado com TODO no código)
- `EmulatorCore`: por enquanto não roda nenhuma instrução de fato. É aqui que
  entram CPU (LR35902), MMU, Cartridge/MBC e PPU quando implementarmos o
  núcleo de emulação.
- `GameView`: hoje só desenha uma tela cinza. Vai ganhar um método
  `renderFrame(int[] framebuffer)` para desenhar o resultado real da PPU.
- `saveState` / `loadState`: os arquivos já são criados na pasta certa
  (`filesDir/saves/`), mas a serialização do estado (registradores,
  memória, VRAM) ainda não existe.

## Arquivos
Todo o app é Java puro — as telas são montadas em código
(`LinearLayout`/`RelativeLayout` criados via `new`), sem nenhum arquivo XML
de layout.

```
app/src/main/java/com/gbemu/app/
├── MainActivity.java          # tela inicial (layout montado em código)
├── GameActivity.java          # tela do jogo + controles (layout em código)
├── GameView.java              # superfície de renderização
├── GameButton.java            # enum dos 8 botões
├── EmulatorCore.java          # ponto central de integração (CPU/MMU/PPU)
└── SaveLoadDialogFragment.java # menu de save/load (layout em código)
```

## Próximo passo natural
Implementar a CPU (LR35902) dentro de uma nova classe `Cpu.java`, começando
pelo conjunto de instruções sem prefixo, depois o bloco 0xCB. Posso montar
isso na sequência quando você quiser seguir.

**Nota:** este projeto assume que os arquivos de ROM usados vêm de mídias
que você já possui legalmente (extraídas por você mesmo). Este repositório
não inclui nem baixa nenhum jogo.
