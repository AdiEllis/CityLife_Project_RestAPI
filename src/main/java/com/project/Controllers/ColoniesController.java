package com.project.Controllers;

import com.project.Models.Colony;
import com.project.Models.Residence;
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
public class ColoniesController {
    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {
    }

    @RequestMapping(value = "/colony/add", method = RequestMethod.POST)
    public Colony addResidence(@RequestParam String enColonyName, @RequestParam String heColonyName) {
        Colony colonyToAdd = new Colony(enColonyName, heColonyName, false);
        persist.save(colonyToAdd);
        return colonyToAdd;
    }
    @RequestMapping(value = "/colony/delete", method = RequestMethod.POST)
    public BasicResponseModel deleteColony(@RequestParam int id, @RequestParam boolean delete) {
        BasicResponseModel basicResponseModel;
        if (id < 1) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_ARGUMENT, Definitions.INVALID_ARGUMENT_MSG);
        } else {
            List<Colony> coloniesList = persist.getQuerySession().createQuery("FROM Colony WHERE id = :id")
                    .setParameter("id", id)
                    .list();
            if (coloniesList.isEmpty()) {
                basicResponseModel = new BasicResponseModel(Definitions.ARGUMENT_NOT_EXISTS, Definitions.ARGUMENT_NOT_EXISTS_MSG);
            } else if (coloniesList.size() > 1) {
                basicResponseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
            } else {
                Colony colony = coloniesList.get(0);
                colony.setDeleted(delete);
                persist.save(colony);
                basicResponseModel = new BasicResponseModel(colony);
            }
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/colony/getAll",method = RequestMethod.GET)
    public BasicResponseModel getAllColonies() {
        BasicResponseModel basicResponseModel;
        List<Colony> allColonies = persist.getQuerySession().createQuery("FROM Colony").list();
        basicResponseModel = new BasicResponseModel(allColonies);
        return basicResponseModel;
    }
    @RequestMapping(value = "/colony/getColony",method = RequestMethod.POST)
    public BasicResponseModel getColony(@RequestParam int id) {
        BasicResponseModel basicResponseModel;
        if (id < 1) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_ARGUMENT,Definitions.INVALID_ARGUMENT_MSG);
        }
        else {
            List<Colony> coloniesList = persist.getQuerySession().createQuery("FROM Colony WHERE id = :id")
                    .setParameter("id", id)
                    .list();
            if (coloniesList.isEmpty()) {
                basicResponseModel = new BasicResponseModel(Definitions.ARGUMENT_NOT_EXISTS,Definitions.ARGUMENT_NOT_EXISTS_MSG);
            }
            else if (coloniesList.size() > 1) {
                basicResponseModel = new BasicResponseModel(Definitions.MULTI_RECORD,Definitions.MULTI_RECORD_MSG);
            }
            else {
                basicResponseModel = new BasicResponseModel(coloniesList.get(0));
            }
        }
        return basicResponseModel;
    }
    @RequestMapping(value = "/colony/update", method = RequestMethod.POST)
    public BasicResponseModel updateColony(@ModelAttribute("Colony") Colony colony) {
        BasicResponseModel basicResponseModel;
        if (colony.getOid() < 1) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_ARGUMENT, Definitions.INVALID_ARGUMENT_MSG);
        } else {
            if (colony.objectIsEmpty()) {
                basicResponseModel = new BasicResponseModel(Definitions.MISSING_FIELDS, Definitions.MISSING_FIELDS_MSG);
            } else {
                List<Colony> coloniesList = persist.getQuerySession().createQuery("FROM Colony WHERE id = :id")
                        .setParameter("id", colony.getOid())
                        .list();
                if (coloniesList.isEmpty()) {
                    basicResponseModel = new BasicResponseModel(Definitions.ARGUMENT_NOT_EXISTS, Definitions.ARGUMENT_NOT_EXISTS_MSG);
                } else if (coloniesList.size() > 1) {
                    basicResponseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
                } else {
                    Colony oldColony = persist.loadObject(Colony.class, colony.getOid());
                    oldColony.setObject(colony);
                    persist.save(oldColony);
                    basicResponseModel = new BasicResponseModel(coloniesList);
                }
            }
        }

        return basicResponseModel;
    }
}
