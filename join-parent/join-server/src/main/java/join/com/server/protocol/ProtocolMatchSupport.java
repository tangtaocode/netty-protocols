package join.com.server.protocol;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * @ClassName:  ProtocolMatchSupport   
 * @Description:
 * 根据协议获取类实例  
 * @author: tt1498
 * @date:   2018年8月27日 上午10:01:44
 */
public class ProtocolMatchSupport
{
  private  PathMatchingResourcePatternResolver pathSources = new PathMatchingResourcePatternResolver();
  private  SimpleMetadataReaderFactory metaReader = new SimpleMetadataReaderFactory();
  public final static String  DEFAULT_LOCATION = "/join/**/**.class";
  
  public List<Object> scanClass(Class<?> type) throws IOException{
    return this.scanClass(this.loadProtocols(),DEFAULT_LOCATION, type);
   }
  
  public List<Object> scanClass(List<Class <? extends Protocol>> protocols,Class<?> type) throws IOException{
   return this.scanClass(protocols,DEFAULT_LOCATION, type);
  }
  public List<Object> scanClass(List<Class <? extends Protocol>> protocols,final String location,Class<?> type) throws IOException{
    Assert.notNull(location, "not exist location !");
    List<Object> re = new ArrayList<Object>();
    for(Class <? extends Protocol> pro:protocols){
      re.addAll(this.loadInstance(pro,this.loadMetadata(pathSources.getResources(location)),type));
    }
    return re;
  }
  public List<Object> scanClass(Class <? extends Protocol> protocol,Class<?> type) throws IOException{
    return this.loadInstance(protocol,this.loadMetadata(pathSources.getResources(DEFAULT_LOCATION)),type);
  }
  public List<Object> scanClass(Class <? extends Protocol> protocol,final String location,Class<?> type) throws IOException{
    Assert.notNull(location, "not exist location !");
    return this.loadInstance(protocol,this.loadMetadata(pathSources.getResources(location)),type);
  }
  
  public List<MetadataReader> loadMetadata(Resource[] resources) throws IOException{
      List<MetadataReader> redata = new ArrayList<MetadataReader>();
      for(Resource resource:resources){
        redata.add(metaReader.getMetadataReader(resource));
      }
      return redata;
  }
  
  public List<Object> loadInstance(Class <? extends Protocol> protocol,List<MetadataReader> mredears,Class<?> type){
      List<Object> reservers = new ArrayList<Object>();
      for(MetadataReader mr:mredears){
        if(!mr.getClassMetadata().isInterface()){
          Class<?> allclass =  ClassUtils.resolveClassName(mr.getClassMetadata().getClassName(), null);
          if(this.containInterface(allclass,type)){
            Class<?> serverClass = this.getMatchInterface(allclass, type);
            Assert.notNull(serverClass, "not type classify ");
            if(this.matchProtocol(serverClass, protocol)){
              reservers.add(BeanUtils.instantiate(serverClass));
            }
          }
        }
      }
      return reservers;
  }
  
  public boolean containInterface(Class<?> sclass,Class<?> destclass){
    for(Class<?> calss:sclass.getInterfaces()){
      if(destclass.isAssignableFrom(calss)){
        return true;
      }
    }
    return false;
  }
  
  public Class<?> getMatchInterface(Class<?> sclass,Class<?> destclass){
    for(Class<?> calss:sclass.getInterfaces()){
      if(destclass.isAssignableFrom(calss)){
        return sclass;
      }
    }
    return null;
  }
  
  public boolean matchProtocol(Class<?> sclass,Class <? extends Protocol> protocol){
    if(protocol.isAssignableFrom(sclass)){
      return true;
    }
    Type[] typs = sclass.getGenericInterfaces();
    for(Type ty :typs){
      if(ParameterizedType.class.isAssignableFrom(ty.getClass())){
        ParameterizedType tem = (ParameterizedType)ty;
        if(protocol.isAssignableFrom((Class<?>)tem.getRawType())){
           return true;
        }
        for(Type innerty:tem.getActualTypeArguments()){
          if(protocol.isAssignableFrom((Class<?>)innerty)){
            return true;
         }
        }
      }
      if(protocol.isAssignableFrom(ty.getClass())){
        return true;
      }
    }
    return false;
  }
  @SuppressWarnings("unchecked")
  public List<Class <? extends Protocol>> loadProtocols() throws IOException{
    List<MetadataReader> redata = this.loadMetadata(pathSources.getResources(DEFAULT_LOCATION));
    List<Class <? extends Protocol>> protocols = new ArrayList<Class <? extends Protocol>>();
    for(MetadataReader mr:redata){
      if(mr.getClassMetadata().isInterface()){
        Class<?> allclass =  ClassUtils.resolveClassName(mr.getClassMetadata().getClassName(), null);
        if(this.containInterface(allclass,Protocol.class)){
          Class<?> interfaceClass = this.getMatchInterface(allclass, Protocol.class);
          Assert.notNull(interfaceClass, "not type classify ");
          protocols.add((Class<? extends Protocol>)interfaceClass);
        }
      }
    }
    return protocols;
  }
}
