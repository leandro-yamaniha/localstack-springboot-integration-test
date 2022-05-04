package com.example.dynamo.controller;

import com.example.dynamo.dto.CostumerDTO;
import com.example.dynamo.model.Costumer;
import com.example.dynamo.service.CostumerService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class AppController {

    private final CostumerService costumerService;

    public AppController(CostumerService costumerService) {
        this.costumerService = costumerService;
    }

    @PostMapping("costumer")
    public ResponseEntity<Costumer> newCostumer(@Valid @RequestBody CostumerDTO costumerDTO) {
        return new ResponseEntity(costumerService.saveCostumer(costumerDTO), HttpStatus.OK);
    }

    @GetMapping("costumer")
    public ResponseEntity<List<Costumer>> findCostumerByName(@Param("companyName") String companyName) {
        return ResponseEntity.ok(costumerService.findByCompanyName(companyName));
    }

    @GetMapping("costumer/all")
    public ResponseEntity<List<Costumer>> allCostumers() {
        return ResponseEntity.ok(costumerService.findAllCostumers());
    }

    @PutMapping("costumer/{id}")
    public ResponseEntity<Costumer> updateCostumer(@PathParam("id") String id,  @Valid @RequestBody CostumerDTO costumerDTO) {
        return ResponseEntity.ok(costumerService.updateCostumer(id, costumerDTO));
    }

    @DeleteMapping("costumer/{companyName}")
    public ResponseEntity<Costumer> disableCostumer(@PathVariable("companyName") String companyName) {
        return ResponseEntity.ok(costumerService.disableCostumer(companyName));
    }

}