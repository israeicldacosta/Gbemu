package com.gbemu.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Superfície de renderização do emulador — construída em código, sem XML.
 * Por enquanto é apenas um placeholder (tela cinza escura) — quando a PPU
 * estiver implementada, cada frame (160x144) será desenhado aqui, escalado
 * para o tamanho da view.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private EmulatorCore emulatorCore;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public void setEmulatorCore(EmulatorCore core) {
        this.emulatorCore = core;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawPlaceholderFrame();
        if (emulatorCore != null) {
            emulatorCore.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO: recalcular escala do framebuffer 160x144 para o novo tamanho da view
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (emulatorCore != null) {
            emulatorCore.pause();
        }
    }

    /** Desenha um frame vazio até a PPU estar implementada e gerando frames reais. */
    private void drawPlaceholderFrame() {
        Canvas canvas = getHolder().lockCanvas();
        if (canvas == null) return;
        try {
            canvas.drawColor(Color.DKGRAY);
        } finally {
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    // TODO: adicionar método renderFrame(int[] framebuffer) para desenhar
    // o resultado real da PPU quando o núcleo de emulação estiver pronto.
}
