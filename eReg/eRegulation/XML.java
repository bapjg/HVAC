package eRegulation;

import java.lang.reflect.Field;
//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class XML
{
        public String 	header;
        public String 	fileHeader = "<?xml version = '1.0' encoding = 'UTF-8'?>";

        public String getXML(Object object) throws IllegalArgumentException, IllegalAccessException
        {
            String xml 										= "";
            String className								= getClassName(object);

           	LogIt.error("XML", "getXML", "className full " + object.getClass().toString());
           	LogIt.error("XML", "getXML", "className part " + className);
            
            String 	header 									= "<" + getClassName(object) + " ";
            String 	footer 									= "</" + getClassName(object) + ">";
            Boolean hasSubs									= false;
            
            String	inner 									= "type = 'Object' ";
                
            for (Field field : object.getClass().getDeclaredFields())
            {
                String fieldType = getFieldType(field);
                
               	LogIt.error("XML", "getXML", "field name " + field.getName());
               	LogIt.error("XML", "getXML", "field type full " + field.getType().toString());
               	LogIt.error("XML", "getXML", "field type part " + fieldType);
                   
                if 
                (	(fieldType.endsWith("String")	)
                ||  (fieldType.endsWith("Integer")	)    
                ||  (fieldType.endsWith("int")		)
                )
                {
                    inner 									= inner + field.getName()  + " = '" + field.get(object).toString() + "' ";
                }
                else if
                (	(fieldType.endsWith("Long")		)
                ||  (fieldType.endsWith("long")		)
                )
                {
                    // Either Date or Time   
                	inner 									= inner + field.getName()  + " = '" + field.get(object).toString() + "' ";
                }
                else if (fieldType.endsWith("Boolean"))
                {
                    // Yes or No
                	inner 									= inner + field.getName()  + " = '" + field.get(object).toString() + "' ";
                }
                else if (fieldType.endsWith("Class"))		// or SubClass
                {
                    hasSubs									= true; 
                }
                else if (fieldType.endsWith("ArrayList"))
                {
                    hasSubs									= true; 
                }
                else
                {
                    hasSubs									= true; 
//                             for (element : field.get(object).item())
//                             {
//                                     inner = inner + "type = 'Collection'"
//                                     inner = inner + field.getName()  + " = '" + field.get(object).toString() + "' ";
//                             }
                }
            }
            if (hasSubs)
            {
                for (Field field : object.getClass().getDeclaredFields())
                {
                    String fieldType = getFieldType(field);
                    
                   	LogIt.error("XML", "getXML", "field name " + field.getName());
                   	LogIt.error("XML", "getXML", "field type full " + fieldType);
                    
                    if 
                    (	(fieldType.endsWith("String")	)
                    ||  (fieldType.endsWith("Integer")  )
                    ||  (fieldType.endsWith("int") 		)
                    ||  (fieldType.endsWith("Long")		)
                    ||  (fieldType.endsWith("long") 	)
                    ||  (fieldType.endsWith("Boolean")	)
                    )
                    {
                    }
                    if  (fieldType.endsWith("Class"))		// or SubClass
                    {
                       	LogIt.error("XML", "getXML", "CLASS DETECTED");
//                    	System.out.println("CLASS DETECTED");
                    }
                    else if (fieldType.endsWith("ArrayList"))
                    {
                    	
                    	
//                       inner = inner + "<Relays type = 'Collection'";
	                     inner = inner + "XXX" + field.getName()  + "YYY";
	                    
	
	                     for (Field innerField : field.getClass().getDeclaredFields())
	            		 {
	            			inner = inner + getXML(innerField) + "ZZZ"; 
	            		 }
	            		 inner = inner + getXML(field) + "AAA"; 

                    }
                    else
                    {
//                            		XML             inside                                         = new XML();
//                                   
//                                   inner = inner +"> " + inside.getXML(field.get(object));
                    }
                }
                xml 										= header + inner + "> " + footer;
            }
            else
            {
            	xml 										= header + inner + "/> " + footer;
            }
            
            
            return xml;
        }
        public String createXMLFile(Object object) throws IllegalArgumentException, IllegalAccessException
        {
                String xmlFile = fileHeader;
                xmlFile = xmlFile + "<eRegulation>" + getXML(object) +  "</eRegulation>";
                return xmlFile;
        }
        private String getClassName(Object object)
        {
        	String						objectName					= object.getClass().toString();
        	objectName												= objectName.replace("$", ".");
        	objectName												= objectName.substring(objectName.lastIndexOf(".") + 1);
        	
        	return objectName;
        }
        private String getFieldType(Field field)
        {
        	String						fieldName					= field.getType().toString();
        	fieldName												= fieldName.replace("$", ".");
        	fieldName												= fieldName.substring(fieldName.lastIndexOf(".") + 1);
        	
        	return fieldName;
        }

}
