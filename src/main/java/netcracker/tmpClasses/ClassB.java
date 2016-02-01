package netcracker.tmpClasses;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

public class ClassB {
    private static final Logger log=Logger.getLogger(ClassB.class.getName());

    public void FirstMethod()
    {
        log.log(Level.INFO,"Method in class without annotation");
    }
}
