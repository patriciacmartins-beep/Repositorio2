package com.empresa.rh;

import com.empresa.rh.view.RHView;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        // Valores no formato brasileiro (R$ 3.500,00)
        Locale.setDefault(Locale.forLanguageTag("pt-BR"));

        RHView view = new RHView();
        view.exibirMenu();
    }
}
