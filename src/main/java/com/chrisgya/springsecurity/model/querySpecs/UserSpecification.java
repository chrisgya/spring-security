package com.chrisgya.springsecurity.model.querySpecs;

import com.chrisgya.springsecurity.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class UserSpecification {

    // if username == null then specification is ignored
    public static Specification<User> userUsernameEquals(String username) {
        return (root, query, builder) ->
                !StringUtils.hasText(username) ?
                        builder.conjunction() :
                        builder.equal(builder.lower(root.get("username")), username.trim().toLowerCase());
                       // builder.equal(root.get("username"), username);
    }

    // if email == null then specification is ignored
    public static Specification<User> userEmailEquals(String email) {
        return (root, query, builder) ->
                !StringUtils.hasText(email) ?
                        builder.conjunction() :
                        builder.like(builder.lower(root.get("email")), "%" + email.trim().toLowerCase() + "%");
    }

    // if firstname == null then specification is ignored
    public static Specification<User> userFirstnameEquals(String firstName) {
        return (root, query, builder) ->
                !StringUtils.hasText(firstName) ?
                        builder.conjunction() :
                        builder.like(builder.lower(root.get("firstName")), "%" + firstName.trim().toLowerCase() + "%");
    }

    // if lastname == null then specification is ignored
    public static Specification<User> userLastnameEquals(String lastName) {
        return (root, query, builder) ->
                !StringUtils.hasText(lastName) ?
                        builder.conjunction() :
                        builder.like(builder.lower(root.get("lastName")), "%" + lastName.trim().toLowerCase() + "%");
    }

    // if isLocked == null then specification is ignored
    public static Specification<User> userIsLockedEquals(Boolean locked) {
        return (root, query, builder) ->
                locked == null ?
                        builder.conjunction() :
                        builder.equal(root.get("locked"), locked);
    }

    // if isEnabled == null then specification is ignored
    public static Specification<User> userIsEnabledEquals(Boolean enabled) {
        return (root, query, builder) ->
                enabled == null ?
                        builder.conjunction() :
                        builder.equal(root.get("enabled"), enabled);
    }

    // if isConfirmed == null then specification is ignored
    public static Specification<User> userIsConfirmedEquals(Boolean confirmed) {
        return (root, query, builder) ->
                confirmed == null ?
                        builder.conjunction() :
                        builder.equal(root.get("confirmed"), confirmed);
    }

}

/*
   for (SearchCriteria criteria : list) {
            if (criteria.getOperation().equals(SearchOperation.GREATER_THAN)) {
                predicates.add(builder.greaterThan(
                        root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {
                predicates.add(builder.lessThan(
                        root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN_EQUAL)) {
                predicates.add(builder.greaterThanOrEqualTo(
                        root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN_EQUAL)) {
                predicates.add(builder.lessThanOrEqualTo(
                        root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.NOT_EQUAL)) {
                predicates.add(builder.notEqual(
                        root.get(criteria.getKey()), criteria.getValue()));
            } else if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
                predicates.add(builder.equal(
                        root.get(criteria.getKey()), criteria.getValue()));
            } else if (criteria.getOperation().equals(SearchOperation.MATCH)) {
                predicates.add(builder.like(
                        builder.lower(root.get(criteria.getKey())),
                        "%" + criteria.getValue().toString().toLowerCase() + "%"));
            } else if (criteria.getOperation().equals(SearchOperation.MATCH_END)) {
                predicates.add(builder.like(
                        builder.lower(root.get(criteria.getKey())),
                        criteria.getValue().toString().toLowerCase() + "%"));
            } else if (criteria.getOperation().equals(SearchOperation.MATCH_START)) {
                predicates.add(builder.like(
                        builder.lower(root.get(criteria.getKey())),
                        "%" + criteria.getValue().toString().toLowerCase()));
            } else if (criteria.getOperation().equals(SearchOperation.IN)) {
                predicates.add(builder.in(root.get(criteria.getKey())).value(criteria.getValue()));
            } else if (criteria.getOperation().equals(SearchOperation.NOT_IN)) {
                predicates.add(builder.not(root.get(criteria.getKey())).in(criteria.getValue()));
            }
        }
 */