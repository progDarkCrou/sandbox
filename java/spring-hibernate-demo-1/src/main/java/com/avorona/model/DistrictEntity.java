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
public class DistrictEntity {

  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  private String name;

  @OneToMany(mappedBy = "district", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
  private Set<CityEntity> cities = new HashSet<>();

  @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
  private CountryEntity country;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<CityEntity> getCities() {
    return cities;
  }

  public void setCities(Set<CityEntity> cities) {
    this.cities = cities;
  }

  public DistrictEntity addCity(CityEntity city) {
    if (!this.getCities().contains(city)) {
      this.getCities().add(city);
      city.setDistrict(this);
    }
    return this;
  }

  public CountryEntity getCountry() {
    return country;
  }

  public void setCountry(CountryEntity country) {
    this.country = country;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
