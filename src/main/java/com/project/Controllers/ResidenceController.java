package com.project.Controllers;

import com.project.Models.Colony;
import com.project.Models.Residence;
import com.project.Models.Street;
import com.project.Models.User;
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
public class ResidenceController extends BaseController {
    @Autowired
    private Persist persist;
    @Autowired
    private DateUtils dateUtils;
    @Autowired
    private IdValidator idValidator;

    @PostConstruct
    public void init() {
    }

    @RequestMapping(value = "/residence/add", method = RequestMethod.POST)
    public BasicResponseModel addResidence(
            @ModelAttribute("Residence") Residence residence,
            AuthUser authUser
    ) {
        BasicResponseModel basicResponseModel = null;
        if (authUser.getAuthUserError() == null) {
            if (residence.objectIsEmpty()) {
                basicResponseModel = new BasicResponseModel(Definitions.MISSING_FIELDS, Definitions.MISSING_FIELDS_MSG);
            } else {
                BasicResponseModel isValidColonyId = idValidator.isValidId(residence.getColonyID(), Colony.class);
                if (isValidColonyId != null) {
                    basicResponseModel = isValidColonyId;
                } else {
                    BasicResponseModel isValidStreetId = idValidator.isValidId(residence.getStreetID(), Street.class);
                    if (isValidStreetId != null) {
                        basicResponseModel = isValidStreetId;
                    } else {
                        if (!dateUtils.isValidDatePattern(residence.getBirthDate())) {
                            basicResponseModel = new BasicResponseModel(Definitions.INVALID_DATE, Definitions.INVALID_DATE_MSG);
                        } else {
                            residence.setBirthDate(dateUtils.convertDate2TS(residence.getBirthDate()));
                            persist.save(residence);
                            basicResponseModel = new BasicResponseModel(residence);
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

    @RequestMapping(value = "/residence/delete", method = RequestMethod.POST)
    public BasicResponseModel deleteResidence(@RequestParam int oid, @RequestParam boolean delete,
                                              AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        BasicResponseModel isValidOid = idValidator.isValidId(oid, Residence.class);
        if (authUser.getAuthUserError() == null) {
            if (isValidOid != null) {
                basicResponseModel = isValidOid;
            } else {
                List<Residence> residencesList = persist.getQuerySession().createQuery("FROM Residence WHERE oid = :oid")
                        .setParameter("oid", oid)
                        .list();
                Residence residence = residencesList.get(0);
                residence.setDeleted(delete);
                persist.save(residence);
                basicResponseModel = new BasicResponseModel(residence);
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
        BasicResponseModel basicResponseModel =  null;

        if (authUser.getAuthUserError() == null) {
            List<Residence> residenceList = persist.getQuerySession().createQuery("FROM Residence").list();
            for (int i = 0; i < residenceList.size(); i++) {
                Colony colonyRow = persist.loadObject(Colony.class, residenceList.get(i).getColonyID());
                Street streetRow = persist.loadObject(Street.class, residenceList.get(i).getStreetID());
                residenceList.get(i).setColonyName(colonyRow.getHeColonyName());
                residenceList.get(i).setStreetName(streetRow.getName());
            }
            if (residenceList.isEmpty()) {
                basicResponseModel = new BasicResponseModel(Definitions.EMPTY_LIST, Definitions.EMPTY_LIST_MSG);
            } else {
                basicResponseModel = new BasicResponseModel(residenceList);
            }
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
        BasicResponseModel isValidId = idValidator.isValidId(oid, Residence.class);
        if (authUser.getAuthUserError() == null) {
            if (isValidId != null) {
                basicResponseModel = isValidId;
            } else {
                List<Residence> residencesList = persist.getQuerySession().createQuery("FROM Residence WHERE oid = :oid")
                        .setParameter("oid", oid)
                        .list();
                basicResponseModel = new BasicResponseModel(residencesList);
            }
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/residence/update", method = RequestMethod.POST)
    public BasicResponseModel updateResidence(
            @ModelAttribute("Residence") Residence residence,
            AuthUser authUser
    ) {
        BasicResponseModel basicResponseModel;
        BasicResponseModel isValidId = idValidator.isValidId(residence.getOid(), Residence.class);
        BasicResponseModel isValidStreetId = idValidator.isValidId(residence.getStreetID(), Street.class);
        BasicResponseModel isValidColonyId = idValidator.isValidId(residence.getColonyID(), Colony.class);
        if (authUser.getAuthUserError() == null) {
            if (residence.objectIsEmpty()) {
                basicResponseModel = new BasicResponseModel(Definitions.MISSING_FIELDS, Definitions.MISSING_FIELDS_MSG);
            } else {
                if (isValidId != null) {
                    basicResponseModel = isValidId;
                } else {
                    if (isValidColonyId != null) {
                        basicResponseModel = isValidColonyId;
                    } else {
                        if (isValidStreetId != null) {
                            basicResponseModel = isValidStreetId;
                        } else {
                            if (!dateUtils.isValidDatePattern(residence.getBirthDate())) {
                                basicResponseModel = new BasicResponseModel(Definitions.INVALID_DATE, Definitions.INVALID_DATE_MSG);
                            } else {
                                List<Residence> residencesList = persist.getQuerySession().createQuery("FROM Residence WHERE oid = :oid")
                                        .setParameter("oid", residence.getOid())
                                        .list();
                                Residence oldResidence = persist.loadObject(Residence.class, residence.getOid());
                                residence.setBirthDate(dateUtils.convertDate2TS(residence.getBirthDate()));
                                oldResidence.setObject(residence);
                                persist.save(oldResidence);
                                basicResponseModel = new BasicResponseModel(residencesList);
                            }
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
