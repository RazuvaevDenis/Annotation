package netcracker;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
public class Test {

    private static final Logger log=Logger.getLogger(Test.class.getName());
    private static final ScanProject scanpr=new ScanProject();
    public static void main(String[] args) {
        try {
            scanpr.ScanClasses();
            LazyInvoke lazyInvoke=new LazyInvoke(scanpr.getInitMethods(),scanpr.getMap());
            lazyInvoke.ScanMap();
        } catch (IllegalAccessException e) {
            log.log(Level.ERROR,"Error "+e.getMessage()+ " ",e);
        } catch (InstantiationException e) {
            log.log(Level.ERROR,"Error "+e.getMessage()+ " ",e);
        } catch (InvocationTargetException e) {
            log.log(Level.ERROR,"Error "+e.getMessage()+ " ",e);
        }
    }
}
