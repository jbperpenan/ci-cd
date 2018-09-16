package com.jekbp.cicd.rest.impl;

import com.jekbp.cicd.exception.OldTransactionException;
import com.jekbp.cicd.model.Transaction;
import com.jekbp.cicd.rest.TransactionRestController;
import com.jekbp.cicd.rest.impl.TransactionRestControllerImpl;
import com.jekbp.cicd.serivce.TransactionService;
import com.jekbp.cicd.service.impl.TransactionServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.when;


public class TransactionRestControllerImplTest {

    private TransactionRestController classUnderTest;
    private TransactionService mockedTransactionService;
    private Transaction mockedTransaction;

    @Before
    public void setUp() {
        mockedTransactionService = Mockito.mock(TransactionServiceImpl.class);
        mockedTransaction = Mockito.mock(Transaction.class);

        classUnderTest = new TransactionRestControllerImpl(mockedTransactionService);
    }

    @Test
    public void createTransactionReturnStatus201IfTransactionNotOlderThan60seconds() throws OldTransactionException {

        when(mockedTransactionService.createTransaction(mockedTransaction))
                .thenReturn(mockedTransaction);

        Response response = classUnderTest.createTransaction(mockedTransaction);

        assertTrue(response.getStatus() == HttpStatus.CREATED.value());
    }

    @Test
    public void createTransactionReturnStatus204IfTransactionOlderThan60seconds() throws OldTransactionException {

        when(mockedTransactionService.createTransaction(Mockito.any(Transaction.class)))
                .thenThrow(OldTransactionException.class);

        Response response = classUnderTest.createTransaction(mockedTransaction);

        assertTrue(response.getStatus() == HttpStatus.NO_CONTENT.value());
    }
}
