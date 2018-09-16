package com.jekbp.cicd.persistence.impl;

import com.jekbp.cicd.model.Transaction;

import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class PersistenceManager {
    private Map transactions = new ConcurrentHashMap<String, Transaction>();

    protected Transaction saveNewTransaction(Transaction transaction) {
        transactions.put(transaction.getTimestamp(), transaction);

        return (Transaction)transactions.get(transaction.getTimestamp());
    }

    protected Map getCachedTransactions() {
        return transactions;
    }

    protected void emptyMap() {
        transactions.clear();
    }
}
