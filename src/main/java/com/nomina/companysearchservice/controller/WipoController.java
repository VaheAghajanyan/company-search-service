package com.nomina.companysearchservice.controller;

import com.nomina.companysearchservice.datascrapper.DataModel;
import com.nomina.companysearchservice.datascrapper.Scraper;
import com.nomina.companysearchservice.dto.RefreshRequestDto;
import com.nomina.companysearchservice.dto.SimilarNameDto;
import com.nomina.companysearchservice.repository.WipoRepository;
import com.nomina.companysearchservice.service.TransliterationService;
import com.nomina.companysearchservice.service.WipoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/wipo")
public class WipoController {

    private final WipoService wipoService;
    private final WipoRepository wipoRepository;
    private final TransliterationService transliterationService;

    public WipoController(WipoService wipoService, WipoRepository wipoRepository, TransliterationService transliterationService) {
        this.wipoService = wipoService;
        this.wipoRepository = wipoRepository;
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

        List<SimilarNameDto> results = wipoService.searchSimilarNames(searchQuery);
        return ResponseEntity.ok(results);
    }

    @PutMapping("/refresh")
    public ResponseEntity<SimilarNameDto> refresh(@RequestBody RefreshRequestDto request) {
        Scraper scraper = new Scraper();
        SimilarNameDto similarNameDto = null;
        try {
            DataModel model = scraper.scrapeWipo(request.getLink());
            similarNameDto = this.wipoService.refreshCompanyData(request.getId(), model);
        } catch (Exception e) {
            similarNameDto = this.wipoRepository.findById(request.getId());
        }
        return ResponseEntity.ok(similarNameDto);
    }
}
