package com.jekbp.cicd.service.impl;

import com.jekbp.cicd.exception.OldTransactionException;
import com.jekbp.cicd.model.Transaction;
import com.jekbp.cicd.serivce.TransactionService;
import com.jekbp.cicd.persistence.TransactionPersistence;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionPersistence transactionPersistence;

    public TransactionServiceImpl(TransactionPersistence transactionPersistence) {
        this.transactionPersistence = transactionPersistence;
    }

    @Override
    public Transaction createTransaction(Transaction transaction) throws OldTransactionException {

        if (isOlderThan60Seconds(transaction.getTimestamp())) {
            saveNewTransaction(transaction);

            throw new OldTransactionException();
        }

        return saveNewTransaction(transaction);
    }

    private boolean isOlderThan60Seconds(long timeStamp) {
        Instant currentTimeStamp = Instant.now();
        Instant transactionTimeStamp = Instant.ofEpochMilli(timeStamp);
        Duration duration = Duration.between(transactionTimeStamp,currentTimeStamp);

        return duration.compareTo(Duration.ofSeconds(TransactionPersistence.LIMIT_IN_SECONDS)) > NumberUtils.INTEGER_ZERO;
    }

    private  Transaction saveNewTransaction(Transaction transaction) {
        return transactionPersistence.persistTransaction(new Transaction(transaction.getAmount(), transaction.getTimestamp()));
    }
}
