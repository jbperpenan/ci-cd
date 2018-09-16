package com.jekbp.cicd.persistence.impl;

import com.jekbp.cicd.model.Transaction;
import com.jekbp.cicd.persistence.TransactionPersistence;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TransactionPersistenceImpl extends PersistenceManager implements TransactionPersistence {

    @Override
    public Transaction persistTransaction(Transaction transaction) {
        return saveNewTransaction(transaction);
    }

    @Override
    public Map getTransactions() {
        return getCachedTransactions();
    }

    @Override
    public void clearCachedData() {
        emptyMap();
    }
}
