package cz.muni.fi.obs.security;

import cz.muni.fi.obs.security.enums.UserScope;
import cz.muni.fi.obs.security.exceptions.AccessDeniedException;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;



@Component
@Profile("!test-disable-security")
public class Security {

    public String getCurrentUserOauthId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public void checkUserIsOwner(String oauthId) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.getName().equals(oauthId)) {
            throw new AccessDeniedException("You are not owner of this account");
        }
    }

    public boolean isUserBanker() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities()
                             .stream()
                             .anyMatch(a -> a.getAuthority().equals(UserScope.Const.BANKER_READ) ||
                                     a.getAuthority().equals(UserScope.Const.BANKER_WRITE));
    }
}
