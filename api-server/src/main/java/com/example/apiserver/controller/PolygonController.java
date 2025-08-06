package com.example.apiserver.controller;

import com.example.apiserver.model.Polygon;
import com.example.apiserver.service.PolygonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/polygons")
public class PolygonController {

    @Autowired
    private PolygonService polygonService;

    @PostMapping
    public ResponseEntity<Polygon> savePolygon(@RequestBody Polygon polygon) {
        try {
            System.out.println("Received polygon: " + polygon);
            if (polygon == null || polygon.getPoints() == null || polygon.getPoints().isEmpty()) {
                System.out.println("Invalid polygon data");
                return ResponseEntity.badRequest().build();
            }

            Polygon savedPolygon = polygonService.savePolygon(polygon);
            System.out.println("Polygon saved: " + savedPolygon);
            return ResponseEntity.ok(savedPolygon);
        } catch (Exception e) {
            System.out.println("Error saving polygon:");
            e.printStackTrace(); // This should go to Koyeb logs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping
    public List<Polygon> getAllPolygons() throws ExecutionException, InterruptedException {
        return polygonService.getAllPolygons();
    }

}
