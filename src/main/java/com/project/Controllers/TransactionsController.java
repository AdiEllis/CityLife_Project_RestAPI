package com.project.Controllers;

import com.project.Models.Colony;
import com.project.Models.Transaction;
import com.project.Objects.Entities.AuthUser;
import com.project.Objects.Entities.BasicResponseModel;
import com.project.Persist;
import com.project.Utils.DateUtils;
import com.project.Utils.Definitions;
import com.project.Utils.IdValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@Transactional
public class TransactionsController extends BaseController {
    @Autowired
    private Persist persist;
    @Autowired
    private IdValidator idValidator;
    @Autowired
    private DateUtils dateUtils;

    @PostConstruct
    public void init() {
    }


    @RequestMapping(value = "/transaction/add", method = RequestMethod.POST)
    public BasicResponseModel addTransaction(@ModelAttribute("Transaction") Transaction transaction,
                                             AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        BasicResponseModel isValidColonyID = idValidator.isValidId(transaction.getColonyID(), Colony.class);
        if (authUser.getAuthUserError() == null) {
            if (transaction.objectIsEmpty()) {
                basicResponseModel = new BasicResponseModel(Definitions.MISSING_FIELDS, Definitions.MISSING_FIELDS_MSG);
            } else {
                if (isValidColonyID != null) {
                    basicResponseModel = isValidColonyID;
                } else {
                    if (!dateUtils.isValidDatePattern(transaction.getDate())) {
                        basicResponseModel = new BasicResponseModel(Definitions.INVALID_DATE, Definitions.INVALID_DATE_MSG);
                    } else {
                        transaction.setDate(dateUtils.convertDate2TS(transaction.getDate()));
                        persist.save(transaction);
                        basicResponseModel = new BasicResponseModel(transaction);
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

    @RequestMapping(value = "/transaction/delete", method = RequestMethod.POST)
    public BasicResponseModel deleteTransaction(@RequestParam int id, @RequestParam boolean delete,
                                                AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        BasicResponseModel isValidId = idValidator.isValidId(id, Transaction.class);
        if (authUser.getAuthUserError() == null) {
            if (isValidId != null) {
                basicResponseModel = isValidId;
            } else {
                List<Transaction> transactionsList = persist.getQuerySession().createQuery("FROM Transaction WHERE id = :id")
                        .setParameter("id", id)
                        .list();
                Transaction transaction = transactionsList.get(0);
                transaction.setDeleted(delete);
                persist.save(transaction);
                basicResponseModel = new BasicResponseModel(transaction);
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
    public BasicResponseModel getTransaction(@RequestParam int id, AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        BasicResponseModel isValidId = idValidator.isValidId(id, Transaction.class);
        if (authUser.getAuthUserError() == null) {
            if (isValidId != null) {
                basicResponseModel = isValidId;
            } else {
                List<Transaction> transactionsList = persist.getQuerySession().createQuery("FROM Transaction WHERE id = :id")
                        .setParameter("id", id)
                        .list();
                basicResponseModel = new BasicResponseModel(transactionsList.get(0));
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
        BasicResponseModel isValidId = idValidator.isValidId(transaction.getOid(), Transaction.class);
        BasicResponseModel isValidColonyID = idValidator.isValidId(transaction.getColonyID(), Colony.class);
        if (authUser.getAuthUserError() == null) {
            if (transaction.objectIsEmpty()) {
                basicResponseModel = new BasicResponseModel(Definitions.MISSING_FIELDS, Definitions.MISSING_FIELDS_MSG);
            } else {
                if (isValidId != null) {
                    basicResponseModel = isValidId;
                } else {
                    if (isValidColonyID != null) {
                        basicResponseModel = isValidColonyID;
                    } else {
                        if (!dateUtils.isValidDatePattern(transaction.getDate())) {
                            basicResponseModel = new BasicResponseModel(Definitions.INVALID_DATE, Definitions.INVALID_DATE_MSG);
                        } else {
                            List<Transaction> transactionsList = persist.getQuerySession().createQuery("FROM Transaction WHERE id = :id")
                                    .setParameter("id", transaction.getOid())
                                    .list();
                            Transaction oldTransaction = persist.loadObject(Transaction.class, transaction.getOid());
                            transaction.setDate(dateUtils.convertDate2TS(transaction.getDate()));
                            oldTransaction.setObject(transaction);
                            persist.save(oldTransaction);
                            basicResponseModel = new BasicResponseModel(transactionsList);
                        }
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
