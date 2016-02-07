package netcracker;

import netcracker.Initialize;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by Denis on 06.02.2016.
 */
public class LazyInvoke {
    private List list;
    private Map map;
    public LazyInvoke(List list,Map map){
        this.list=list;
        this.map=map;
    }
    public void ScanMap() throws InvocationTargetException, IllegalAccessException {
        for(Object instance: map.values()){
            Method[] methods=instance.getClass().getDeclaredMethods();
            for(int i=0;i<methods.length;i++){
                Initialize initialize=methods[i].getAnnotation(Initialize.class);
                if(initialize!=null){
                    if(!list.contains(instance.getClass().getName()+methods[i].getName())){
                        methods[i].setAccessible(true);
                        methods[i].invoke(instance);
                        list.add(instance.getClass().getName()+methods[i].getName());
                    }
                }
            }
        }
    }
}
