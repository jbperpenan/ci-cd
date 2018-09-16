package com.jekbp.cicd.rest.impl;

import com.jekbp.cicd.model.TransactionStatistics;
import com.jekbp.cicd.rest.StatisticsRestController;
import com.jekbp.cicd.serivce.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.Response;


@RestController
public class StatisticsRestControllerImpl implements StatisticsRestController {

    @Autowired
    private StatisticsService statisticsService;

    public StatisticsRestControllerImpl(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/statistics")
    @Override
    public TransactionStatistics getTransactionStatistics() {
        return statisticsService.getStatistics();
    }

    @GetMapping("/clear")
    @Override
    public Response clear() {
        statisticsService.clearData();
        return Response.status(HttpStatus.OK.value()).build();
    }
}
