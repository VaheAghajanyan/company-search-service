package com.nomina.companysearchservice.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class TransliterationService {

    // Armenian to English transliteration map
    private static final Map<String, String> ARM_TO_ENG = new HashMap<>();
    // Armenian to Russian transliteration map
    private static final Map<String, String> ARM_TO_RUS = new HashMap<>();
    // Russian to English transliteration map
    private static final Map<String, String> RUS_TO_ENG = new HashMap<>();
    // English to Russian transliteration map
    private static final Map<String, String> ENG_TO_RUS = new HashMap<>();
    // Russian to Armenian transliteration map
    private static final Map<String, String> RUS_TO_ARM = new HashMap<>();
    // English to Armenian transliteration map
    private static final Map<String, String> ENG_TO_ARM = new HashMap<>();

    static {
        // Armenian to English
        ARM_TO_ENG.put("ա", "a"); ARM_TO_ENG.put("Ա", "A");
        ARM_TO_ENG.put("բ", "b"); ARM_TO_ENG.put("Բ", "B");
        ARM_TO_ENG.put("գ", "g"); ARM_TO_ENG.put("Գ", "G");
        ARM_TO_ENG.put("դ", "d"); ARM_TO_ENG.put("Դ", "D");
        ARM_TO_ENG.put("ե", "e"); ARM_TO_ENG.put("Ե", "E");
        ARM_TO_ENG.put("զ", "z"); ARM_TO_ENG.put("Զ", "Z");
        ARM_TO_ENG.put("է", "e"); ARM_TO_ENG.put("Է", "E");
        ARM_TO_ENG.put("ը", "y"); ARM_TO_ENG.put("Ը", "Y");
        ARM_TO_ENG.put("թ", "t"); ARM_TO_ENG.put("Թ", "T");
        ARM_TO_ENG.put("ժ", "zh"); ARM_TO_ENG.put("Ժ", "Zh");
        ARM_TO_ENG.put("ի", "i"); ARM_TO_ENG.put("Ի", "I");
        ARM_TO_ENG.put("լ", "l"); ARM_TO_ENG.put("Լ", "L");
        ARM_TO_ENG.put("խ", "kh"); ARM_TO_ENG.put("Խ", "Kh");
        ARM_TO_ENG.put("ծ", "ts"); ARM_TO_ENG.put("Ծ", "Ts");
        ARM_TO_ENG.put("կ", "k"); ARM_TO_ENG.put("Կ", "K");
        ARM_TO_ENG.put("հ", "h"); ARM_TO_ENG.put("Հ", "H");
        ARM_TO_ENG.put("ձ", "dz"); ARM_TO_ENG.put("Ձ", "Dz");
        ARM_TO_ENG.put("ղ", "gh"); ARM_TO_ENG.put("Ղ", "Gh");
        ARM_TO_ENG.put("ճ", "ch"); ARM_TO_ENG.put("Ճ", "Ch");
        ARM_TO_ENG.put("մ", "m"); ARM_TO_ENG.put("Մ", "M");
        ARM_TO_ENG.put("յ", "y"); ARM_TO_ENG.put("Յ", "Y");
        ARM_TO_ENG.put("ն", "n"); ARM_TO_ENG.put("Ն", "N");
        ARM_TO_ENG.put("շ", "sh"); ARM_TO_ENG.put("Շ", "Sh");
        ARM_TO_ENG.put("ո", "o"); ARM_TO_ENG.put("Ո", "O");
        ARM_TO_ENG.put("չ", "ch"); ARM_TO_ENG.put("Չ", "Ch");
        ARM_TO_ENG.put("պ", "p"); ARM_TO_ENG.put("Պ", "P");
        ARM_TO_ENG.put("ջ", "j"); ARM_TO_ENG.put("Ջ", "J");
        ARM_TO_ENG.put("ռ", "r"); ARM_TO_ENG.put("Ռ", "R");
        ARM_TO_ENG.put("ս", "s"); ARM_TO_ENG.put("Ս", "S");
        ARM_TO_ENG.put("վ", "v"); ARM_TO_ENG.put("Վ", "V");
        ARM_TO_ENG.put("տ", "t"); ARM_TO_ENG.put("Տ", "T");
        ARM_TO_ENG.put("ր", "r"); ARM_TO_ENG.put("Ր", "R");
        ARM_TO_ENG.put("ց", "ts"); ARM_TO_ENG.put("Ց", "Ts");
        ARM_TO_ENG.put("ու", "u"); ARM_TO_ENG.put("ՈՒ", "U");
        ARM_TO_ENG.put("Ու", "U");
        ARM_TO_ENG.put("փ", "p"); ARM_TO_ENG.put("Փ", "P");
        ARM_TO_ENG.put("ք", "q"); ARM_TO_ENG.put("Ք", "Q");
        ARM_TO_ENG.put("օ", "o"); ARM_TO_ENG.put("Օ", "O");
        ARM_TO_ENG.put("ֆ", "f"); ARM_TO_ENG.put("Ֆ", "F");
        ARM_TO_ENG.put("և", "ev"); ARM_TO_ENG.put("ԵՎ", "EV");

        // Armenian to Russian
        ARM_TO_RUS.put("ա", "а"); ARM_TO_RUS.put("Ա", "А");
        ARM_TO_RUS.put("բ", "б"); ARM_TO_RUS.put("Բ", "Б");
        ARM_TO_RUS.put("գ", "г"); ARM_TO_RUS.put("Գ", "Г");
        ARM_TO_RUS.put("դ", "д"); ARM_TO_RUS.put("Դ", "Д");
        ARM_TO_RUS.put("ե", "е"); ARM_TO_RUS.put("Ե", "Е");
        ARM_TO_RUS.put("զ", "з"); ARM_TO_RUS.put("Զ", "З");
        ARM_TO_RUS.put("է", "э"); ARM_TO_RUS.put("Է", "Э");
        ARM_TO_RUS.put("ը", "ы"); ARM_TO_RUS.put("Ը", "Ы");
        ARM_TO_RUS.put("թ", "т"); ARM_TO_RUS.put("Թ", "Т");
        ARM_TO_RUS.put("ժ", "ж"); ARM_TO_RUS.put("Ժ", "Ж");
        ARM_TO_RUS.put("ի", "и"); ARM_TO_RUS.put("Ի", "И");
        ARM_TO_RUS.put("լ", "л"); ARM_TO_RUS.put("Լ", "Л");
        ARM_TO_RUS.put("խ", "х"); ARM_TO_RUS.put("Խ", "Х");
        ARM_TO_RUS.put("ծ", "ц"); ARM_TO_RUS.put("Ծ", "Ц");
        ARM_TO_RUS.put("կ", "к"); ARM_TO_RUS.put("Կ", "К");
        ARM_TO_RUS.put("հ", "г"); ARM_TO_RUS.put("Հ", "Г");
        ARM_TO_RUS.put("ձ", "дз"); ARM_TO_RUS.put("Ձ", "Дз");
        ARM_TO_RUS.put("ղ", "х"); ARM_TO_RUS.put("Ղ", "Х");
        ARM_TO_RUS.put("ճ", "ч"); ARM_TO_RUS.put("Ճ", "Ч");
        ARM_TO_RUS.put("մ", "м"); ARM_TO_RUS.put("Մ", "М");
        ARM_TO_RUS.put("յ", "й"); ARM_TO_RUS.put("Յ", "Й");
        ARM_TO_RUS.put("ն", "н"); ARM_TO_RUS.put("Ն", "Н");
        ARM_TO_RUS.put("շ", "ш"); ARM_TO_RUS.put("Շ", "Ш");
        ARM_TO_RUS.put("ո", "о"); ARM_TO_RUS.put("Ո", "О");
        ARM_TO_RUS.put("չ", "ч"); ARM_TO_RUS.put("Չ", "Ч");
        ARM_TO_RUS.put("պ", "п"); ARM_TO_RUS.put("Պ", "П");
        ARM_TO_RUS.put("ջ", "дж"); ARM_TO_RUS.put("Ջ", "Дж");
        ARM_TO_RUS.put("ռ", "р"); ARM_TO_RUS.put("Ռ", "Р");
        ARM_TO_RUS.put("ս", "с"); ARM_TO_RUS.put("Ս", "С");
        ARM_TO_RUS.put("վ", "в"); ARM_TO_RUS.put("Վ", "В");
        ARM_TO_RUS.put("տ", "т"); ARM_TO_RUS.put("Տ", "Т");
        ARM_TO_RUS.put("ր", "р"); ARM_TO_RUS.put("Ր", "Р");
        ARM_TO_RUS.put("ց", "ц"); ARM_TO_RUS.put("Ց", "Ц");
        ARM_TO_RUS.put("ու", "у"); ARM_TO_RUS.put("ՈՒ", "У");
        ARM_TO_RUS.put("Ու", "У");
        ARM_TO_RUS.put("փ", "п"); ARM_TO_RUS.put("Փ", "П");
        ARM_TO_RUS.put("ք", "к"); ARM_TO_RUS.put("Ք", "К");
        ARM_TO_RUS.put("օ", "о"); ARM_TO_RUS.put("Օ", "О");
        ARM_TO_RUS.put("ֆ", "ф"); ARM_TO_RUS.put("Ֆ", "Ф");
        ARM_TO_RUS.put("և", "ев"); ARM_TO_RUS.put("ԵՎ", "ЕВ");

        // Russian to English
        RUS_TO_ENG.put("а", "a"); RUS_TO_ENG.put("А", "A");
        RUS_TO_ENG.put("б", "b"); RUS_TO_ENG.put("Б", "B");
        RUS_TO_ENG.put("в", "v"); RUS_TO_ENG.put("В", "V");
        RUS_TO_ENG.put("г", "g"); RUS_TO_ENG.put("Г", "G");
        RUS_TO_ENG.put("д", "d"); RUS_TO_ENG.put("Д", "D");
        RUS_TO_ENG.put("е", "e"); RUS_TO_ENG.put("Е", "E");
        RUS_TO_ENG.put("ё", "yo"); RUS_TO_ENG.put("Ё", "Yo");
        RUS_TO_ENG.put("ж", "zh"); RUS_TO_ENG.put("Ж", "Zh");
        RUS_TO_ENG.put("з", "z"); RUS_TO_ENG.put("З", "Z");
        RUS_TO_ENG.put("и", "i"); RUS_TO_ENG.put("И", "I");
        RUS_TO_ENG.put("й", "y"); RUS_TO_ENG.put("Й", "Y");
        RUS_TO_ENG.put("к", "k"); RUS_TO_ENG.put("К", "K");
        RUS_TO_ENG.put("л", "l"); RUS_TO_ENG.put("Л", "L");
        RUS_TO_ENG.put("м", "m"); RUS_TO_ENG.put("М", "M");
        RUS_TO_ENG.put("н", "n"); RUS_TO_ENG.put("Н", "N");
        RUS_TO_ENG.put("о", "o"); RUS_TO_ENG.put("О", "O");
        RUS_TO_ENG.put("п", "p"); RUS_TO_ENG.put("П", "P");
        RUS_TO_ENG.put("р", "r"); RUS_TO_ENG.put("Р", "R");
        RUS_TO_ENG.put("с", "s"); RUS_TO_ENG.put("С", "S");
        RUS_TO_ENG.put("т", "t"); RUS_TO_ENG.put("Т", "T");
        RUS_TO_ENG.put("у", "u"); RUS_TO_ENG.put("У", "U");
        RUS_TO_ENG.put("ф", "f"); RUS_TO_ENG.put("Ф", "F");
        RUS_TO_ENG.put("х", "kh"); RUS_TO_ENG.put("Х", "Kh");
        RUS_TO_ENG.put("ц", "ts"); RUS_TO_ENG.put("Ц", "Ts");
        RUS_TO_ENG.put("ч", "ch"); RUS_TO_ENG.put("Ч", "Ch");
        RUS_TO_ENG.put("ш", "sh"); RUS_TO_ENG.put("Ш", "Sh");
        RUS_TO_ENG.put("щ", "sch"); RUS_TO_ENG.put("Щ", "Sch");
        RUS_TO_ENG.put("ъ", ""); RUS_TO_ENG.put("Ъ", "");
        RUS_TO_ENG.put("ы", "y"); RUS_TO_ENG.put("Ы", "Y");
        RUS_TO_ENG.put("ь", ""); RUS_TO_ENG.put("Ь", "");
        RUS_TO_ENG.put("э", "e"); RUS_TO_ENG.put("Э", "E");
        RUS_TO_ENG.put("ю", "yu"); RUS_TO_ENG.put("Ю", "Yu");
        RUS_TO_ENG.put("я", "ya"); RUS_TO_ENG.put("Я", "Ya");

        // Build reverse maps
        for (Map.Entry<String, String> entry : ARM_TO_RUS.entrySet()) {
            RUS_TO_ARM.put(entry.getValue(), entry.getKey());
        }
        for (Map.Entry<String, String> entry : ARM_TO_ENG.entrySet()) {
            ENG_TO_ARM.put(entry.getValue(), entry.getKey());
        }
        for (Map.Entry<String, String> entry : RUS_TO_ENG.entrySet()) {
            ENG_TO_RUS.put(entry.getValue(), entry.getKey());
        }
    }

    public String transliterate(String text, String fromLang, String toLang) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        if (fromLang.equals(toLang)) {
            return text; // No conversion needed
        }

        Map<String, String> transliterationMap = getTransliterationMap(fromLang, toLang);

        if (transliterationMap == null) {
            return text; // Unsupported conversion, return original
        }

        return transliterateText(text, transliterationMap);
    }

    private Map<String, String> getTransliterationMap(String from, String to) {
        String key = from + "_TO_" + to;

        switch (key) {
            case "ARM_TO_ENG":
                return ARM_TO_ENG;
            case "ARM_TO_RUS":
                return ARM_TO_RUS;
            case "RUS_TO_ENG":
                return RUS_TO_ENG;
            case "RUS_TO_ARM":
                return RUS_TO_ARM;
            case "ENG_TO_ARM":
                return ENG_TO_ARM;
            case "ENG_TO_RUS":
                return ENG_TO_RUS;
            default:
                return null;
        }
    }

    private String transliterateText(String text, Map<String, String> map) {
        StringBuilder result = new StringBuilder();
        int i = 0;

        while (i < text.length()) {
            boolean matched = false;

            // Try to match multi-character sequences first (up to 3 chars)
            for (int len = Math.min(3, text.length() - i); len > 0; len--) {
                String substring = text.substring(i, i + len);
                String replacement = map.get(substring);

                if (replacement != null) {
                    result.append(replacement);
                    i += len;
                    matched = true;
                    break;
                }
            }

            // If no match found, keep the original character
            if (!matched) {
                result.append(text.charAt(i));
                i++;
            }
        }

        return result.toString();
    }
}