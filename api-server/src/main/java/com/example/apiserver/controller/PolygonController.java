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
            Polygon savedPolygon = polygonService.savePolygon(polygon);
            return ResponseEntity.ok(savedPolygon);
        } catch (Exception e) {
            System.out.println("Error saving polygon:");
            e.printStackTrace(); // This will appear in Koyeb logs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public List<Polygon> getAllPolygons() throws ExecutionException, InterruptedException {
        return polygonService.getAllPolygons();
    }
}
