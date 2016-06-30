package com.avorona.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by avorona on 29.06.16.
 */
@Entity
public class ContinentEntity {

  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  private String name;

  @OneToMany(mappedBy = "continent", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
  private Set<CountryEntity> districts = new HashSet<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<CountryEntity> getDistricts() {
    return districts;
  }

  public void setDistricts(Set<CountryEntity> districts) {
    this.districts = districts;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
