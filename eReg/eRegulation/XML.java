package eRegulation;

import java.lang.reflect.Field;

public class XML
{
        public String header;
        public String inner;
        public String footer;
        public String fileHeader = "<?xml version = '1.0' encoding = 'UTF-8'>";

        public String getXML(Object object) throws IllegalArgumentException, IllegalAccessException
        {
                String xml = "";
                header = "<" + object.getClass().getName() + " ";
                footer = "</" + object.getClass().getName() + ">";
                
                inner = "type = 'Object' ";
                
                for (Field field : object.getClass().getDeclaredFields())
                {
                        String fieldType = field.getType().toString();
                        
                        if ((fieldType.endsWith("String"))
                        ||  (fieldType.endsWith("Integer"))    
                        ||  (fieldType.endsWith("int")) 
                        )
                        {
                               inner = inner + field.getName()  + " = '" + field.get(object).toString() + "' ";
                        }
                        else if ((fieldType.endsWith("Long"))
                             ||  (fieldType.endsWith("long"))) 
                        {
                               inner = inner + field.getName()  + " = '" + field.get(object).toString() + "' ";
                        }
                        else if (fieldType.endsWith("Boolean"))
                        {
                               inner = inner + field.getName()  + " = '" + field.get(object).toString() + "' ";
                        }
                        else if (fieldType.endsWith("List"))
                        {
//                             for (element : field.get(object).item())
//                             {
//                                     inner = inner + "type = 'Collection'"
//                                     inner = inner + field.getName()  + " = '" + field.get(object).toString() + "' ";
//                             }
                        }
                        else
                        {
                               XML             inside                                         = new XML();
                               
                               inner = inner +"> " + inside.getXML(field.get(object));
                        }
                }
                
                xml = header + inner + "> " + footer;
                
                return xml;
        }
        public String createXMLFile(Object object) throws IllegalArgumentException, IllegalAccessException
        {
                String xmlFile = fileHeader;
                xmlFile = xmlFile + "<eRegulation>" + getXML(object) +  "</eRegulation>";
                return xmlFile;
        }
}
