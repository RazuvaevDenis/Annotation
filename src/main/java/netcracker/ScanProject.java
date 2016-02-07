package netcracker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import netcracker.tmpClasses.ClassA;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

/**
 * Created by Denis on 01.02.2016.
 */
public class ScanProject {
    private static final Logger log = Logger.getLogger(Test.class.getName());

    public List<String> getInitMethods() {
        return initMethods;
    }

    public void setInitMethods(List<String> initMethods) {
        this.initMethods = initMethods;
    }

    private List<String> initMethods;

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    private Map map;
    public void ScanClasses() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Set<Class<?>> classes = getClasses();
        map = new HashMap<String, Object>();
        initMethods=new ArrayList<>();
        for (Class cl : classes) {
            if (cl.isAnnotationPresent(Component.class)) {
                Object result = cl.newInstance();
                map.put(result.getClass().getSimpleName(), result);
                Method[] methods = cl.getDeclaredMethods();
                for (int j = 0; j < methods.length; j++) {
                    Initialize initialize = methods[j].getAnnotation(Initialize.class);
                    if (initialize != null) {
                        if (!initialize.lazy()) {
                            methods[j].setAccessible(true);
                            methods[j].invoke(result);
                            initMethods.add(result.getClass().getName()+methods[j].getName());
                        }
                    }
                }
            }
        }
    }

    private Set<Class<?>> getClasses() {
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("netcracker/tmpClasses"))));
        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);
        return classes;
    }
}
