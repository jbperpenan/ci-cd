package com.jekbp.cicd.service.impl;

import com.jekbp.cicd.model.Transaction;
import com.jekbp.cicd.model.TransactionStatistics;
import com.jekbp.cicd.serivce.StatisticsService;
import com.jekbp.cicd.persistence.TransactionPersistence;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.*;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private TransactionPersistence transactionPersistence;

    public StatisticsServiceImpl(TransactionPersistence transactionPersistence) {
        this.transactionPersistence = transactionPersistence;
    }

    @Override
    public TransactionStatistics getStatistics() {
        Map<String, Transaction> transactions = transactionPersistence.getTransactions();

        List<Double> amountList = new ArrayList<>();
        for(Transaction transaction: transactions.values()){

            long cachedTransactionTimestamp = transaction.getTimestamp();
            Instant currentTimestamp = Instant.now();
            Instant transactionTimestamp = Instant.ofEpochMilli(cachedTransactionTimestamp);
            Duration duration = Duration.between(transactionTimestamp,currentTimestamp);

            if(duration.compareTo(Duration.ofSeconds(TransactionPersistence.LIMIT_IN_SECONDS)) < NumberUtils.INTEGER_ZERO) {
                amountList.add(transaction.getAmount());
            }
        }

        DoubleSummaryStatistics stats = amountList.stream().collect(Collectors.summarizingDouble(Double::doubleValue));

        return new TransactionStatistics(stats.getSum(),
                                stats.getAverage(),
                                stats.getMax() != Double.NEGATIVE_INFINITY ? stats.getMax(): NumberUtils.INTEGER_ZERO,
                                stats.getMin() != Double.POSITIVE_INFINITY ? stats.getMin(): NumberUtils.INTEGER_ZERO,
                                amountList.size());
    }

    @Override
    public void clearData() {
        transactionPersistence.clearCachedData();
    }
}
