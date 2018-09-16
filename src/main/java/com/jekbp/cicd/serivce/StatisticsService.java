package com.jekbp.cicd.serivce;

import com.jekbp.cicd.model.TransactionStatistics;

public interface StatisticsService {
    TransactionStatistics getStatistics();
    void clearData();
}
