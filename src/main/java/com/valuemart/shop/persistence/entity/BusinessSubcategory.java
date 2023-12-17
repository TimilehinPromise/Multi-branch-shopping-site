package com.valuemart.shop.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="business_subcategory")
@NamedQuery(name="BusinessSubcategory.findAll", query="SELECT b FROM BusinessSubcategory b")
public class BusinessSubcategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="category_id")
    private BusinessCategory businessCategory;


    @JsonIgnore
    @OneToMany(mappedBy="businessSubcategory")
    private List<Product> products;

    @Column(name="is_deleted")
    private boolean deleted;

}
