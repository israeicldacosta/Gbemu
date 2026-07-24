package com.gbemu.app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Tela inicial do emulador — construída inteiramente em Java, sem XML.
 * Contém apenas o botão "Adicionar Jogo", que abre o seletor de arquivos
 * do sistema (Storage Access Framework) para o usuário escolher a ROM
 * já presente no armazenamento do celular.
 */
public class MainActivity extends AppCompatActivity {

    private final ActivityResultLauncher<String[]> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
                if (uri != null) {
                    openGame(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(buildLayout());
    }

    /** Monta a tela inteira em código: título + botão, centralizados. */
    private LinearLayout buildLayout() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setBackgroundColor(Color.parseColor("#1B1B1F"));
        root.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        TextView title = new TextView(this);
        title.setText("GB Emulator");
        title.setTextColor(Color.WHITE);
        title.setTextSize(28);
        title.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        titleParams.bottomMargin = dp(40);
        title.setLayoutParams(titleParams);

        Button addGameButton = new Button(this);
        addGameButton.setText("Adicionar Jogo");
        addGameButton.setTextSize(16);
        addGameButton.setLayoutParams(new LinearLayout.LayoutParams(dp(220), dp(56)));
        addGameButton.setOnClickListener(v -> openFilePicker());

        root.addView(title);
        root.addView(addGameButton);
        return root;
    }

    /** Abre o seletor de arquivos do Android para escolher a ROM (.gb/.gbc). */
    private void openFilePicker() {
        filePickerLauncher.launch(new String[]{"*/*"});
    }

    /** Recebe a URI escolhida e inicia a tela do jogo. */
    private void openGame(Uri romUri) {
        getContentResolver().takePersistableUriPermission(
                romUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.EXTRA_ROM_URI, romUri.toString());
        startActivity(intent);
    }

    private int dp(int value) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(value * density);
    }
}
