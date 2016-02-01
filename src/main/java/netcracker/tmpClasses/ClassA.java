package netcracker.tmpClasses;

import netcracker.Component;
import netcracker.Initialize;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
/**
 * Created by Denis on 09.01.2016.
 */
@Component
public class ClassA {
    private static final Logger log=Logger.getLogger(ClassA.class.getName());

    @Initialize(lazy=false)
    public void FirstMethod()
    {
        log.log(Level.INFO,"First method with annotation without lazy parameter called");
    }
    @Initialize(lazy=false)
    public void SecondMethod()
    {
        log.log(Level.INFO,"Second method with annotation without lazy parameter called");
    }
    @Initialize(lazy=true)
    public void ThirdMethod()
    {
        log.log(Level.INFO,"Method with annotation with lazy parameter called");
    }
}
