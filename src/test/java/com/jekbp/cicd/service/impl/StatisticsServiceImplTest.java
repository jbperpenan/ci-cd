package com.jekbp.cicd.service.impl;

import com.jekbp.cicd.model.Transaction;
import com.jekbp.cicd.model.TransactionStatistics;
import com.jekbp.cicd.persistence.TransactionPersistence;
import com.jekbp.cicd.serivce.StatisticsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StatisticsServiceImplTest {

    private StatisticsService classUnderTest;
    private TransactionPersistence mockedTransactionPersistence;
    private Transaction transaction;
    private Map transactionMap;

    @Before
    public void setUp() {
        mockedTransactionPersistence = Mockito.mock(TransactionPersistence.class);
        transaction = new Transaction(100, Instant.now().toEpochMilli());
        transactionMap = new ConcurrentHashMap<String, Transaction>();

        transactionMap.put(transaction.getTimestamp(), transaction);

        classUnderTest = new StatisticsServiceImpl(mockedTransactionPersistence);
    }

    @Test
    public void getStatisticsShouldReturnCorrectFieldValuesIfDataExists () {

        when(mockedTransactionPersistence.getTransactions()).thenReturn(transactionMap);

        TransactionStatistics transactionStatistics =  classUnderTest.getStatistics();
        assertTrue(transactionStatistics.getSum() ==  100 );
        assertTrue(transactionStatistics.getAvg() ==  100 );
        assertTrue(transactionStatistics.getMax() ==  100 );
        assertTrue(transactionStatistics.getMin() ==  100 );
        assertTrue(transactionStatistics.getCount() ==  1 );
    }

    @Test
    public void getStatisticsShouldReturnCorrectFieldValuesIfNoDataExists () {

        when(mockedTransactionPersistence.getTransactions()).thenReturn(new ConcurrentHashMap<String, Transaction>());

        TransactionStatistics transactionStatistics =  classUnderTest.getStatistics();

        assertTrue(transactionStatistics.getSum() ==  0 );
        assertTrue(transactionStatistics.getAvg() ==  0 );
        assertTrue(transactionStatistics.getMax() ==  0 );
        assertTrue(transactionStatistics.getMin() ==  0 );
        assertTrue(transactionStatistics.getCount() ==  0 );
    }

    @Test
    public void clearShouldClearMapContentAndReturnNothing() {

        when(mockedTransactionPersistence.getTransactions()).thenReturn(transactionMap);

        classUnderTest.clearData();

        verify(mockedTransactionPersistence, times(1)).clearCachedData();
    }
}