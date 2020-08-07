package com.spring.dev2chuc.nutritious_food.payload.response;

import com.spring.dev2chuc.nutritious_food.model.UserProfile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SpecificationAll implements org.springframework.data.jpa.domain.Specification<UserProfile> {

    private SearchCriteria criteria;

    public SpecificationAll(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<UserProfile> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
        if (criteria.getOperation().equalsIgnoreCase("orderBy")) {
            if (criteria.getValue().toString().equals("desc")) {
                criteriaQuery.orderBy(builder.desc(root.get(criteria.getKey())));
            }
            if (criteria.getValue().toString().equals("asc")) {
                criteriaQuery.orderBy(builder.asc(root.get(criteria.getKey())));
            }
        }

        if (criteria.getOperation().equalsIgnoreCase(">=")) {
            return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("<=")) {
            return builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                        root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        } else if (criteria.getOperation().equalsIgnoreCase("in")) {
            CriteriaBuilder.In<Long> inClause = builder.in(root.get(criteria.getKey()));
            Long[] ids = (Long[]) criteria.getValue();
            for (Long id : ids) {
                inClause.value(id);
            }
            return inClause;
        }
        return null;
    }
}
