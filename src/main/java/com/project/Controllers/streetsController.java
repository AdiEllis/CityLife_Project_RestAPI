package com.project.Controllers;

import com.project.Models.Colony;
import com.project.Models.Street;
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
public class streetsController extends BaseController {
    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {
    }

    @RequestMapping(value = "/streets/add", method = RequestMethod.POST)
    public BasicResponseModel addStreet(@RequestParam String name, @RequestParam int colonyID,
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
                    Street streetToAdd = new Street(false, name, colonyID);
                    persist.save(streetToAdd);
                    basicResponseModel = new BasicResponseModel(streetToAdd);
                }
            }
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/streets/delete", method = RequestMethod.POST)
    public BasicResponseModel deleteStreet(@RequestParam int id, @RequestParam boolean delete,
                                           AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        if (authUser.getAuthUserError() == null) {
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
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/streets/getAll", method = RequestMethod.GET)
    public BasicResponseModel getAllStreets(AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        if (authUser.getAuthUserError() == null) {
            List<Street> allStreets = persist.getQuerySession().createQuery("FROM Street").list();
            basicResponseModel = new BasicResponseModel(allStreets);
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/streets/getStreet", method = RequestMethod.POST)
    public BasicResponseModel getStreet(@RequestParam int id, AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        if (authUser.getAuthUserError() == null) {
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
                    basicResponseModel = new BasicResponseModel(streetsList.get(0));
                }
            }
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }
        return basicResponseModel;
    }

    @RequestMapping(value = "/streets/update", method = RequestMethod.POST)
    public BasicResponseModel updateStreet(@ModelAttribute("Street") Street street, AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        if (authUser.getAuthUserError() == null) {
            if (street.getOid() < 1 || street.getColonyID() < 1) {
                basicResponseModel = new BasicResponseModel(Definitions.INVALID_ARGUMENT, Definitions.INVALID_ARGUMENT_MSG);
            } else {
                if (street.objectIsEmpty()) {
                    basicResponseModel = new BasicResponseModel(Definitions.MISSING_FIELDS, Definitions.MISSING_FIELDS_MSG);
                } else {
                    List<Street> streetsList = persist.getQuerySession().createQuery("FROM Street WHERE id = :id")
                            .setParameter("id", street.getOid())
                            .list();
                    List<Colony> relevantColony = persist.getQuerySession().createQuery("FROM Colony WHERE id =: id")
                            .setParameter("id", street.getColonyID()).list();
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
        } else if (authUser.getAuthUserError() == Definitions.INVALID_TOKEN) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_TOKEN, Definitions.INVALID_TOKEN_MSG);
        } else {
            basicResponseModel = new BasicResponseModel(Definitions.NO_PERMISSIONS, Definitions.NO_PERMISSIONS_MSG);
        }
        return basicResponseModel;
    }

}
