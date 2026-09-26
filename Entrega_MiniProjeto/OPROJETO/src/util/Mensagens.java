package util;

import java.util.Locale;
import java.util.ResourceBundle;

//Classe utilitária para suporte a múltiplos idiomas

public class Mensagens {
    private static ResourceBundle bundle;
    private static Locale localeAtual = Locale.of("pt", "BR");

    static {
        bundle = ResourceBundle.getBundle("mensagens", localeAtual);
    }

    public static String get(String chave) {
        try {
            return bundle.getString(chave);
        } catch (Exception e) {
            return chave; // Retorna a chave se não encontrar
        }
    }

    public static void setIdioma(String idioma) {
        if (idioma.equals("en")) {
            localeAtual = Locale.of("en", "US");
        } else {
            localeAtual = Locale.of("pt", "BR");
        }
        bundle = ResourceBundle.getBundle("mensagens", localeAtual);
    }

    public static Locale getLocaleAtual() {
        return localeAtual;
    }
}
