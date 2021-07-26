package com.project.Controllers;

import com.project.Models.Residence;
import com.project.Objects.Entities.BasicResponseModel;
import com.project.Persist;
import com.project.Utils.Definitions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

import java.util.List;
/**/

@RestController
@Transactional
public class ResidenceController {
    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {
    }

    @RequestMapping(value = "/residence/add", method = RequestMethod.POST)
    public Residence addResidence(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String birthDate,
                                  @RequestParam String phone, @RequestParam String id, @RequestParam String colonyID,
                                  @RequestParam String streetID, @RequestParam String houseNumber) {
        Residence residenceToAdd = new Residence(firstName, lastName, birthDate, phone, id,colonyID ,streetID,houseNumber, false);
        persist.save(residenceToAdd);
        return residenceToAdd;
    }

    @RequestMapping(value = "/residence/delete", method = RequestMethod.POST)
    public BasicResponseModel deleteResidence(@RequestParam int oid, @RequestParam boolean delete) {
        BasicResponseModel basicResponseModel;
        if (oid < 1) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_ARGUMENT, Definitions.INVALID_ARGUMENT_MSG);
        } else {
            List<Residence> residencesList = persist.getQuerySession().createQuery("FROM Residence WHERE oid = :oid")
                    .setParameter("oid", oid)
                    .list();
            if (residencesList.isEmpty()) {
                basicResponseModel = new BasicResponseModel(Definitions.PRODUCT_NOT_EXISTS, Definitions.PRODUCT_NOT_EXISTS_MSG);
            } else if (residencesList.size() > 1) {
                basicResponseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
            } else {
                Residence residence = residencesList.get(0);
                residence.setDeleted(delete);
                persist.save(residence);
                basicResponseModel = new BasicResponseModel(residence);
            }
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/residence/getAll", method = RequestMethod.GET)
    public BasicResponseModel getAllResidences() {
        BasicResponseModel basicResponseModel;
        List<Residence> residencesList = persist.getQuerySession().createQuery("FROM Residence")
                .list();
        basicResponseModel = new BasicResponseModel(residencesList);

        return basicResponseModel;
    }

    @RequestMapping(value = "/residence/getResidence", method = RequestMethod.GET)
    public BasicResponseModel getResidence(@RequestParam int oid) {
        BasicResponseModel basicResponseModel;
        if (oid < 1) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_ARGUMENT, Definitions.INVALID_ARGUMENT_MSG);
        } else {
            List<Residence> residencesList = persist.getQuerySession().createQuery("FROM Residence WHERE oid = :oid")
                    .setParameter("oid", oid)
                    .list();
            if (residencesList.isEmpty()) {
                basicResponseModel = new BasicResponseModel(Definitions.PRODUCT_NOT_EXISTS, Definitions.PRODUCT_NOT_EXISTS_MSG);
            } else if (residencesList.size() > 1) {
                basicResponseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
            } else {
                basicResponseModel = new BasicResponseModel(residencesList);
            }
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/residence/update", method = RequestMethod.POST)
    public BasicResponseModel updateResidence(@ModelAttribute("Residence") Residence residence) {
        BasicResponseModel basicResponseModel;
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
                    basicResponseModel = new BasicResponseModel(Definitions.PRODUCT_NOT_EXISTS, Definitions.PRODUCT_NOT_EXISTS_MSG);
                } else if (residencesList.size() > 1) {
                    basicResponseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
                } else {
                    Residence oldResidence = persist.loadObject(Residence.class, residence.getOid());
                    oldResidence.setObject(residence);
                    persist.save(oldResidence);
                    basicResponseModel = new BasicResponseModel(residencesList);
                }
            }
        }

        return basicResponseModel;
    }
}
