package com.jekbp.cicd.service.impl;

import com.jekbp.cicd.exception.OldTransactionException;
import com.jekbp.cicd.model.Transaction;
import com.jekbp.cicd.persistence.TransactionPersistence;
import com.jekbp.cicd.serivce.TransactionService;
import com.jekbp.cicd.service.impl.TransactionServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Instant;

import static junit.framework.TestCase.assertNull;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.when;

public class TransactionServiceImplTest {

    private TransactionService classUnderTest;
    private TransactionPersistence mockedTransactionPersistence;
    private Transaction mockedTransaction;


    @Before
    public void setUp() {
        mockedTransactionPersistence = Mockito.mock(TransactionPersistence.class);
        mockedTransaction = Mockito.mock(Transaction.class);

        classUnderTest = new TransactionServiceImpl(mockedTransactionPersistence);
    }

    @Test
    public void createTransactionReturnNotNullIfTransactionNotOlderThan60seconds() throws OldTransactionException {

        when(mockedTransactionPersistence.persistTransaction(Mockito.any(Transaction.class)))
                .thenReturn(mockedTransaction);

        when(mockedTransaction.getTimestamp())
                .thenReturn(Instant.now().toEpochMilli());

        assertNotNull(classUnderTest.createTransaction(mockedTransaction));
    }

    @Test(expected = OldTransactionException.class)
    public void createTransactionThrowsExceptionIfTransactionOlderThan60seconds() throws OldTransactionException {

        when(mockedTransactionPersistence.persistTransaction(mockedTransaction)).thenReturn(mockedTransaction);

        when(mockedTransaction.getTimestamp()).thenReturn(Instant.ofEpochMilli(1478192204000L).toEpochMilli());

        assertNull(classUnderTest.createTransaction(mockedTransaction));
    }
}