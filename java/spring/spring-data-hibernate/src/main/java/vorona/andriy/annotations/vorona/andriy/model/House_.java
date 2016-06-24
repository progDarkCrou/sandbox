package vorona.andriy.annotations.vorona.andriy.model;

import vorona.andriy.model.House;
import vorona.andriy.model.User;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(House.class)
public abstract class House_ {

	public static volatile SingularAttribute<House, Integer> number;
	public static volatile SingularAttribute<House, String> street;
	public static volatile SingularAttribute<House, User> user;

}

