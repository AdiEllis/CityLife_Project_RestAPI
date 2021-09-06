package com.project.Controllers;

import com.project.Models.Colony;
import com.project.Models.Transaction;
import com.project.Objects.Entities.AuthUser;
import com.project.Objects.Entities.BasicResponseModel;
import com.project.Persist;
import com.project.Utils.Definitions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@Transactional
public class transactionsController extends BaseController {
    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {
    }


    @RequestMapping(value = "/transaction/add", method = RequestMethod.POST)
    public BasicResponseModel addTransaction(@RequestParam String date, @RequestParam String typeAction,
                                             @RequestParam String descriptionAction, @RequestParam int colonyID,
                                             AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        if (authUser.getAuthUserError() == null) {
            if (colonyID < 1) {
                basicResponseModel = new BasicResponseModel(Definitions.INVALID_ARGUMENT, Definitions.INVALID_ARGUMENT_MSG);
            } else {
                List<Colony> relevantColony = persist.getQuerySession().createQuery("FROM Colony WHERE id =: id")
                        .setParameter("id", colonyID).list();
                if (relevantColony.isEmpty()) {
                    basicResponseModel = new BasicResponseModel(Definitions.COLONY_NOT_EXISTS, Definitions.COLONY_NOT_EXISTS_MSG);
                } else if (relevantColony.size() > 1) {
                    basicResponseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
                } else {
                    Transaction transactionToAdd = new Transaction(false, date, typeAction, descriptionAction, colonyID);
                    persist.save(transactionToAdd);
                    basicResponseModel = new BasicResponseModel(transactionToAdd);
                }
            }
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/transaction/delete", method = RequestMethod.POST)
    public BasicResponseModel deleteTransaction(@RequestParam int id, @RequestParam boolean delete,
                                                AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        if (authUser.getAuthUserError() == null) {
            if (id < 1) {
                basicResponseModel = new BasicResponseModel(Definitions.INVALID_ARGUMENT, Definitions.INVALID_ARGUMENT_MSG);
            } else {
                List<Transaction> transactionsList = persist.getQuerySession().createQuery("FROM Transaction WHERE id = :id")
                        .setParameter("id", id)
                        .list();
                if (transactionsList.isEmpty()) {
                    basicResponseModel = new BasicResponseModel(Definitions.ARGUMENT_NOT_EXISTS, Definitions.ARGUMENT_NOT_EXISTS_MSG);
                } else if (transactionsList.size() > 1) {
                    basicResponseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
                } else {
                    Transaction transaction = transactionsList.get(0);
                    transaction.setDeleted(delete);
                    persist.save(transaction);
                    basicResponseModel = new BasicResponseModel(transaction);
                }
            }
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/transaction/getAll", method = RequestMethod.GET)
    public BasicResponseModel getAllTransactions(AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        if (authUser.getAuthUserError() == null) {
            List<Transaction> allTransactions = persist.getQuerySession().createQuery("FROM Transaction").list();
            basicResponseModel = new BasicResponseModel(allTransactions);
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/transaction/getTransaction", method = RequestMethod.POST)
    public BasicResponseModel getTransaction(@RequestParam int id,AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        if (authUser.getAuthUserError() == null) {
            if (id < 1) {
                basicResponseModel = new BasicResponseModel(Definitions.INVALID_ARGUMENT, Definitions.INVALID_ARGUMENT_MSG);
            } else {
                List<Transaction> transactionsList = persist.getQuerySession().createQuery("FROM Transaction WHERE id = :id")
                        .setParameter("id", id)
                        .list();
                if (transactionsList.isEmpty()) {
                    basicResponseModel = new BasicResponseModel(Definitions.ARGUMENT_NOT_EXISTS, Definitions.ARGUMENT_NOT_EXISTS_MSG);
                } else if (transactionsList.size() > 1) {
                    basicResponseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
                } else {
                    basicResponseModel = new BasicResponseModel(transactionsList.get(0));
                }
            }
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/transaction/update", method = RequestMethod.POST)
    public BasicResponseModel updateColony(@ModelAttribute("Transaction") Transaction transaction,
                                           AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        if (authUser.getAuthUserError() == null) {
            if (transaction.getOid() < 1 || transaction.getColonyID() < 1) {
                basicResponseModel = new BasicResponseModel(Definitions.INVALID_ARGUMENT, Definitions.INVALID_ARGUMENT_MSG);
            } else {
                if (transaction.objectIsEmpty()) {
                    basicResponseModel = new BasicResponseModel(Definitions.MISSING_FIELDS, Definitions.MISSING_FIELDS_MSG);
                } else {
                    List<Transaction> transactionsList = persist.getQuerySession().createQuery("FROM Transaction WHERE id = :id")
                            .setParameter("id", transaction.getOid())
                            .list();
                    List<Colony> relevantColony = persist.getQuerySession().createQuery("FROM Colony WHERE id =: id")
                            .setParameter("id", transaction.getColonyID()).list();
                    if (transactionsList.isEmpty() || relevantColony.isEmpty()) {
                        basicResponseModel = new BasicResponseModel(Definitions.ARGUMENT_NOT_EXISTS, Definitions.ARGUMENT_NOT_EXISTS_MSG);
                    } else if (transactionsList.size() > 1 || relevantColony.size() > 1) {
                        basicResponseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
                    } else {
                        Transaction oldTransaction = persist.loadObject(Transaction.class, transaction.getOid());
                        oldTransaction.setObject(transaction);
                        persist.save(oldTransaction);
                        basicResponseModel = new BasicResponseModel(transactionsList);
                    }
                }
            }
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }
        return basicResponseModel;
    }

}
