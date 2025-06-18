package com.example.apiserver.controller;

import com.example.apiserver.model.Polygon;
import com.example.apiserver.service.PolygonService;
import org.springframework.beans.factory.annotation.Autowired;
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
        Polygon savedPolygon = polygonService.savePolygon(polygon);
        return ResponseEntity.ok(savedPolygon);
    }

    @GetMapping
    public List<Polygon> getAllPolygons() throws ExecutionException, InterruptedException {
        return polygonService.getAllPolygons();
    }
}
