package com.project.Controllers;

import com.project.Models.Colony;
import com.project.Models.Street;
import com.project.Models.Transaction;
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

@RestController
@Transactional
public class StreetsController extends BaseController {
    @Autowired
    private Persist persist;
    @Autowired
    private IdValidator idValidator;

    @PostConstruct
    public void init() {
    }

    @RequestMapping(value = "/streets/add", method = RequestMethod.POST)
    public BasicResponseModel addStreet(@ModelAttribute("Street") Street street,
                                        AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        BasicResponseModel isValidColonyId = idValidator.isValidId(street.getColonyID(), Colony.class);
        if (authUser.getAuthUserError() == null) {
            if (street.objectIsEmpty()) {
                basicResponseModel = new BasicResponseModel(Definitions.MISSING_FIELDS, Definitions.MISSING_FIELDS_MSG);
            } else {
                if (isValidColonyId != null) {
                    basicResponseModel = isValidColonyId;
                } else {
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

    @RequestMapping(value = "/streets/delete", method = RequestMethod.POST)
    public BasicResponseModel deleteStreet(@RequestParam int id, @RequestParam boolean delete,
                                           AuthUser authUser) {
        BasicResponseModel basicResponseModel;
        BasicResponseModel isValidId = idValidator.isValidId(id, Street.class);
        if (authUser.getAuthUserError() == null) {
            if (isValidId != null) {
                basicResponseModel = isValidId;
            } else {
                List<Street> streetsList = persist.getQuerySession().createQuery("FROM Street WHERE id = :id")
                        .setParameter("id", id)
                        .list();
                Street street = streetsList.get(0);
                street.setDeleted(delete);
                persist.save(street);
                basicResponseModel = new BasicResponseModel(street);
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
        BasicResponseModel isValidId = idValidator.isValidId(id, Colony.class);
        if (authUser.getAuthUserError() == null) {
            if (isValidId != null) {
                basicResponseModel = isValidId;
            } else {
                List<Street> streetsList = persist.getQuerySession().createQuery("FROM Street WHERE id = :id")
                        .setParameter("id", id)
                        .list();
                basicResponseModel = new BasicResponseModel(streetsList.get(0));

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
        BasicResponseModel isValidId = idValidator.isValidId(street.getOid(), Street.class);
        BasicResponseModel isValidColonyID = idValidator.isValidId(street.getColonyID(), Colony.class);
        if (authUser.getAuthUserError() == null) {
            if (street.objectIsEmpty()) {
                basicResponseModel = new BasicResponseModel(Definitions.MISSING_FIELDS, Definitions.MISSING_FIELDS_MSG);
            } else {
                if (isValidId != null) {
                    basicResponseModel = isValidId;
                } else {
                    if (isValidColonyID != null) {
                        basicResponseModel = isValidColonyID;
                    } else {
                        List<Street> streetsList = persist.getQuerySession().createQuery("FROM Street WHERE id = :id")
                                .setParameter("id", street.getOid())
                                .list();
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
