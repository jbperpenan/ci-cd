package com.jekbp.cicd.rest;

import com.jekbp.cicd.model.TransactionStatistics;

import javax.ws.rs.core.Response;

public interface StatisticsRestController {
    TransactionStatistics getTransactionStatistics();
    Response clear();
}
