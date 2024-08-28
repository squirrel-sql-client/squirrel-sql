package net.sourceforge.squirrel_sql.plugins.hibernate.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TypedValueList implements Serializable
{
   private List<TypedValue> _list = new ArrayList<>();

   public void add(Object value, String fieldName, Class<?> fieldType)
   {
      _list.add(new TypedValue(asString(value), isNull(value), fieldName, asQualifiedTypeName(fieldType)));
   }

   public void addUntyped(Object value)
   {
      _list.add(new TypedValue(asString(value), isNull(value), tryGetTypeName(value), TypedValue.UNKNOWN_FIELD_NAME));
   }

   private String asString(Object value)
   {
      if( isNull(value) )
      {
         return "<null>";
      }

      try
      {
         return "" + value;
      }
      catch(Exception e)
      {
         return TypedValue.UNKNOWN_FIELD_VALUE;
      }
   }

   private String asQualifiedTypeName(Class<?> fieldType)
   {
      if(null == fieldType)
      {
         return TypedValue.UNKNOWN_TYPE_NAME;
      }

      try
      {
         return fieldType.getName();
      }
      catch(Exception e)
      {
         return TypedValue.UNKNOWN_TYPE_NAME;
      }
   }

   private boolean isNull(Object value)
   {
      return null == value;
   }

   private String tryGetTypeName(Object value)
   {
      try
      {
         if(isNull(value))
         {
            return TypedValue.UNKNOWN_TYPE_NAME;
         }
         return value.getClass().getName();
      }
      catch(Exception e)
      {
         return TypedValue.UNKNOWN_TYPE_NAME;
      }
   }


   public String asString()
   {
      if(_list.isEmpty())
      {
         return "<noFields>";
      }

      switch( _list.get(0).getTypedValuesDisplayMode() )
      {
         case JSON_MODE:
            return "{\n" + _list.stream().map(e -> e.asString()).collect(Collectors.joining(",\n")) + "\n}";
      }

      return _list.stream().map(e -> e.asString()).collect(Collectors.joining("|"));
   }

   /**
    * @return true if visiting is to continue
    */
   public boolean visitValues(TypedValueVisitor visitor)
   {
      for( TypedValue typedValue : _list )
      {
         if(false == visitor.visit(typedValue))
         {
            return false;
         }
      }

      return true;
   }
}
