package cz.muni.fi.obs.etl;

/**
 * Exception for etl errors
 */
public class EtlException extends RuntimeException {

    public EtlException(Throwable cause) {
        super(cause);
    }
}
