package com.nomina.companysearchservice.controller;

import com.nomina.companysearchservice.datascrapper.DataModel;
import com.nomina.companysearchservice.datascrapper.Scraper;
import com.nomina.companysearchservice.dto.RefreshRequestDto;
import com.nomina.companysearchservice.dto.SimilarNameDto;
import com.nomina.companysearchservice.repository.AipaRepository;
import com.nomina.companysearchservice.service.AipaService;
import com.nomina.companysearchservice.service.TransliterationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/aipa")
public class AipaController {

    private final AipaService aipaService;
    private final AipaRepository aipaRepository;
    private final TransliterationService transliterationService;

    public AipaController(AipaService aipaService, AipaRepository aipaRepository, TransliterationService transliterationService) {
        this.aipaService = aipaService;
        this.aipaRepository = aipaRepository;
        this.transliterationService = transliterationService;
    }

    @GetMapping("/search-by-name")
    public ResponseEntity<List<SimilarNameDto>> search(
            @RequestParam("query") String query,
            @RequestHeader(value = "From", required = false) String from,
            @RequestHeader(value = "To", required = false) String to) {
        String searchQuery = query;

        if (from != null && to != null) {
            searchQuery = transliterationService.transliterate(query, from, to);
        }

        List<SimilarNameDto> results = aipaService.searchSimilarNames(searchQuery);
        return ResponseEntity.ok(results);
    }

    @PutMapping("/refresh")
    public ResponseEntity<SimilarNameDto> refresh(@RequestBody RefreshRequestDto request) {
        Scraper scraper = new Scraper();
        SimilarNameDto similarNameDto = null;
        try {
            DataModel model = scraper.scrapeAipa(request.getLink());
            similarNameDto = this.aipaService.refreshCompanyData(request.getId(), model);
        } catch (Exception e) {
            similarNameDto = this.aipaRepository.findById(request.getId());
        }
        return ResponseEntity.ok(similarNameDto);
    }
}
