package net.sourceforge.squirrel_sql.plugins.hibernate.server;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class ObjectSubstitute implements Serializable
{
   private HashMap<String, PropertySubstitute> _substituteValueByPropertyName = new HashMap<>();
   private MappedClassInfoData _mappedClassInfoData;
   private String _toString;


   public static final String PLAIN_VALUES = "<plain values>";
   private boolean _isPrimitiveTypePersistentCollection = false;
   private HashMap<String, PlainValue> _plainValueByPropertyName = new HashMap<>();

   /**
    * Is seriously meant to be package visible because it must not called nowhere except during initialization in ObjectSubstituteFactory
    */
   ObjectSubstitute(MappedClassInfoData mappedClassInfoData, String toString)
   {
      _mappedClassInfoData = mappedClassInfoData;
      _toString = toString;
   }

   ObjectSubstitute(ClassLoader cl, Collection<Object> primitiveOrUnknownObjectCollection)
   {
      _isPrimitiveTypePersistentCollection = true;

      _toString = PLAIN_VALUES + " size="
                  + primitiveOrUnknownObjectCollection.size()
                  + ", values: " + primitiveOrUnknownObjectCollectionAsString(primitiveOrUnknownObjectCollection, cl);


      Optional<Object> any = primitiveOrUnknownObjectCollection.stream().filter(Objects::nonNull).findAny();
      String className = any.isEmpty() ? "<unknown>" : any.get().getClass().getName();
      String propertyName = "value " + (1);
      HibernatePropertyInfo indentifierHibernatePropertyInfo = new HibernatePropertyInfo(propertyName, className, "<unknown>", new String[]{"<unknown>"});

      _plainValueByPropertyName.put(propertyName, new PlainValue(toPrimitiveType(any.orElse(null), cl), indentifierHibernatePropertyInfo));

      HibernatePropertyInfo[] hibernatePropertyInfos = new HibernatePropertyInfo[primitiveOrUnknownObjectCollection.size() - 1];

      ArrayList<Object> primitiveOrUnknownObjectList = new ArrayList<>(primitiveOrUnknownObjectCollection);
      for ( int i = 1; i < primitiveOrUnknownObjectList.size(); i++)
      {
         className = null == primitiveOrUnknownObjectList.get(i) ? "<unknown>" : primitiveOrUnknownObjectList.get(i).getClass().getName();
         propertyName = "value " + (i + 1);
         hibernatePropertyInfos[i-1] = new HibernatePropertyInfo(propertyName, className, "<unknown>", new String[]{"<unknown>"});

         _plainValueByPropertyName.put(propertyName, new PlainValue(toPrimitiveType(primitiveOrUnknownObjectList.get(i), cl), hibernatePropertyInfos[i-1]));
      }


      _mappedClassInfoData = new MappedClassInfoData(PLAIN_VALUES, "<unknown>", indentifierHibernatePropertyInfo, hibernatePropertyInfos);
      _mappedClassInfoData.setPlainValueArray(true);
   }

   private String primitiveOrUnknownObjectCollectionAsString(Collection<Object> plainValueCollection, ClassLoader cl)
   {
      StringBuilder ret = new StringBuilder();

      int count = 0;
      for( Object o : plainValueCollection )
      {
         if(0 == count)
         {
            ret.append(toPrimitiveType(o, cl));
         }
         else
         {
            ret.append(";").append(toPrimitiveType(o, cl));
         }

         ++count;

         if(count > 20)
         {
            ret.append(" ...");
            break;
         }
      }

      return ret.toString();
   }

   private Object toPrimitiveType(Object o, ClassLoader cl)
   {
      if(null == o || o.getClass().getName().startsWith("java."))
      {
         return o;
      }
      else
      {
         return transformToString(o, cl);
      }
   }

   private String transformToString(Object o, ClassLoader cl)
   {

      StringBuilder sb = new StringBuilder();
      try
      {
         Field[] declaredFields = o.getClass().getDeclaredFields();

         boolean valueAppended = false;
         for( Field f : declaredFields )
         {
            if( valueAppended )
            {
               sb.append("|");
               valueAppended = false;
            }

            try
            {
               f.setAccessible(true);
               Object fieldVal = f.get(o);
               String fieldName = f.getName();
               if(HibernateServerUtil.isInitialized(cl, fieldVal))
               {
                  sb.append(fieldName + "=" + fieldVal);
                  valueAppended = true;
               }
               else
               {
                  sb.append(fieldName + "=<uninitialized>");
                  valueAppended = true;
               }
            }
            catch(Throwable e)
            {
            }
         }
      }
      catch(Throwable e)
      {
         sb.append("" + o);
      }

      return sb.toString();
   }

   void putSubstituteValueByPropertyName(String propertyName, PropertySubstitute propertySubstitute)
   {
      if ( false == _isPrimitiveTypePersistentCollection )
      {
         _substituteValueByPropertyName.put(propertyName, propertySubstitute);
      }
      else
      {
         throw new IllegalStateException("Should not be called for plain values");
      }
   }

   public String getClassName()
   {
      return _mappedClassInfoData.getMappedClassName();
   }

   /**
    * @return May return ObjectSubstitute or plain primitive mapped value
    */
   public Object getValue(String propertyName)
   {
      if ( false == _isPrimitiveTypePersistentCollection )
      {
         return _substituteValueByPropertyName.get(propertyName).getSingleValue();
      }
      else
      {
         return _plainValueByPropertyName.get(propertyName).getValue();
      }
   }

   public String getTypeName(String propertyName)
   {
      if ( false == _isPrimitiveTypePersistentCollection )
      {
         return _substituteValueByPropertyName.get(propertyName).getHibernatePropertyInfo().getClassName();
      }
      else
      {
         return _plainValueByPropertyName.get(propertyName).getHibernatePropertyInfo().getClassName();
      }
   }

   public boolean wasInitialized(String propertyName)
   {
      if ( false == _isPrimitiveTypePersistentCollection )
      {
         return _substituteValueByPropertyName.get(propertyName).isInitialized();
      }
      else
      {
         return true;
      }
   }

   public Collection<? extends ObjectSubstitute> getPersistentCollection(String propertyName)
   {
      return _substituteValueByPropertyName.get(propertyName).getObjectSubstituteCollection();
   }

   public boolean isPrimitiveTypePersistentCollection(String propertyName)
   {
      if ( false == _isPrimitiveTypePersistentCollection )
      {
         return _substituteValueByPropertyName.get(propertyName).isPersistenCollection();
      }
      else
      {
         return false;
      }
   }

   public boolean isNull(String propertyName)
   {
      if ( false == _isPrimitiveTypePersistentCollection )
      {
         return _substituteValueByPropertyName.get(propertyName).isNull();
      }
      else
      {
         return null == _plainValueByPropertyName.get(propertyName).getValue();
      }
   }

   public HibernatePropertyInfo getHibernatePropertyInfo(String propertyName)
   {
      if ( false == _isPrimitiveTypePersistentCollection )
      {
         return _substituteValueByPropertyName.get(propertyName).getHibernatePropertyInfo();
      }
      else
      {
         return _plainValueByPropertyName.get(propertyName).getHibernatePropertyInfo();
      }
   }

   @Override
   public String toString()
   {
      return _toString;
   }

   public MappedClassInfoData getPrimitiveTypePersistentCollectionClassInfo()
   {
      if ( false == _isPrimitiveTypePersistentCollection )
      {
         return null;
      }
      else
      {
         return _mappedClassInfoData;
      }
   }
}
