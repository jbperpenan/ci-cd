package com.jekbp.cicd.persistence;

import com.jekbp.cicd.model.Transaction;

import java.util.Map;

public interface TransactionPersistence {
    Transaction persistTransaction (Transaction transaction);
    Map getTransactions();
    void clearCachedData();

    int LIMIT_IN_SECONDS = 60;
}
