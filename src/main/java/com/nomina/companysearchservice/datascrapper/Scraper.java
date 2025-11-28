package com.nomina.companysearchservice.datascrapper;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scraper {

    public WebDriver driver = WebDriverFactory.createDriver();
    CompanyCatalogPage companyCatalogPage;
    List<WebElement> allData;
    String aipaLink = CompanyCatalogPage.AIPA_BASE;
    String wipoLink = CompanyCatalogPage.WIPO_BASE;
    private CompanyCatalogPage page;

    public DataModel scrapeAipa(String link) throws Exception {
        page = new CompanyCatalogPage(driver);
                var dataModel = new DataModel();

                // Загружаем блоки и пытаемся собрать карту
                List<WebElement> allData = page.getCompanyData(null, "data", CompanyCatalogPage.AIPA_BASE, link);

                // Прямой маппинг по ключам AIPA
                Map<String, String> dataMap = page.zipToMap(page.aipaKeys, allData);
                boolean hasData = page.hasLikelyValidData(allData);

                if (hasData) {
                    String appNum = page.extractAipaApplicationNumber(dataMap);
                    String markName = dataMap.getOrDefault("markName", "UNKNOWN");
                    dataModel.setMarkName(markName);
                    dataModel.setData(dataMap);
                    dataModel.setType("aipa");
                } else {
                    // «Нет данных» — пишем в error с именем по самому id (чтобы отличать)
                   /* page.saveJson("aipa", "error", bucket, fullId, dataMap);
                    page.downloadImageWithRetry(CompanyCatalogPage.AIPA_BASE, "aipa", bucket, fullId, dataModel);*/
                }
                return dataModel;
    }

    public DataModel scrapeWipo(String link) throws Exception {
        page = new CompanyCatalogPage(driver);
        driver.get(link);


        By markNameLoc = By.cssSelector(".markname"); // используем в хелпере
        var dataModel = new DataModel();

        // Загружаем блоки и пытаемся собрать данные
        List<WebElement> allData = page.getCompanyData("text");

        // Номер обращения
        String fullNumber = "";
        WebElement h = page.findInAllFrames(driver, markNameLoc, Duration.ofSeconds(20)); // <<< замена
        String t = (h.getText() == null ? "" : h.getText().trim())
                .replace('\u2013', '-')  // – en dash
                .replace('\u2014', '-'); // — em dash

        Matcher m = Pattern.compile("^\\s*(\\d+)\\s*[-—–]?").matcher(t);
        fullNumber = m.find() ? m.group(1) : "";

        // Определяем корзину - bucket
        String bucket = page.wipoBucket(fullNumber);

        // 2) Определяем holder и niceClasses
        String holder = "";
        String niceClasses = "";
        try {
            holder = new WebDriverWait(driver, Duration.ofSeconds(60))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[@class='lapin client holType'])[1]"))).getText();
        } catch (TimeoutException e) {
            page.saveJson("wipo", "error", bucket, fullNumber, Map.of("applicationNumber", fullNumber, "reason", "holder-missing"));
        }

        try {
            niceClasses = new WebDriverWait(driver, Duration.ofSeconds(60))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[@class='nice']//div"))).getText();
        } catch (TimeoutException e) {
            page.saveJson("wipo", "error", bucket, fullNumber, Map.of("applicationNumber", fullNumber, "reason", "niceClasses-missing"));
        }

        // 3) Даты
        String regDate = "", expDate = "";
        Optional<String[]> datesOpt = page.tryReadWipoDates();
        if (datesOpt.isPresent()) {
            regDate = datesOpt.get()[0];
            expDate = datesOpt.get()[1];
        }

        // 4) 732 и 511
        String holder732 = "";
        String goods511 = "";
        try {
            WebElement n732 = driver.findElement(By.xpath(
                    "//div[@class='inidCode' and contains(text(),'732')]/following::div[contains(@class,'text')][1]"
            ));
            holder732 = Optional.ofNullable(n732.getText()).orElse("").trim();
        } catch (NoSuchElementException ignored) {
        }

        try {
            WebElement n511 = driver.findElement(By.xpath(
                    "//div[@class='inidCode' and contains(text(),'511')]/following::div[contains(@class,'text')][1]"
            ));
            goods511 = Optional.ofNullable(n511.getText()).orElse("").trim();
        } catch (NoSuchElementException ignored) {
        }

        // 5) Собираем JSON
        Map<String, String> dataMap = new LinkedHashMap<>();
        dataMap.put("markName", holder);
        dataMap.put("applicationNumber", fullNumber);
        dataMap.put("holder", holder);
        dataMap.put("niceClasses", niceClasses);
        dataMap.put("holder732", holder732);
        dataMap.put("goodsAndServices511", goods511);
        dataMap.put("registrationDate", regDate);
        dataMap.put("expirationDate", expDate);

        boolean hasData = page.hasLikelyValidData(allData);
        String appNum = fullNumber;

        if (hasData) {
            page.saveJson("wipo", "success", bucket, appNum, dataMap);
            dataModel.setType("wipo");
            dataModel.setData(dataMap);
            dataModel.setMarkName(holder);
            dataModel.setFullId(fullNumber);
            dataModel.setLink(CompanyCatalogPage.WIPO_BASE + fullNumber);
        } else {

        }
        return dataModel;
    }
}
