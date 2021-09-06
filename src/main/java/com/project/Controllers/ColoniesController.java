package com.project.Controllers;

import com.project.Models.Colony;
import com.project.Models.Residence;
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
public class ColoniesController extends BaseController {
    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {
    }

    @RequestMapping(value = "/colony/add", method = RequestMethod.POST)
    public BasicResponseModel addColony(
            @RequestParam String enColonyName,
            @RequestParam String heColonyName,
            AuthUser authUser
    ) {
        BasicResponseModel basicResponseModel;
        if (authUser.getAuthUserError() == null) {
            Colony colonyToAdd = new Colony(enColonyName, heColonyName, false);
            persist.save(colonyToAdd);
            basicResponseModel = new BasicResponseModel(colonyToAdd);
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }
        return basicResponseModel;
    }


    @RequestMapping(value = "/colony/delete", method = RequestMethod.POST)
    public BasicResponseModel deleteColony(
            @RequestParam int id,
            @RequestParam boolean delete,
            AuthUser authUser
    ) {
        BasicResponseModel basicResponseModel;
        if (authUser.getAuthUserError() == null) {
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
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/colony/getAll", method = RequestMethod.GET)
    public BasicResponseModel getAllColonies(AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        if (authUser.getAuthUserError() == null) {
            List<Colony> allColonies = persist.getQuerySession().createQuery("FROM Colony").list();
            basicResponseModel = new BasicResponseModel(allColonies);
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/colony/getColony", method = RequestMethod.POST)
    public BasicResponseModel getColony(@RequestParam int id, AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        if (authUser.getAuthUserError() == null) {
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
                    basicResponseModel = new BasicResponseModel(coloniesList.get(0));
                }
            }
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/colony/update", method = RequestMethod.POST)
    public BasicResponseModel updateColony(@ModelAttribute("Colony") Colony colony,
                                           AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        if (authUser.getAuthUserError() == null) {
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
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }
        return basicResponseModel;
    }
}
