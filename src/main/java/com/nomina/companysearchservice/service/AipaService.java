package com.nomina.companysearchservice.service;

import com.nomina.companysearchservice.dto.SimilarNameDto;
import com.nomina.companysearchservice.repository.AipaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AipaService {

    private final AipaRepository aipaRepository;

    public AipaService(AipaRepository aipaRepository) {
        this.aipaRepository = aipaRepository;
    }

    public List<SimilarNameDto> searchSimilarNames(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Query cannot be empty");
        }
        return aipaRepository.findSimilarNames(query);
    }
}
