package com.project.Utils;

import com.project.Models.Residence;
import com.project.Objects.Entities.BasicResponseModel;
import com.project.Persist;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaDelete;
import java.util.List;

@Component
public class IdValidator {
    @Autowired
    private Persist persist;

    public <T> BasicResponseModel isValidId(int id, Class<T> clazz) {
        BasicResponseModel basicResponseModel = null;

        if (id < 1) {
            basicResponseModel = new BasicResponseModel(Definitions.INVALID_ARGUMENT, Definitions.INVALID_ARGUMENT_MSG);
        } else {
            List<T> residencesList = persist
                    .getQuerySession()
                    .createQuery(
                            String.format("FROM %s WHERE %s = :oid", clazz.getSimpleName(), "oid")
                    )
                    .setParameter("oid", id)
                    .list();
            if (residencesList.isEmpty()) {
                basicResponseModel = new BasicResponseModel(Definitions.ARGUMENT_NOT_EXISTS, Definitions.ARGUMENT_NOT_EXISTS_MSG);
            } else if (residencesList.size() > 1) {
                basicResponseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
            }
        }
        return basicResponseModel;
    }
}
