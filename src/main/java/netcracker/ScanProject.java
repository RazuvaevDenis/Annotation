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
    private static final Logger log=Logger.getLogger(Test.class.getName());
    public void ScanClasses() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Set<Class<?>> classes=getClasses();
        HashMap<String,Method> testmap=new HashMap<>();
        for(Class cl:classes){
            Object result=cl.newInstance();
            if(cl.isAnnotationPresent(Component.class)){
                Method[] methods=cl.getMethods();
                for(int j=0;j<methods.length;j++){
                    if(methods[j].isAnnotationPresent(Initialize.class))
                    {
                        Initialize classann=methods[j].getAnnotation(Initialize.class);
                        if(!classann.lazy())
                            methods[j].invoke(result);
                        testmap.put(cl.getSimpleName()+" "+methods[j].getName(),methods[j]);
                    }
                }
            }
        }
        ClassA classA=new ClassA();
        Initialize methodinit1=testmap.get("ClassA FirstMethod").getAnnotation(Initialize.class);
        Initialize methodinit2=testmap.get("ClassA SecondMethod").getAnnotation(Initialize.class);
        Initialize methodinit3=testmap.get("ClassA ThirdMethod").getAnnotation(Initialize.class);
        if(methodinit1.lazy())
            testmap.get("ClassA FirstMethod").invoke(classA);
        if(methodinit2.lazy())
            testmap.get("ClassA SecondMethod").invoke(classA);
        if(methodinit3.lazy())
            testmap.get("ClassA ThirdMethod").invoke(classA);
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
