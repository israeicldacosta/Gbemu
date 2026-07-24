package com.gbemu.app;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Tela onde o jogo roda de fato — construída inteiramente em Java, sem XML.
 * Contém:
 *  - GameView: a superfície onde os frames do emulador são desenhados
 *  - Controles virtuais (D-pad + A/B/Start/Select)
 *  - Botão central que abre o menu de Save/Load
 */
public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_ROM_URI = "extra_rom_uri";

    private GameView gameView;
    private EmulatorCore emulatorCore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String romUriString = getIntent().getStringExtra(EXTRA_ROM_URI);
        Uri romUri = Uri.parse(romUriString);
        emulatorCore = new EmulatorCore(this, romUri);

        setContentView(buildLayout());
        gameView.setEmulatorCore(emulatorCore);
    }

    /** Monta a tela inteira em código: GameView em cima, controles embaixo. */
    private RelativeLayout buildLayout() {
        RelativeLayout root = new RelativeLayout(this);
        root.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        root.setBackgroundColor(Color.BLACK);

        RelativeLayout controlsArea = buildControlsArea();
        controlsArea.setId(View.generateViewId());
        RelativeLayout.LayoutParams controlsParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, dp(220));
        controlsParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        controlsArea.setLayoutParams(controlsParams);

        gameView = new GameView(this);
        RelativeLayout.LayoutParams gameViewParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, 0);
        gameViewParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        gameViewParams.addRule(RelativeLayout.ABOVE, controlsArea.getId());
        gameView.setLayoutParams(gameViewParams);

        root.addView(gameView);
        root.addView(controlsArea);
        return root;
    }

    /** Monta a faixa inferior com D-pad, botão de menu, A/B e Start/Select. */
    private RelativeLayout buildControlsArea() {
        RelativeLayout area = new RelativeLayout(this);
        area.setBackgroundColor(Color.parseColor("#1B1B1F"));
        area.setPadding(dp(12), dp(12), dp(12), dp(12));

        // D-pad (esquerda)
        RelativeLayout dpad = new RelativeLayout(this);
        RelativeLayout.LayoutParams dpadParams = new RelativeLayout.LayoutParams(dp(150), dp(150));
        dpadParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        dpadParams.addRule(RelativeLayout.CENTER_VERTICAL);
        dpad.setLayoutParams(dpadParams);

        Button btnUp = squareButton("▲");
        RelativeLayout.LayoutParams upParams = new RelativeLayout.LayoutParams(dp(50), dp(50));
        upParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        upParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btnUp.setLayoutParams(upParams);

        Button btnDown = squareButton("▼");
        RelativeLayout.LayoutParams downParams = new RelativeLayout.LayoutParams(dp(50), dp(50));
        downParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        downParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btnDown.setLayoutParams(downParams);

        Button btnLeft = squareButton("◀");
        RelativeLayout.LayoutParams leftParams = new RelativeLayout.LayoutParams(dp(50), dp(50));
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        leftParams.addRule(RelativeLayout.CENTER_VERTICAL);
        btnLeft.setLayoutParams(leftParams);

        Button btnRight = squareButton("▶");
        RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams(dp(50), dp(50));
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        rightParams.addRule(RelativeLayout.CENTER_VERTICAL);
        btnRight.setLayoutParams(rightParams);

        dpad.addView(btnUp);
        dpad.addView(btnDown);
        dpad.addView(btnLeft);
        dpad.addView(btnRight);

        // Botão central: abre o menu de Save/Load
        Button btnMenu = new Button(this);
        btnMenu.setText("☰");
        btnMenu.setTextSize(20);
        RelativeLayout.LayoutParams menuParams = new RelativeLayout.LayoutParams(dp(60), dp(60));
        menuParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        btnMenu.setLayoutParams(menuParams);
        btnMenu.setOnClickListener(v -> openPauseMenu());

        // Botões A/B (direita)
        RelativeLayout actionButtons = new RelativeLayout(this);
        RelativeLayout.LayoutParams actionParams = new RelativeLayout.LayoutParams(dp(150), dp(150));
        actionParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        actionParams.addRule(RelativeLayout.CENTER_VERTICAL);
        actionButtons.setLayoutParams(actionParams);

        Button btnA = squareButton("A");
        RelativeLayout.LayoutParams aParams = new RelativeLayout.LayoutParams(dp(60), dp(60));
        aParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        aParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        btnA.setLayoutParams(aParams);

        Button btnB = squareButton("B");
        RelativeLayout.LayoutParams bParams = new RelativeLayout.LayoutParams(dp(60), dp(60));
        bParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        bParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        btnB.setLayoutParams(bParams);

        actionButtons.addView(btnA);
        actionButtons.addView(btnB);

        // Start/Select (parte inferior central)
        LinearLayout startSelect = new LinearLayout(this);
        startSelect.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams startSelectParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        startSelectParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        startSelectParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        startSelect.setLayoutParams(startSelectParams);

        Button btnSelect = smallButton("SELECT");
        LinearLayout.LayoutParams selectParams = new LinearLayout.LayoutParams(dp(80), dp(36));
        selectParams.rightMargin = dp(8);
        btnSelect.setLayoutParams(selectParams);

        Button btnStart = smallButton("START");
        btnStart.setLayoutParams(new LinearLayout.LayoutParams(dp(80), dp(36)));

        startSelect.addView(btnSelect);
        startSelect.addView(btnStart);

        area.addView(dpad);
        area.addView(btnMenu);
        area.addView(actionButtons);
        area.addView(startSelect);

        // Liga todos os botões de jogo (exceto o de menu) ao emulador
        bindButton(btnUp, GameButton.UP);
        bindButton(btnDown, GameButton.DOWN);
        bindButton(btnLeft, GameButton.LEFT);
        bindButton(btnRight, GameButton.RIGHT);
        bindButton(btnA, GameButton.A);
        bindButton(btnB, GameButton.B);
        bindButton(btnStart, GameButton.START);
        bindButton(btnSelect, GameButton.SELECT);

        return area;
    }

    private Button squareButton(String label) {
        Button b = new Button(this);
        b.setText(label);
        return b;
    }

    private Button smallButton(String label) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextSize(10);
        return b;
    }

    /** Liga um botão da UI a uma tecla do Game Boy (pressionar/soltar). */
    private void bindButton(Button uiButton, GameButton button) {
        uiButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    emulatorCore.onButtonPressed(button);
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    emulatorCore.onButtonReleased(button);
                    return true;
            }
            return false;
        });
    }

    private void openPauseMenu() {
        emulatorCore.pause();
        SaveLoadDialogFragment dialog = SaveLoadDialogFragment.newInstance();
        dialog.setEmulatorCore(emulatorCore);
        dialog.setOnDismissAction(emulatorCore::resume);
        dialog.show(getSupportFragmentManager(), "save_load_menu");
    }

    private int dp(int value) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(value * density);
    }

    @Override
    protected void onPause() {
        super.onPause();
        emulatorCore.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        emulatorCore.shutdown();
    }
}
