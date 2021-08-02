package com.project.Controllers;

import com.project.Models.Colony;
import com.project.Models.Street;
import com.project.Models.Transaction;
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
public class streetsController {
    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {
    }

    @RequestMapping(value = "/streets/add", method = RequestMethod.POST)
    public BasicResponseModel addStreet(@RequestParam String name,@RequestParam int colonyId) {
        BasicResponseModel basicResponseModel;
        if (colonyId < 1) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_ARGUMENT, Definitions.INVALID_ARGUMENT_MSG);
        } else {
            List<Colony> relevantColony = persist.getQuerySession().createQuery("FROM Colony WHERE id =: id")
                    .setParameter("id", colonyId).list();
            if (relevantColony.isEmpty()) {
                basicResponseModel = new BasicResponseModel(Definitions.COLONY_NOT_EXISTS, Definitions.COLONY_NOT_EXISTS_MSG);
            } else if (relevantColony.size() > 1) {
                basicResponseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
            } else {
                Street streetToAdd = new Street(false, name, colonyId);
                persist.save(streetToAdd);
                basicResponseModel = new BasicResponseModel(streetToAdd);
            }
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/streets/delete", method = RequestMethod.POST)
    public BasicResponseModel deleteStreet(@RequestParam int id, @RequestParam boolean delete) {
        BasicResponseModel basicResponseModel;
        if (id < 1) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_ARGUMENT, Definitions.INVALID_ARGUMENT_MSG);
        } else {
            List<Street> streetsList = persist.getQuerySession().createQuery("FROM Street WHERE id = :id")
                    .setParameter("id", id)
                    .list();
            if (streetsList.isEmpty()) {
                basicResponseModel = new BasicResponseModel(Definitions.ARGUMENT_NOT_EXISTS, Definitions.ARGUMENT_NOT_EXISTS_MSG);
            } else if (streetsList.size() > 1) {
                basicResponseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
            } else {
                Street street = streetsList.get(0);
                street.setDeleted(delete);
                persist.save(street);
                basicResponseModel = new BasicResponseModel(street);
            }
        }
        return basicResponseModel;
    }
    @RequestMapping(value = "/streets/getAll",method = RequestMethod.GET)
    public BasicResponseModel getAllStreets() {
        BasicResponseModel basicResponseModel;
        List<Street> allStreets = persist.getQuerySession().createQuery("FROM Street").list();
        basicResponseModel = new BasicResponseModel(allStreets);
        return basicResponseModel;
    }
    @RequestMapping(value = "/streets/getStreet",method = RequestMethod.POST)
    public BasicResponseModel getStreet(@RequestParam int id) {
        BasicResponseModel basicResponseModel;
        if (id < 1) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_ARGUMENT,Definitions.INVALID_ARGUMENT_MSG);
        }
        else {
            List<Street> streetsList = persist.getQuerySession().createQuery("FROM Street WHERE id = :id")
                    .setParameter("id", id)
                    .list();
            if (streetsList.isEmpty()) {
                basicResponseModel = new BasicResponseModel(Definitions.ARGUMENT_NOT_EXISTS,Definitions.ARGUMENT_NOT_EXISTS_MSG);
            }
            else if (streetsList.size() > 1) {
                basicResponseModel = new BasicResponseModel(Definitions.MULTI_RECORD,Definitions.MULTI_RECORD_MSG);
            }
            else {
                basicResponseModel = new BasicResponseModel(streetsList.get(0));
            }
        }
        return basicResponseModel;
    }
    @RequestMapping(value = "/streets/update", method = RequestMethod.POST)
    public BasicResponseModel updateStreet(@ModelAttribute("Street") Street street) {
        BasicResponseModel basicResponseModel;
        if (street.getOid() < 1 || street.getColonyId() < 1) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_ARGUMENT, Definitions.INVALID_ARGUMENT_MSG);
        } else {
            if (street.objectIsEmpty()) {
                basicResponseModel = new BasicResponseModel(Definitions.MISSING_FIELDS, Definitions.MISSING_FIELDS_MSG);
            } else {
                List<Street> streetsList = persist.getQuerySession().createQuery("FROM Street WHERE id = :id")
                        .setParameter("id", street.getOid())
                        .list();
                List<Colony> relevantColony = persist.getQuerySession().createQuery("FROM Colony WHERE id =: id")
                        .setParameter("id", street.getColonyId()).list();
                if (streetsList.isEmpty() || relevantColony.isEmpty()) {
                    basicResponseModel = new BasicResponseModel(Definitions.ARGUMENT_NOT_EXISTS, Definitions.ARGUMENT_NOT_EXISTS_MSG);
                } else if (streetsList.size() > 1 || relevantColony.size() > 1) {
                    basicResponseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
                } else {
                    Street oldStreet = persist.loadObject(Street.class, street.getOid());
                    oldStreet.setObject(street);
                    persist.save(oldStreet);
                    basicResponseModel = new BasicResponseModel(streetsList);
                }
            }
        }

        return basicResponseModel;
    }

}
