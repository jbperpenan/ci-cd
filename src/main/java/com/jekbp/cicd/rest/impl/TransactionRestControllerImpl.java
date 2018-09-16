package com.jekbp.cicd.rest.impl;

import com.jekbp.cicd.exception.OldTransactionException;
import com.jekbp.cicd.model.Transaction;
import com.jekbp.cicd.rest.TransactionRestController;
import com.jekbp.cicd.serivce.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.Response;

@RestController
@RequestMapping("/transactions")
public class TransactionRestControllerImpl implements TransactionRestController {

    @Autowired
    private TransactionService transactionService;

    public TransactionRestControllerImpl(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @Override
    public Response createTransaction(@RequestBody Transaction transaction) {

        try {
            transactionService.createTransaction(transaction);

            return Response.status(HttpStatus.CREATED.value()).build();

        } catch (OldTransactionException e) {
            return Response.status(HttpStatus.NO_CONTENT.value()).build();
        }
    }
}
