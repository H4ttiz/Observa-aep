package br.com.observaacao.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataUtil {
    public static String formatarDataHora(LocalDateTime dataParaFormatar) {
        if (dataParaFormatar == null) return "";

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dataParaFormatar.format(formatador);
    }

    public static String formatarData(LocalDateTime dataParaFormatar) {
        if (dataParaFormatar == null) return "";

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dataParaFormatar.format(formatador);
    }

    public static LocalDateTime somarDias(int diasParaSoma) {
        return LocalDateTime.now().plusDays(diasParaSoma);
    }
}
