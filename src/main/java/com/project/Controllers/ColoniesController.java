package com.project.Controllers;

import com.project.Models.Colony;
import com.project.Objects.Entities.AuthUser;
import com.project.Objects.Entities.BasicResponseModel;
import com.project.Persist;
import com.project.Utils.Definitions;
import com.project.Utils.IdValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@Transactional
public class ColoniesController extends BaseController {
    @Autowired
    private Persist persist;
    @Autowired
    private IdValidator idValidator;

    @PostConstruct
    public void init() {
    }

    @RequestMapping(value = "/colony/add", method = RequestMethod.POST)
    public BasicResponseModel addColony(@ModelAttribute("Colony") Colony colony,
                                        AuthUser authUser
    ) {
        BasicResponseModel basicResponseModel;
        if (!authUser.getAuthUserIsAdmin()) {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        } else {
            if (authUser.getAuthUserError() == null) {
                if (colony.objectIsEmpty()) {
                    basicResponseModel = new BasicResponseModel(Definitions.MISSING_FIELDS, Definitions.MISSING_FIELDS_MSG);
                } else {
                    if (!Pattern.matches("[א-ת]+", colony.getHeColonyName())) {
                        basicResponseModel = new BasicResponseModel(Definitions.INVALID_HEBREW_NAME, Definitions.INVALID_HEBREW_NAME_MSG);
                    } else {
                        if (!Pattern.matches("[a-zA-Z]+", colony.getEnColonyName())) {
                            basicResponseModel = new BasicResponseModel(Definitions.INVALID_ENGLISH_NAME, Definitions.INVALID_ENGLISH_NAME_MSG);
                        } else {
                            persist.save(colony);
                            basicResponseModel = new BasicResponseModel(colony);
                        }
                    }
                }
            } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
                basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
            } else {
                basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
            }
        }


        return basicResponseModel;
    }


    @RequestMapping(value = "/colony/delete", method = RequestMethod.POST)
    public BasicResponseModel deleteColony(
            @RequestParam int oid,
            @RequestParam boolean deleted,
            AuthUser authUser
    ) {
        BasicResponseModel basicResponseModel;
        if (!authUser.getAuthUserIsAdmin()) {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        } else {
            if (authUser.getAuthUserError() == null) {
                BasicResponseModel isValidId = idValidator.isValidId(oid, Colony.class);
                if (isValidId != null) {
                    basicResponseModel = isValidId;
                } else {
                    List<Colony> coloniesList = persist.getQuerySession().createQuery("FROM Colony WHERE oid = :oid")
                            .setParameter("oid", oid)
                            .list();
                    Colony colony = coloniesList.get(0);
                    colony.setDeleted(deleted);
                    persist.save(colony);
                    basicResponseModel = new BasicResponseModel(colony);
                }
            } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
                basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
            } else {
                basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
            }
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/colony/getAll", method = RequestMethod.GET)
    public BasicResponseModel getAllColonies(AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        if (!authUser.getAuthUserIsAdmin()) {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        } else {
            if (authUser.getAuthUserError() == null) {
                List<Colony> allColonies = persist.getQuerySession().createQuery("FROM Colony")
                        .list();
                basicResponseModel = new BasicResponseModel(allColonies);
            } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
                basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
            } else {
                basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
            }
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/colony/getColony", method = RequestMethod.GET)
    public BasicResponseModel getColony(@RequestParam int oid, AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        BasicResponseModel isValidId = idValidator.isValidId(oid, Colony.class);
        if (!authUser.getAuthUserIsAdmin()) {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        } else {
            if (authUser.getAuthUserError() == null) {
                if (isValidId != null) {
                    basicResponseModel = isValidId;
                } else {
                    List<Colony> coloniesList = persist.getQuerySession().createQuery("FROM Colony WHERE oid = :oid")
                            .setParameter("oid", oid)
                            .list();
                    basicResponseModel = new BasicResponseModel(coloniesList.get(0));

                }
            } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
                basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
            } else {
                basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
            }
        }
        return basicResponseModel;
    }


    @RequestMapping(value = "/colony/update", method = RequestMethod.POST)
    public BasicResponseModel updateColony(@ModelAttribute("Colony") Colony colony,
                                           AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        BasicResponseModel isValidId = idValidator.isValidId(colony.getOid(), Colony.class);
        if (!authUser.getAuthUserIsAdmin()) {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        } else {
            if (authUser.getAuthUserError() == null) {
                if (isValidId != null) {
                    basicResponseModel = isValidId;
                } else {
                    if (colony.objectIsEmpty()) {
                        basicResponseModel = new BasicResponseModel(Definitions.MISSING_FIELDS, Definitions.MISSING_FIELDS_MSG);
                    } else {
                        if (!Pattern.matches("[א-ת]+", colony.getHeColonyName())) {
                            basicResponseModel = new BasicResponseModel(Definitions.INVALID_HEBREW_NAME, Definitions.INVALID_HEBREW_NAME_MSG);
                        } else {
                            if (!Pattern.matches("[a-zA-Z]+", colony.getEnColonyName())) {
                                basicResponseModel = new BasicResponseModel(Definitions.INVALID_ENGLISH_NAME, Definitions.INVALID_ENGLISH_NAME_MSG);
                            } else {
                                List<Colony> coloniesList = persist.getQuerySession().createQuery("FROM Colony WHERE id = :id")
                                        .setParameter("id", colony.getOid())
                                        .list();
                                Colony oldColony = persist.loadObject(Colony.class, colony.getOid());
                                oldColony.setObject(colony);
                                persist.save(oldColony);
                                basicResponseModel = new BasicResponseModel(coloniesList);
                            }
                        }
                    }
                }
            } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
                basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
            } else {
                basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
            }
        }
        return basicResponseModel;
    }
}
