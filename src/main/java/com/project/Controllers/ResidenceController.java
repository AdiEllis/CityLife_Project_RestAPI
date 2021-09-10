package com.project.Controllers;

import com.project.Models.Residence;
import com.project.Objects.Entities.AuthUser;
import com.project.Objects.Entities.BasicResponseModel;
import com.project.Persist;
import com.project.Utils.DateValidator;
import com.project.Utils.Definitions;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

import java.util.List;

import static org.apache.commons.validator.GenericValidator.isDate;
/**/

@RestController
@Transactional
public class ResidenceController extends BaseController {
    @Autowired
    private Persist persist;
    @Autowired
    private DateValidator dateValidator;

    @PostConstruct
    public void init() {
    }

    @RequestMapping(value = "/residence/add", method = RequestMethod.POST)
    public BasicResponseModel addResidence(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String birthDate,
                                           @RequestParam String phone, @RequestParam String id, @RequestParam String colonyID,
                                           @RequestParam String streetID, @RequestParam String houseNumber,
                                           AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        if (authUser.getAuthUserError() == null) {
            if (!dateValidator.isValid(birthDate)) {
                basicResponseModel = new BasicResponseModel(Definitions.INVALID_DATE, Definitions.INVALID_DATE_MSG);
            } else {
                Residence residenceToAdd = new Residence(firstName, lastName, birthDate, phone, id, colonyID, streetID, houseNumber, false);
                persist.save(residenceToAdd);
                basicResponseModel = new BasicResponseModel(residenceToAdd);
            }
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }

        return basicResponseModel;
    }

    @RequestMapping(value = "/residence/delete", method = RequestMethod.POST)
    public BasicResponseModel deleteResidence(@RequestParam int oid, @RequestParam boolean delete,
                                              AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        if (authUser.getAuthUserError() == null) {
            if (oid < 1) {
                basicResponseModel = new BasicResponseModel(Definitions.INVALID_ARGUMENT, Definitions.INVALID_ARGUMENT_MSG);
            } else {
                List<Residence> residencesList = persist.getQuerySession().createQuery("FROM Residence WHERE oid = :oid")
                        .setParameter("oid", oid)
                        .list();
                if (residencesList.isEmpty()) {
                    basicResponseModel = new BasicResponseModel(Definitions.ARGUMENT_NOT_EXISTS, Definitions.ARGUMENT_NOT_EXISTS_MSG);
                } else if (residencesList.size() > 1) {
                    basicResponseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
                } else {
                    Residence residence = residencesList.get(0);
                    residence.setDeleted(delete);
                    persist.save(residence);
                    basicResponseModel = new BasicResponseModel(residence);
                }
            }
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/residence/getAll", method = RequestMethod.GET)
    public BasicResponseModel getAllResidences(AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        if (authUser.getAuthUserError() == null) {
            List<Residence> residencesList = persist.getQuerySession().createQuery("FROM Residence")
                    .list();
            basicResponseModel = new BasicResponseModel(residencesList);
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }
        return basicResponseModel;
    }


    @RequestMapping(value = "/residence/getResidence", method = RequestMethod.GET)
    public BasicResponseModel getResidence(@RequestParam int oid, AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        if (authUser.getAuthUserError() == null) {
            if (oid < 1) {
                basicResponseModel = new BasicResponseModel(Definitions.INVALID_ARGUMENT, Definitions.INVALID_ARGUMENT_MSG);
            } else {
                List<Residence> residencesList = persist.getQuerySession().createQuery("FROM Residence WHERE oid = :oid")
                        .setParameter("oid", oid)
                        .list();
                if (residencesList.isEmpty()) {
                    basicResponseModel = new BasicResponseModel(Definitions.ARGUMENT_NOT_EXISTS, Definitions.ARGUMENT_NOT_EXISTS_MSG);
                } else if (residencesList.size() > 1) {
                    basicResponseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
                } else {
                    basicResponseModel = new BasicResponseModel(residencesList);
                }
            }
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/residence/update", method = RequestMethod.POST)
    public BasicResponseModel updateResidence(@ModelAttribute("Residence") Residence residence,
                                              AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        if (authUser.getAuthUserError() == null) {
            if (residence.getOid() < 1) {
                basicResponseModel = new BasicResponseModel(Definitions.INVALID_ARGUMENT, Definitions.INVALID_ARGUMENT_MSG);
            } else {
                if (residence.objectIsEmpty()) {
                    basicResponseModel = new BasicResponseModel(Definitions.MISSING_FIELDS, Definitions.MISSING_FIELDS_MSG);
                } else {
                    List<Residence> residencesList = persist.getQuerySession().createQuery("FROM Residence WHERE oid = :oid")
                            .setParameter("oid", residence.getOid())
                            .list();
                    if (residencesList.isEmpty()) {
                        basicResponseModel = new BasicResponseModel(Definitions.ARGUMENT_NOT_EXISTS, Definitions.ARGUMENT_NOT_EXISTS_MSG);
                    } else if (residencesList.size() > 1) {
                        basicResponseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
                    } else {
                        if (!dateValidator.isValid(residence.getBirthDate())) {
                            basicResponseModel = new BasicResponseModel(Definitions.INVALID_DATE, Definitions.INVALID_DATE_MSG);
                        } else {
                            Residence oldResidence = persist.loadObject(Residence.class, residence.getOid());
                            oldResidence.setObject(residence);
                            persist.save(oldResidence);
                            basicResponseModel = new BasicResponseModel(residencesList);
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
