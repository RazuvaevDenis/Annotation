package netcracker;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class Test {

    private static final Logger log=Logger.getLogger(Test.class.getName());
    private static ScanProject scanpr;
    public static void main(String[] args) {
        try {
            scanpr=new ScanProject();
            Map map=scanpr.ScanClasses();
            scanpr.ScanMap(map.get("netcracker.tmpClasses.ClassA"));
            scanpr.ScanMap(map.get("netcracker.tmpClasses.ClassA"));
        } catch (IllegalAccessException e) {
            log.log(Level.ERROR,"Error "+e.getMessage()+ " ",e);
        } catch (InstantiationException e) {
            log.log(Level.ERROR,"Error "+e.getMessage()+ " ",e);
        } catch (InvocationTargetException e) {
            log.log(Level.ERROR,"Error "+e.getMessage()+ " ",e);
        }
    }
}
