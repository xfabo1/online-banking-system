package cz.muni.fi.obs.integration;

import cz.muni.fi.obs.security.Security;
import cz.muni.fi.obs.security.exceptions.AccessDeniedException;


public class TestSecurity extends Security {

    @Override
    public String getCurrentUserOauthId() {
        return "111111@muni.cz";
    }

    @Override
    public void checkUserIsOwner(String oauthId) throws AccessDeniedException {
    }

    @Override
    public boolean isUserBanker() {
        return true;
    }
}
