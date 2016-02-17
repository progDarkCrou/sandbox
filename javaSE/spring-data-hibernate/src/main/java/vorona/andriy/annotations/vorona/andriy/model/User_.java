package vorona.andriy.annotations.vorona.andriy.model;

import vorona.andriy.model.House;
import vorona.andriy.model.User;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile SingularAttribute<User, String> surname;
	public static volatile SingularAttribute<User, String> name;
	public static volatile ListAttribute<User, House> houses;
	public static volatile SingularAttribute<User, Long> id;
	public static volatile SingularAttribute<User, String> username;

}

