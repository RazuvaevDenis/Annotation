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

    public List<String> getInitInstance() {
        return initInstance;
    }

    public void setInitInstance(List<String> initInstance) {
        this.initInstance = initInstance;
    }

    private Map map;

    public ScanProject(){
        map=new HashMap<String,Object>();
    }

    private List<String> initInstance;
    public Map ScanClasses() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Set<Class<?>> classes = getClasses();
        initInstance=new ArrayList<>();
        for (Class cl : classes) {
            if (cl.isAnnotationPresent(Component.class)) {
                Object result = cl.newInstance();
                Method[] methods = cl.getDeclaredMethods();
                for (int j = 0; j < methods.length; j++) {
                    if(methods[j].isAnnotationPresent(Initialize.class)) {
                        if (!methods[j].getAnnotation(Initialize.class).lazy()) {
                            methods[j].setAccessible(true);
                            methods[j].invoke(result);
                            initInstance.add(result.getClass().getName()+methods[j].getName());
                        }
                    }
                }
                map.put(result.getClass().getName(), result);
            }
        }
        return map;
    }

    public void ScanMap(Object instance) throws InvocationTargetException, IllegalAccessException {
        Method[] methods=instance.getClass().getDeclaredMethods();
            for(int i=0;i<methods.length;i++) {
                if (methods[i].isAnnotationPresent(Initialize.class)) {
                    if (methods[i].getAnnotation(Initialize.class).lazy()) {
                        if (!initInstance.contains(instance.getClass().getName() + methods[i].getName())) {
                            methods[i].setAccessible(true);
                            methods[i].invoke(instance);
                            initInstance.add(instance.getClass().getName() + methods[i].getName());
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
