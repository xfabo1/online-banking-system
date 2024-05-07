package cz.muni.fi.obs.security.annotations;

import cz.muni.fi.obs.security.enums.UserScope;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize(
        "hasAuthority('" + UserScope.Const.CUSTOMER_READ + "') or hasAuthority('" + UserScope.Const.BANKER_READ + "')")
public @interface AuthorityBankerOrCustomerRead {
}
