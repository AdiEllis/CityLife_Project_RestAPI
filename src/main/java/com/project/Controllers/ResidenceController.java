package com.project.Controllers;

import com.google.gson.JsonArray;
import com.project.Models.Colony;
import com.project.Models.Residence;
import com.project.Models.Street;
import com.project.Objects.Entities.AuthUser;
import com.project.Objects.Entities.BasicResponseModel;
import com.project.Persist;
import com.project.Utils.DateUtils;
import com.project.Utils.Definitions;
import com.project.Utils.IdValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

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


    @RequestMapping(value = "/residence/filterResidenceListByAge", method = RequestMethod.GET)
    public BasicResponseModel filterResidenceListByAge(AuthUser authUser, int age) {
        BasicResponseModel basicResponseModel = null;
        if (authUser.getAuthUserError() == null) {
            List<Residence> residencesListByAge = persist.getQuerySession().createQuery("FROM Residence WHERE age =: age AND colonyID =: colonyID")
                    .setParameter("age", age)
                    .setParameter("colonyID", authUser.getAuthUserColonyID())
                    .list();
            basicResponseModel = new BasicResponseModel(residencesListByAge);
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/residence/numberFamilies", method = RequestMethod.GET)
    public BasicResponseModel getNumberOfFamilies(AuthUser authUser) {
        BasicResponseModel basicResponseModel = null;
        if (authUser.getAuthUserError() == null) {
            List<Residence> residencesList = persist.getQuerySession().createQuery("FROM Residence WHERE colonyID =: colonyID AND deleted =: deleted GROUP BY streetID, houseNumber")
                    .setParameter("colonyID", authUser.getAuthUserColonyID())
                    .setParameter("deleted",false)
                    .list();
            basicResponseModel = new BasicResponseModel(residencesList);
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/residence/add", method = RequestMethod.POST)
    public BasicResponseModel addResidence(
            @ModelAttribute("Residence") Residence residence,
            AuthUser authUser
    ) {
        BasicResponseModel basicResponseModel = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        int ageOfResident;
        if (authUser.getAuthUserError() == null) {
            if (residence.objectIsEmpty()) {
                basicResponseModel = new BasicResponseModel(Definitions.MISSING_FIELDS, Definitions.MISSING_FIELDS_MSG);
            } else {
                BasicResponseModel isValidColonyId = idValidator.isValidId(residence.getColonyID(), Colony.class);
                if (isValidColonyId != null) {
                    basicResponseModel = isValidColonyId;
                } else {
                    List<Street> streetExistInColony = persist.getQuerySession().createQuery("FROM Street WHERE oid = :oid AND colonyID =: colonyID")
                            .setParameter("oid", residence.getStreetID())
                            .setParameter("colonyID", residence.getColonyID())
                            .list();
                    if (streetExistInColony.isEmpty()) {
                        basicResponseModel = new BasicResponseModel(Definitions.STREET_NOT_EXIST_IN_COLONY, Definitions.STREET_NOT_EXIST_IN_COLONY_MSG);
                    } else {
                        if (!dateUtils.isValidDatePattern(residence.getBirthDate())) {
                            basicResponseModel = new BasicResponseModel(Definitions.INVALID_DATE, Definitions.INVALID_DATE_MSG);
                        } else {
                            List<Residence> sameResidence = persist.getQuerySession().createQuery("FROM Residence WHERE id = :id")
                                    .setParameter("id", residence.getId())
                                    .list();
                            if (sameResidence.size() > 0) {
                                basicResponseModel = new BasicResponseModel(Definitions.USER_EXISTS, Definitions.USER_EXISTS_MSG);
                            } else {
                                if (residence.isLivesInHousingUnit() && residence.getApartmentOwner() == 0) {
                                    basicResponseModel = new BasicResponseModel(Definitions.NOT_CHOSEN_APARTMENT_OWNER, Definitions.NOT_CHOSEN_APARTMENT_OWNER_MSG);
                                } else {
                                    // calculation of resident age
                                    LocalDate now = LocalDate.now();
                                    LocalDate localDate = LocalDate.parse(residence.getBirthDate(), formatter);
                                    ageOfResident = Period.between(localDate, now).getYears();
                                    residence.setAge(ageOfResident);
                                    // set colony and street name
                                    Colony colonyRow = persist.loadObject(Colony.class, residence.getColonyID());
                                    Street streetRow = persist.loadObject(Street.class, residence.getStreetID());
                                    residence.setColonyName(colonyRow.getHeColonyName());
                                    residence.setStreetName(streetRow.getName());
                                    residence.setBirthDate(dateUtils.convertDate2TS(residence.getBirthDate()));
                                    // save resident
                                    persist.save(residence);
                                    basicResponseModel = new BasicResponseModel(residence);
                                }
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

    @RequestMapping(value = "/residence/delete", method = RequestMethod.POST)
    public BasicResponseModel deleteResidence(@RequestParam int oid, @RequestParam boolean deleted,
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
                residence.setDeleted(deleted);
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
        BasicResponseModel basicResponseModel = null;
        List<Residence> residenceList;
        if (authUser.getAuthUserError() == null) {
            if (authUser.getAuthUserIsAdmin()) {
                residenceList = persist.getQuerySession().createQuery("FROM Residence")
                        .list();
            } else {
                residenceList = persist.getQuerySession().createQuery("FROM Residence WHERE colonyID = :authColonyID AND deleted =: deleted")
                        .setParameter("authColonyID", authUser.getAuthUserColonyID())
                        .setParameter("deleted",false)
                        .list();
            }
            for (int i = 0; i < residenceList.size(); i++) {
                Colony colonyRow = persist.loadObject(Colony.class, residenceList.get(i).getColonyID());
                Street streetRow = persist.loadObject(Street.class, residenceList.get(i).getStreetID());
                residenceList.get(i).setColonyName(colonyRow.getHeColonyName());
                residenceList.get(i).setStreetName(streetRow.getName());
                residenceList.get(i).setBirthDate(residenceList.get(i).getBirthDate());
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
                basicResponseModel = new BasicResponseModel(residencesList.get(0));
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        BasicResponseModel isValidId = idValidator.isValidId(residence.getOid(), Residence.class);
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
                        List<Street> streetExistInColony = persist.getQuerySession().createQuery("FROM Street WHERE oid = :oid AND colonyID =: colonyID")
                                .setParameter("oid", residence.getStreetID())
                                .setParameter("colonyID", residence.getColonyID())
                                .list();
                        if (streetExistInColony.isEmpty()) {
                            basicResponseModel = new BasicResponseModel(Definitions.STREET_NOT_EXIST_IN_COLONY, Definitions.STREET_NOT_EXIST_IN_COLONY_MSG);
                        } else {
                            if (!dateUtils.isValidDatePattern(residence.getBirthDate())) {
                                basicResponseModel = new BasicResponseModel(Definitions.INVALID_DATE, Definitions.INVALID_DATE_MSG);
                            } else {
                                List<Residence> residencesList = persist.getQuerySession().createQuery("FROM Residence WHERE oid = :oid")
                                        .setParameter("oid", residence.getOid())
                                        .list();
                                Residence oldResidence = persist.loadObject(Residence.class, residence.getOid());
                                // update resident age
                                LocalDate now = LocalDate.now();
                                LocalDate localDate = LocalDate.parse(residence.getBirthDate(), formatter);
                                int ageOfResident = Period.between(localDate, now).getYears();
                                residence.setAge(ageOfResident);
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
