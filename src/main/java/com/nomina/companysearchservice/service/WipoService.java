package com.nomina.companysearchservice.service;

import com.nomina.companysearchservice.dto.SimilarNameDto;
import com.nomina.companysearchservice.repository.WipoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WipoService {

    private final WipoRepository wipoRepository;

    public WipoService(WipoRepository wipoRepository) {
        this.wipoRepository = wipoRepository;
    }

    public List<SimilarNameDto> searchSimilarNames(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Query cannot be empty");
        }
        return wipoRepository.findSimilarNames(query);
    }
}
