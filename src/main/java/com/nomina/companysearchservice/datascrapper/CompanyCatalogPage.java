package com.nomina.companysearchservice.datascrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

public class CompanyCatalogPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Базы
    public static final String AIPA_BASE = "https://old.aipa.am/search_mods/tm_database/view_item.php?id=";
    public static final String WIPO_BASE = "https://www3.wipo.int/madrid/monitor/en/showData.jsp?ID=ROM.";

    // Ключи для маппинга
    public final List<String> aipaKeys = Arrays.asList(
            "markName",                     // (540)
            "registrationSerialNumber",     // (111)
            "registrationYear",             // (151)
            "expectedRegistrationCompletion",// (181)
            "expectedRegistrationExtension", // (186)
            "applicationNumber",            // (210)
            "applicationFilingYear",        // (220)
            "applicationPublicationYear",   // (442)
            "goodsAndServices",             // (511)
            "colorAndCombination",          // (591)
            "proprietor",                   // (730)
            "representative"                // (750)
    );


    public final List<String> wipoKeys = Arrays.asList(
            "holderInfo",
            "establishmentJurisdiction",
            "legalInfo",
            "representativeInfo",
            "markType",
            "viennaClass",
            "niceClass",
            "basicApplication",
            "basicRegistration",
            "madridDesignation",
            "registrationDate",
            "expirationDate"
    );

    public CompanyCatalogPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    /* ========== Навигация и получение блоков данных ========== */

    public void open(String base, String id) {
        driver.get(base + id);
        waitUntilReady();
    }

    private void waitUntilReady() {
        // document.readyState == 'complete'
        wait.until((ExpectedCondition<Boolean>) d ->
                ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Возвращает найденные элементы с ожидаемым классом или пустой список, если их нет.
     */
    public List<WebElement> getCompanyData(String linkId, String searchingData, String dataLink, String link) throws InterruptedException {
        driver.get(link);

        // стартовая пауза — дайте странице дорендериться
        Thread.sleep(1200);

        List<WebElement> result = new ArrayList<>();
        int attempts = 6;          // сколько раз перепробовать
        int pauseMs = 600;        // пауза между попытками
        int minNonEmpty = 4;       // считаем, что страница «загрузилась», если непустых блоков >= 4

        for (int i = 0; i < attempts; i++) {
            result = driver.findElements(By.className(searchingData));
            long nonEmpty = result.stream()
                    .map(WebElement::getText)
                    .filter(t -> t != null && !t.trim().isEmpty())
                    .count();

            if (nonEmpty >= minNonEmpty) {
                break; // достаточно данных — выходим
            }
            Thread.sleep(pauseMs);
        }
        return result;
    }

    public List<WebElement> getCompanyData(String searchingData) throws InterruptedException {
        // стартовая пауза — дайте странице дорендериться
        Thread.sleep(1200);

        List<WebElement> result = new ArrayList<>();
        int attempts = 6;          // сколько раз перепробовать
        int pauseMs = 600;        // пауза между попытками
        int minNonEmpty = 4;       // считаем, что страница «загрузилась», если непустых блоков >= 4

        for (int i = 0; i < attempts; i++) {
            result = driver.findElements(By.className(searchingData));
            long nonEmpty = result.stream()
                    .map(WebElement::getText)
                    .filter(t -> t != null && !t.trim().isEmpty())
                    .count();

            if (nonEmpty >= minNonEmpty) {
                break; // достаточно данных — выходим
            }
            Thread.sleep(pauseMs);
        }
        return result;
    }

    /* ========== Преобразование DOM → Map ========== */

    public Map<String, String> zipToMap(List<String> keys, List<WebElement> values) {
        Map<String, String> dataMap = new LinkedHashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            String v = (i < values.size()) ? values.get(i).getText().trim() : "";
            dataMap.put(keys.get(i), v);
        }
        return dataMap;
    }

    /* ========== Выделение идентификаторов заявок ========== */

    /**
     * АIPA: достаём строго из карты поля "applicationNumber" и нормализуем.
     */
    public String extractAipaApplicationNumber(Map<String, String> dataMap) {
        String raw = String.valueOf(dataMap.getOrDefault("applicationNumber", "")).trim();
        String app = raw.replaceAll("[^a-zA-Z0-9_-]", "");
        return app.isEmpty() ? "unknown_app" : app;
    }

    /**
     * WIPO: пробуем найти application/basic application в списке текстовых блоков,
     * иначе берём fallback — сам id.
     */
    public String extractWipoApplicationNumber(List<WebElement> allTextBlocks, String fallbackId) {
        String candidate = null;
        for (WebElement el : allTextBlocks) {
            String t = el.getText();
            if (t == null) continue;
            String lower = t.toLowerCase(Locale.ROOT);
            if (lower.contains("application")) {
                // берём самую длинную последовательность цифр
                String digits = longestDigits(t);
                if (!digits.isEmpty()) {
                    candidate = digits;
                    break;
                }
            }
        }
        if (candidate == null || candidate.isEmpty()) {
            candidate = fallbackId;
        }
        return candidate;
    }

    private String longestDigits(String s) {
        String best = "";
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\d+").matcher(s);
        while (m.find()) {
            String g = m.group();
            if (g.length() > best.length()) best = g;
        }
        return best;
    }

    /* ========== Пути и запись файлов ========== */

    private void ensureParent(File f) throws IOException {
        File p = f.getParentFile();
        if (p != null && !p.exists()) {
            Files.createDirectories(p.toPath());
        }
    }

    private File jsonPath(String source, String status, String bucket, String file) {
        String projectDir = System.getProperty("user.dir");
        return Paths.get(projectDir, "src", "test", "resources", "json", status, source, bucket, file + ".json").toFile();
    }

    private File imagePath(String source, String bucket, String id, String extOrMissing) {
        String projectDir = System.getProperty("user.dir");
        return Paths.get(projectDir, "src", "test", "resources", "image", source, bucket, id + "." + extOrMissing).toFile();
    }

    public void saveJson(String source, String status, String bucket, String appNumber, Map<String, String> dataMap) throws IOException {
        File out = jsonPath(source, status, bucket, appNumber);
        ensureParent(out);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(out, dataMap);
        System.out.println("JSON saved: " + out.getAbsolutePath());
    }

    /* ========== Картинки ========== */

    /**
     * Возвращает src первой подходящей картинки или null.
     */
    public String findImageSrcById(String linkId) {
        try {
            Thread.sleep(1200);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }

        String xpathById = "//img[contains(@src, '" + linkId + "')]";
        String cssFallback = "img[src*='/dbimg/']"; // на AIPA картинки лежат в /dbimg/****

        int attempts = 6;   // сколько раз пробовать
        int pauseMs = 600; // пауза между попытками

        for (int i = 0; i < attempts; i++) {
            List<WebElement> imgs = driver.findElements(By.xpath(xpathById));
            if (imgs.isEmpty()) {
                imgs = driver.findElements(By.cssSelector(cssFallback));
            }

            if (!imgs.isEmpty()) {
                // небольшая задержка, чтобы байты успели подтянуться
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
                return imgs.get(0).getAttribute("src");
            }

            try {
                Thread.sleep(pauseMs);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }

        // не нашли
        return null;
    }


    private String guessExtFromUrl(String url) {
        String u = url.toLowerCase(Locale.ROOT);
        if (u.contains(".png")) return "png";
        if (u.contains(".webp")) return "webp";
        if (u.contains(".jpeg")) return "jpeg";
        return "jpg";
    }

    /**
     * «Есть валидные данные» — не один пустой блок, а реально контент. Порог можно подстроить.
     */
    public boolean hasLikelyValidData(List<WebElement> allData) {
        if (allData == null || allData.isEmpty()) return false;
        long nonEmpty = allData.stream().map(WebElement::getText).filter(t -> t != null && !t.trim().isEmpty()).count();
        return nonEmpty > 1;
    }

    /**
     * Для WIPO: пытаемся безопасно прочитать даты по классу "date".
     */
    public Optional<String[]> tryReadWipoDates() {
        List<WebElement> dates = driver.findElements(By.className("date"));
        if (dates.size() >= 4) {
            String reg = dates.get(2).getText();
            String exp = dates.get(3).getText();
            return Optional.of(new String[]{reg, exp});
        }
        return Optional.empty();
    }

    /* ========== Группировка ========== */

    /**
     * AIPA: бакет = префикс (например, "2010").
     */
    public String aipaBucket(String prefix) {
        return prefix;
    }

    /**
     * WIPO: бакет = id / 10000 (например, 1000000 → "100").
     */
    public String wipoBucket(String fullNumber) {
        try {
            int n = Integer.parseInt(fullNumber);
            return String.valueOf(n / 10000);
        } catch (NumberFormatException e) {
            return "unknown";
        }
    }

    /** хелпер, который ищет локатор в корне и по всем iframe */
    public WebElement findInAllFrames(WebDriver driver, By locator, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        driver.switchTo().defaultContent();

        // 1) Пробуем в корне
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException ignore) {}

        // 2) Перебор iframe
        List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
        for (WebElement frame : iframes) {
            try {
                driver.switchTo().defaultContent();
                driver.switchTo().frame(frame);
                return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            } catch (TimeoutException | NoSuchFrameException ignore) {
                // пробуем следующий
            }
        }

        driver.switchTo().defaultContent();
        throw new NoSuchElementException("Element not found in any frame: " + locator);
    }
}