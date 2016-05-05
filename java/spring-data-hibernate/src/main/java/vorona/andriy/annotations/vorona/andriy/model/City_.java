package vorona.andriy.annotations.vorona.andriy.model;

import vorona.andriy.model.City;
import vorona.andriy.model.House;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(City.class)
public abstract class City_ {

	public static volatile SingularAttribute<City, Integer> zipCode;
	public static volatile SetAttribute<City, House> houses;
	public static volatile SingularAttribute<City, Long> id;
	public static volatile SingularAttribute<City, String> title;

}

