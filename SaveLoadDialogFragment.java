package com.gbemu.app;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * Menu acessado pelo botão central durante o jogo — construído em código, sem XML.
 * Permite salvar o progresso atual (save state) ou carregar
 * um estado salvo anteriormente.
 */
public class SaveLoadDialogFragment extends DialogFragment {

    // Por simplicidade este esqueleto usa um único slot de save.
    private static final int DEFAULT_SLOT = 0;

    private EmulatorCore emulatorCore;
    private Runnable onDismissAction;

    public static SaveLoadDialogFragment newInstance() {
        return new SaveLoadDialogFragment();
    }

    public void setEmulatorCore(EmulatorCore core) {
        this.emulatorCore = core;
    }

    public void setOnDismissAction(Runnable action) {
        this.onDismissAction = action;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(buildLayout());
        dialog.setCancelable(true);
        return dialog;
    }

    /** Monta o conteúdo do menu: título + 3 botões. */
    private LinearLayout buildLayout() {
        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.parseColor("#2A2A2E"));
        int padding = dp(20);
        root.setPadding(padding, padding, padding, padding);
        root.setLayoutParams(new LinearLayout.LayoutParams(dp(280),
                LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView title = new TextView(requireContext());
        title.setText("Menu");
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);
        title.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        titleParams.bottomMargin = dp(16);
        title.setLayoutParams(titleParams);

        Button btnSave = fullWidthButton("Salvar Jogo");
        Button btnLoad = fullWidthButton("Carregar Save");
        Button btnResume = fullWidthButton("Continuar Jogando");

        btnSave.setOnClickListener(v -> {
            boolean success = emulatorCore.saveState(DEFAULT_SLOT);
            Toast.makeText(getContext(),
                    success ? "Jogo salvo" : "Não foi possível salvar",
                    Toast.LENGTH_SHORT).show();
        });

        btnLoad.setOnClickListener(v -> {
            boolean success = emulatorCore.loadState(DEFAULT_SLOT);
            Toast.makeText(getContext(),
                    success ? "Save carregado" : "Nenhum save encontrado",
                    Toast.LENGTH_SHORT).show();
            if (success) dismiss();
        });

        btnResume.setOnClickListener(v -> dismiss());

        root.addView(title);
        root.addView(btnSave);
        root.addView(btnLoad);
        root.addView(btnResume);
        return root;
    }

    private Button fullWidthButton(String label) {
        Button b = new Button(requireContext());
        b.setText(label);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = dp(8);
        b.setLayoutParams(params);
        return b;
    }

    private int dp(int value) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(value * density);
    }

    @Override
    public void onDismiss(@NonNull android.content.DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissAction != null) {
            onDismissAction.run();
        }
    }
}
