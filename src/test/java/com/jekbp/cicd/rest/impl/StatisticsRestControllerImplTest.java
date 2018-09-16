package com.jekbp.cicd.rest.impl;

import com.jekbp.cicd.model.Transaction;
import com.jekbp.cicd.model.TransactionStatistics;
import com.jekbp.cicd.rest.StatisticsRestController;
import com.jekbp.cicd.serivce.StatisticsService;
import com.jekbp.cicd.service.impl.StatisticsServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.when;

public class StatisticsRestControllerImplTest {

    private StatisticsRestController classUnderTest;
    private StatisticsService mockedStatisticsService;

    @Before
    public void setUp() {
        mockedStatisticsService = Mockito.mock(StatisticsServiceImpl.class);

        classUnderTest = new StatisticsRestControllerImpl(mockedStatisticsService);
    }

    @Test
    public void getTransactionStatisticsReturndResponse() {
        when(mockedStatisticsService.getStatistics()).thenReturn(new TransactionStatistics());

        TransactionStatistics response = classUnderTest.getTransactionStatistics();

        assertTrue(response != null);
    }

    @Test
    public void clearReturnStatus200andResponse() {
        TransactionStatistics transactionStatistics = new TransactionStatistics();

        when(mockedStatisticsService.getStatistics()).thenReturn(transactionStatistics);

        Response response = classUnderTest.clear();

        assertTrue(response != null);
        assertTrue(response.getStatus() == 200);
    }
}
