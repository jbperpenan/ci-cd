package com.jekbp.cicd.rest;

import com.jekbp.cicd.model.Transaction;

import javax.ws.rs.core.Response;

public interface TransactionRestController {
    Response createTransaction(Transaction transaction);
}
