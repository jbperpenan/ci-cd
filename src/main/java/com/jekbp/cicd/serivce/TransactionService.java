package com.jekbp.cicd.serivce;

import com.jekbp.cicd.exception.OldTransactionException;
import com.jekbp.cicd.model.Transaction;

public interface TransactionService {
    Transaction createTransaction (Transaction newTransaction) throws OldTransactionException;
}
