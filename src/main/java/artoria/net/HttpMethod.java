package artoria.net;

/**
 * Http method.
 * @author Kahle
 */
public enum HttpMethod {

    /**
     * Http get method.
     */
    GET(     false ),

    /**
     * Http post method.
     */
    POST(    true  ),

    /**
     * Http put method.
     */
    PUT(     true  ),

    /**
     * Http delete method.
     */
    DELETE(  false ),

    /**
     * Http head method.
     */
    HEAD(    false ),

    /**
     * Http options method.
     */
    OPTIONS( false ),

    /**
     * Http trace method.
     */
    TRACE(   false ),
    ;

    private final boolean hasBody;

    HttpMethod(boolean hasBody) {

        this.hasBody = hasBody;
    }

    /**
     * Check if this HTTP method has/needs a request body.
     * @return If body needed
     */
    public final boolean hasBody() {

        return this.hasBody;
    }

}
