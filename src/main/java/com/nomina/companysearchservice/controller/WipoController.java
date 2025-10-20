package com.nomina.companysearchservice.controller;

import com.nomina.companysearchservice.dto.SimilarNameDto;
import com.nomina.companysearchservice.service.WipoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/wipo")
public class WipoController {

    private final WipoService wipoService;

    public WipoController(WipoService wipoService) {
        this.wipoService = wipoService;
    }

    @GetMapping("/search-by-name")
    public ResponseEntity<List<SimilarNameDto>> search(@RequestParam("query") String query) {
        List<SimilarNameDto> results = wipoService.searchSimilarNames(query);
        return ResponseEntity.ok(results);
    }
}
