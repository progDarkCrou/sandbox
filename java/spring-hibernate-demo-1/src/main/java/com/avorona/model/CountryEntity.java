package com.avorona.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by avorona on 29.06.16.
 */
@Entity
public class CountryEntity {

  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  private String name;

  @OneToMany(mappedBy = "country", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
  private Set<DistrictEntity> districts = new HashSet<>();

  @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.REFRESH})
  private ContinentEntity continent;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<DistrictEntity> getDistricts() {
    return districts;
  }

  public void setDistricts(Set<DistrictEntity> districts) {
    this.districts = districts;
  }

  public CountryEntity addDistrict(DistrictEntity district) {
    if (!this.getDistricts().contains(district)) {
      this.getDistricts().add(district);
      district.setCountry(this);
    }
    return this;
  }

  public ContinentEntity getContinent() {
    return continent;
  }

  public void setContinent(ContinentEntity continent) {
    this.continent = continent;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "CountryEntity{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", districts=" + districts +
        ", continent=" + continent +
        '}';
  }
}
