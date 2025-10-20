package com.nomina.companysearchservice.controller;

import com.nomina.companysearchservice.dto.SimilarNameDto;
import com.nomina.companysearchservice.service.AipaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/aipa")
public class AipaController {

    private final AipaService aipaService;

    public AipaController(AipaService aipaService) {
        this.aipaService = aipaService;
    }

    @GetMapping("/search-by-name")
    public ResponseEntity<List<SimilarNameDto>> search(@RequestParam("query") String query) {
        List<SimilarNameDto> results = aipaService.searchSimilarNames(query);
        return ResponseEntity.ok(results);
    }
}
