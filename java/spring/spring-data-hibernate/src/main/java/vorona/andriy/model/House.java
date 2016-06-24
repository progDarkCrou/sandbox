package vorona.andriy.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Created by avorona on 29.12.15.
 */
@Entity
public class House {

  private String street;

  @Id
  @Column(unique = true)
  private Integer number;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private User user;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public House() {
  }

  public House(Integer number, String street) {
    this.number = number;
    this.street = street;
  }

  public House(String street, City city, Integer number) {
    this.street = street;
    this.number = number;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }
}
