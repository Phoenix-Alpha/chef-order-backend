package com.halalhomemade.backend.repositories.Specifications;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.halalhomemade.backend.models.Offer;

public class OfferSpecification implements Specification<Offer> {

  private static final long serialVersionUID = 4708035207074053294L;

  private List<SearchCriteria> searchCriterialist;

  public OfferSpecification(final List<SearchCriteria> criteriaList) {
    this.searchCriterialist = criteriaList;
  }

  public void add(SearchCriteria criteria) {
    searchCriterialist.add(criteria);
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public Predicate toPredicate(Root<Offer> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

    // create a new predicate list
    List<Predicate> predicates = new ArrayList<>();

    // add add criteria to predicates
    for (SearchCriteria criteria : searchCriterialist) {
      String fieldName = criteria.getKey();
      if (fieldName.equals("offerType")) {
        predicates.add(builder.equal(root.get("store").get("id"), criteria.getValue()));
      }
    }
    return builder.and(predicates.toArray(new Predicate[0]));
  }
}
