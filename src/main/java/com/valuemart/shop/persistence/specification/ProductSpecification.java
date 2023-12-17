package com.valuemart.shop.persistence.specification;

import com.valuemart.shop.persistence.entity.Branch;
import com.valuemart.shop.persistence.entity.BusinessCategory;
import com.valuemart.shop.persistence.entity.BusinessSubcategory;
import com.valuemart.shop.persistence.entity.Product;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProductSpecification implements Specification<Product> {

    private final List<SearchCriteria> list;

    public ProductSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        //create a new predicate list
        List<Predicate> predicates = new ArrayList<>();

        //add add criteria to predicates
        for (SearchCriteria criteria : list) {
            if (criteria.getOperation().equals(SearchOperation.GREATER_THAN)) {
                predicates.add(builder.greaterThan(
                        root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {
                predicates.add(builder.lessThan(
                        root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN_EQUAL) && criteria.getValue() instanceof LocalDateTime) {
                predicates.add(builder.greaterThanOrEqualTo(
                        root.get(criteria.getKey()),
                        LocalDateTime.parse(criteria.getValue().toString())));
            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN_EQUAL) && criteria.getValue() instanceof LocalDateTime) {
                predicates.add(builder.lessThanOrEqualTo(
                        root.get(criteria.getKey()), LocalDateTime.parse(criteria.getValue().toString())));
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

        return builder.and(predicates.toArray(new Predicate[0]));
    }
    public static Specification<Product> searchInBranchWithKeyword(Set<Long> branchIds, String keyword) {
        return (root, query, criteriaBuilder) -> {
            // Join with branches
            Join<Product, Branch> branchesJoin = root.join("branches");
            Join<Product, BusinessCategory> categoryJoin = root.join("businessCategory", JoinType.LEFT);
            Join<Product, BusinessSubcategory> subcategoryJoin = root.join("businessSubcategory", JoinType.LEFT);


            // Convert keyword to lowercase for case-insensitive search
            String lowerKeyword = keyword.toLowerCase();


            // Build predicates for case-insensitive search
            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + lowerKeyword + "%");
            Predicate brandPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")), "%" + lowerKeyword + "%");
            Predicate skuPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("skuId")), "%" + lowerKeyword + "%");
            Predicate categoryPredicate = criteriaBuilder.like(criteriaBuilder.lower(categoryJoin.get("name")), "%" + lowerKeyword + "%");
            Predicate subcategoryPredicate = criteriaBuilder.like(criteriaBuilder.lower(subcategoryJoin.get("name")), "%" + lowerKeyword + "%");
            Predicate branchPredicate = branchesJoin.get("id").in(branchIds);

            Predicate notDeletedPredicate = criteriaBuilder.isFalse(root.get("deleted"));
            Predicate enabledPredicate = criteriaBuilder.isTrue(root.get("enabled"));

            // Combine predicates
            Predicate combinedPredicate = criteriaBuilder.and(
                    branchPredicate,
                    criteriaBuilder.or(namePredicate,categoryPredicate,subcategoryPredicate, brandPredicate,skuPredicate,
                            notDeletedPredicate,
                            enabledPredicate)
            );

            query.distinct(true); // Ensure distinct results
            return combinedPredicate;
        };
    }

}
