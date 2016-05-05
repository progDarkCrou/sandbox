package vorona.andriy.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by avorona on 16.02.16.
 */

@Entity
public class City {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String title;

  @NotNull
  @Column(name = "zip_code")
  private Integer zipCode;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(name = "City_House", joinColumns = @JoinColumn(name = "city_id"), inverseJoinColumns = @JoinColumn(name = "house_number"))
  private Set<House> houses = new HashSet<>();

  public City() {
  }

  public City(String title, Integer zipCode) {
    this.title = title;
    this.zipCode = zipCode;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getZipCode() {
    return zipCode;
  }

  public void setZipCode(Integer zipCode) {
    this.zipCode = zipCode;
  }

  public Set<House> getHouses() {
    return houses;
  }

  public void setHouses(Set<House> houses) {
    this.houses = houses;
  }

  @Override
  public String toString() {
    return "City{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", zipCode=" + zipCode +
            ", houses=" + houses +
            '}';
  }
}
