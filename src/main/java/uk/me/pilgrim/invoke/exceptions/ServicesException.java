package uk.me.pilgrim.invoke.exceptions;


import lombok.Getter;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
@Getter
public class ServicesException extends RuntimeException {
    private final ServicesExceptionType type;
    private boolean logged;


    public ServicesException(Throwable cause, ServicesExceptionType type, String messagePattern, Object... messageArguments) {
        super(getMessage(type, messagePattern, messageArguments), cause);
        this.type = type;
    }

    private static String getMessage(ServicesExceptionType type, String messagePattern, Object[] messageArguments) {
        if (messagePattern == null) return type.getClass().getName() + "." + type.getName();
        return type.getClass().getName() + "." + type.getName() + ": " + new ParameterizedMessage(messagePattern, messageArguments).getFormattedMessage();
    }

    public ServicesException(ServicesExceptionType type){ this(null, type, null); }
    public ServicesException(ServicesExceptionType type, String messagePattern, Object... messageArguments){ this(null, type, messagePattern, messageArguments); }

    public ServicesException logInfo(Logger logger){
        logger.info(getMessage());
        logged = true;
        return this;
    }

    public ServicesException logWarn(Logger logger){
        logger.warn(getMessage());
        logged = true;
        return this;
    }

    public ServicesException logError(Logger logger){
        logger.error(getMessage(), this);
        logged = true;
        return this;
    }
}
