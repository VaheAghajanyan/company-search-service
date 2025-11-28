package com.nomina.companysearchservice.service;

import com.nomina.companysearchservice.dto.CompanyData;
import com.nomina.companysearchservice.model.CompanySimilarityResult;
import com.nomina.companysearchservice.repository.CompanyRepository;
import dev.brachtendorf.jimagehash.hash.Hash;
import dev.brachtendorf.jimagehash.hashAlgorithms.DifferenceHash;
import dev.brachtendorf.jimagehash.hashAlgorithms.PerceptiveHash;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageComparisonService {

    private final CompanyRepository companyRepository;

    public ImageComparisonService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<CompanySimilarityResult> findSimilarImagesAipa(MultipartFile uploadedImage) throws IOException {
        // 1. Convert uploaded image to BufferedImage
        BufferedImage image = ImageIO.read(uploadedImage.getInputStream());

        // 2. Calculate Hash objects for uploaded image
        PerceptiveHash pHash = new PerceptiveHash(128);
        DifferenceHash dHash = new DifferenceHash(128, DifferenceHash.Precision.Triple);

        Hash uploadedPerceptiveHash = pHash.hash(image);
        Hash uploadedDifferenceHash = dHash.hash(image);

        // Get BigInteger values from uploaded image hashes
        BigInteger uploadedPHashValue = uploadedPerceptiveHash.getHashValue();
        BigInteger uploadedDHashValue = uploadedDifferenceHash.getHashValue();

        // 3. Get all companies from database (only aipa)
        List<CompanyData> allCompanies = companyRepository.getAllAipaCompaniesWithHashes();

        // 4. Compare and calculate similarity scores
        List<CompanySimilarityResult> results = new ArrayList<>();

        for (CompanyData company : allCompanies) {
            BigInteger companyPerceptiveHashValue = company.getPerceptiveHash();
            BigInteger companyDifferenceHashValue = company.getDifferenceHash();

            // Calculate similarity using normalized Hamming distance on BigInteger
            double perceptiveSimilarity = calculateNormalizedSimilarity(uploadedPHashValue, companyPerceptiveHashValue, 128);
            double differenceSimilarity = calculateNormalizedSimilarity(uploadedDHashValue, companyDifferenceHashValue, 128);

            // Only include results where BOTH similarities are >= 0.6 (60%)
            if (perceptiveSimilarity >= 0.6 || differenceSimilarity >= 0.6) {
                CompanySimilarityResult result = new CompanySimilarityResult(
                        company.getId(),
                        company.getMarkName(),
                        company.getImagePath(),
                        company.getLink(),
                        perceptiveSimilarity,
                        differenceSimilarity
                );

                results.add(result);
            }
        }

        // 5. Sort by perceptive similarity first (highest), then by difference similarity (highest)
        results.sort(Comparator
                .comparingDouble(CompanySimilarityResult::getPerceptiveSimilarity).reversed()
                .thenComparing(Comparator.comparingDouble(CompanySimilarityResult::getDifferenceSimilarity).reversed())
        );

        // 6. Return top 20
        return results.stream()
                .limit(20)
                .collect(Collectors.toList());
    }

    public List<CompanySimilarityResult> findSimilarImagesWipo(MultipartFile uploadedImage) throws IOException {
        // 1. Convert uploaded image to BufferedImage
        BufferedImage image = ImageIO.read(uploadedImage.getInputStream());

        // 2. Calculate Hash objects for uploaded image
        PerceptiveHash pHash = new PerceptiveHash(128);
        DifferenceHash dHash = new DifferenceHash(128, DifferenceHash.Precision.Triple);

        Hash uploadedPerceptiveHash = pHash.hash(image);
        Hash uploadedDifferenceHash = dHash.hash(image);

        // Get BigInteger values from uploaded image hashes
        BigInteger uploadedPHashValue = uploadedPerceptiveHash.getHashValue();
        BigInteger uploadedDHashValue = uploadedDifferenceHash.getHashValue();

        // 3. Get all companies from database (only wipo)
        List<CompanyData> allCompanies = companyRepository.getAllWipoCompaniesWithHashes();

        // 4. Compare and calculate similarity scores
        List<CompanySimilarityResult> results = new ArrayList<>();

        for (CompanyData company : allCompanies) {
            BigInteger companyPerceptiveHashValue = company.getPerceptiveHash();
            BigInteger companyDifferenceHashValue = company.getDifferenceHash();

            // Calculate similarity using normalized Hamming distance on BigInteger
            double perceptiveSimilarity = calculateNormalizedSimilarity(uploadedPHashValue, companyPerceptiveHashValue, 128);
            double differenceSimilarity = calculateNormalizedSimilarity(uploadedDHashValue, companyDifferenceHashValue, 128);

            // Only include results where BOTH similarities are >= 0.75 (75%)
            if (perceptiveSimilarity >= 0.6 || differenceSimilarity >= 0.6) {
                CompanySimilarityResult result = new CompanySimilarityResult(
                        company.getId(),
                        company.getMarkName(),
                        company.getImagePath(),
                        company.getLink(),
                        perceptiveSimilarity,
                        differenceSimilarity
                );

                results.add(result);
            }
        }

        // 5. Sort by perceptive similarity first (highest), then by difference similarity (highest)
        results.sort(Comparator
                .comparingDouble(CompanySimilarityResult::getPerceptiveSimilarity).reversed()
                .thenComparing(Comparator.comparingDouble(CompanySimilarityResult::getDifferenceSimilarity).reversed())
        );

        // 6. Return top 20
        return results.stream()
                .limit(20)
                .collect(Collectors.toList());
    }

    /**
     * Calculate normalized similarity between two BigInteger hashes
     * This mimics: 1.0 - hash1.normalizedHammingDistance(hash2)
     *
     * @param hash1 First hash value
     * @param hash2 Second hash value
     * @param bitResolution Number of bits in the hash (e.g., 128)
     * @return Similarity score from 0.0 to 1.0
     */
    private double calculateNormalizedSimilarity(BigInteger hash1, BigInteger hash2, int bitResolution) {
        // XOR to find differing bits
        BigInteger xor = hash1.xor(hash2);

        // Count differing bits (Hamming distance)
        int hammingDistance = xor.bitCount();

        // Normalize by dividing by total bits
        double normalizedDistance = (double) hammingDistance / bitResolution;

        // Convert distance to similarity: 1.0 - distance
        return 1.0 - normalizedDistance;
    }
}