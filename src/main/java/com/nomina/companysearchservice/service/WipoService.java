package com.nomina.companysearchservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nomina.companysearchservice.datascrapper.DataModel;
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

    public SimilarNameDto refreshCompanyData(int id, DataModel dataModel) {
        try {
            this.wipoRepository.updateCompanyData(id, dataModel);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        SimilarNameDto data = wipoRepository.findById(id);

        return data;
    }

    /**
     * Simulated method to fetch fresh data from external source.
     * Replace this with actual API call or web scraping logic.
     */
    private SimilarNameDto fetchFreshDataFromSource(int id, String link) {
        // TODO: Implement actual data fetching logic
        // For now, return mock data with updated similarity score
        
        // Example: You might call an external API here
        // String freshMarkName = externalApiClient.fetchMarkName(link);
        
        // For demonstration, we'll just return updated data
        return new SimilarNameDto(
            id,
            "Updated Company Name", // Replace with actual fetched data
            0.95, // Updated similarity score
            link
        );
    }
}
