package cz.muni.fi.obs.security.enums;

public enum UserScope {
    BANKER_READ(Const.BANKER_READ),
    BANKER_WRITE(Const.BANKER_WRITE),
    CUSTOMER_READ(Const.CUSTOMER_READ),
    CUSTOMER_WRITE(Const.CUSTOMER_WRITE);


    private final String scope;

    UserScope(String scope) {
        this.scope = scope;
    }

    public String toString() {
        return this.scope;
    }

    public static class Const {
        public static final String BANKER_READ = "SCOPE_test_read";
        public static final String BANKER_WRITE = "SCOPE_test_write";
        public static final String CUSTOMER_READ = "SCOPE_test_1";
        public static final String CUSTOMER_WRITE = "SCOPE_test_2";
    }
}