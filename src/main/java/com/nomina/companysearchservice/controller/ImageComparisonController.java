package com.nomina.companysearchservice.controller;

import com.nomina.companysearchservice.model.CompanySimilarityResult;
import com.nomina.companysearchservice.service.ImageComparisonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageComparisonController {

    private final ImageComparisonService imageComparisonService;

    public ImageComparisonController(ImageComparisonService imageComparisonService) {
        this.imageComparisonService = imageComparisonService;
    }

    @PostMapping("/compare-aipa")
    public ResponseEntity<List<CompanySimilarityResult>> compareImageAipa(
            @RequestParam("image") MultipartFile image) {

        try {
            List<CompanySimilarityResult> similarImages =
                    imageComparisonService.findSimilarImagesAipa(image);

            return ResponseEntity.ok(similarImages);

        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/compare-wipo")
    public ResponseEntity<List<CompanySimilarityResult>> compareImageWipo(
            @RequestParam("image") MultipartFile image) {

        try {
            List<CompanySimilarityResult> similarImages =
                    imageComparisonService.findSimilarImagesWipo(image);

            return ResponseEntity.ok(similarImages);

        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
