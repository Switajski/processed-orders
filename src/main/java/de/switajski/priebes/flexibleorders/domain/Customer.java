package de.switajski.priebes.flexibleorders.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooJpaEntity
public class Customer {

    /**
     */
    @Column(unique = true)
    private String shortName;

    /**
     */
    private String name1;

    /**
     */
    private String name2;

    /**
     */
    @NotNull
    private String street;
}
