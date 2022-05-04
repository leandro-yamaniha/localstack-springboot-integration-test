package com.example.dynamo.service;

import com.example.dynamo.dto.CostumerDTO;
import com.example.dynamo.model.Costumer;

import java.util.List;

public interface CostumerService {
    Costumer saveCostumer(CostumerDTO costumerDTO);
    List<Costumer> findAllCostumers();
    List<Costumer> findByCompanyName(String companyName);
    Costumer updateCostumer(String companyDocumentNumber, CostumerDTO costumerDTO);
    Costumer disableCostumer(String companyDocumentNumber);
}
