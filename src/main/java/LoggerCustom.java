import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class LoggerCustom {


    public Logger getLogger(String nombre){

        FileAppender appender = new FileAppender();

        appender.setFile("output/resultado-"+nombre+".log");
        appender.setLayout(new PatternLayout("%d{HH:mm:ss,SSS} %-5p %c - %m%n"));
        appender.activateOptions();

        // Get logger and add appender
        Logger logger = Logger.getLogger(nombre);
        logger.setAdditivity(false);
        logger.addAppender(appender);

        // Remove appender
        //logger.removeAppender(appender);
        return logger;
    }

}