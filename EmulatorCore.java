package com.gbemu.app;

import android.content.Context;
import android.net.Uri;
import java.io.File;

/**
 * Ponto central de integração do emulador.
 *
 * Esta classe é o "controlador" que vai orquestrar os componentes reais
 * do emulador (CPU, MMU, PPU, APU, Cartridge/MBC) quando forem implementados.
 * Por enquanto contém apenas a estrutura: carregar ROM, iniciar/pausar/retomar,
 * repassar input, e salvar/carregar estado.
 *
 * TODO (próximas etapas do projeto):
 *  - CPU: implementar o conjunto de instruções do LR35902
 *  - MMU: mapear ROM/VRAM/WRAM/OAM/I-O/HRAM
 *  - Cartridge/MBC: leitura da ROM e bank switching (MBC1, MBC3, etc.)
 *  - PPU: renderização de background, window e sprites
 *  - Loop principal: rodar em uma thread separada, sincronizado a ~59.7 fps
 */
public class EmulatorCore {

    private final Context context;
    private final Uri romUri;
    private boolean running = false;

    public EmulatorCore(Context context, Uri romUri) {
        this.context = context;
        this.romUri = romUri;
        // TODO: ler os bytes da ROM via context.getContentResolver().openInputStream(romUri)
        // e inicializar o Cartridge/MBC correspondente.
    }

    /** Inicia o loop de emulação (chamado quando a GameView fica pronta). */
    public void start() {
        running = true;
        // TODO: iniciar thread do loop principal (fetch-decode-execute + timing da PPU)
    }

    /** Pausa a emulação, por exemplo ao abrir o menu de save/load. */
    public void pause() {
        running = false;
    }

    /** Retoma a emulação após o menu ser fechado. */
    public void resume() {
        if (!running) {
            running = true;
            // TODO: retomar o loop principal
        }
    }

    /** Encerra a emulação e libera recursos. */
    public void shutdown() {
        running = false;
        // TODO: liberar buffers, fechar streams de arquivo, etc.
    }

    public void onButtonPressed(GameButton button) {
        // TODO: atualizar registrador de joypad (0xFF00) e disparar interrupção se necessário
    }

    public void onButtonReleased(GameButton button) {
        // TODO: atualizar registrador de joypad (0xFF00)
    }

    /**
     * Salva o estado atual do emulador (registradores da CPU, memória, VRAM etc.)
     * em um arquivo dentro da pasta de dados do app.
     */
    public boolean saveState(int slot) {
        File saveFile = getSaveFile(slot);
        // TODO: serializar o estado completo (CPU + memória + MBC) para saveFile
        return false;
    }

    /** Carrega um estado salvo anteriormente, restaurando a emulação a partir dele. */
    public boolean loadState(int slot) {
        File saveFile = getSaveFile(slot);
        if (!saveFile.exists()) return false;
        // TODO: desserializar o estado salvo e restaurar CPU/memória/MBC
        return false;
    }

    private File getSaveFile(int slot) {
        File savesDir = new File(context.getFilesDir(), "saves");
        if (!savesDir.exists()) savesDir.mkdirs();
        return new File(savesDir, "save_slot_" + slot + ".sav");
    }
}
