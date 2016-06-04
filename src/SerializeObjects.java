import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * Created by Sid on 5/31/2016.
 */
public class SerializeObjects {

    private static String delimiter = ",";

    public void setDelimiter(String delimiter) {
        delimiter = delimiter;
    }

    private static String addBracks(String str) {
        return String.format("\"%s\"", str);
    }

    private static void writeHeaders(OutputStreamWriter fw, String[] headers) {

        try {
            for (int i = 0; i < headers.length; i++) {
                fw.append(addBracks(headers[i]));
                if (i < headers.length - 1)
                    fw.append(delimiter);
            }
            fw.append("\r\n");
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    private static <T> boolean writeObject(OutputStreamWriter fw, String[] fieldOrder, T object) {

        Class<?> cls = object.getClass();
        Field field;
        String fieldValue, fieldName, typeName;
        Object valueObj;
        for (int i=0; i<fieldOrder.length; i++) {
            fieldName = fieldOrder[i];
            try {
                field = cls.getDeclaredField(fieldName);
                field.setAccessible(true);
                valueObj = field.get(object);
                if (valueObj == null) {
                    fieldValue = "";
                    System.out.println(String.format("Field \"%s\" not found in object \"%s\"", field.getName(), object.toString()));
                }
                else
                    fieldValue = field.get(object).toString();
                fw.append(addBracks(fieldValue));

                //add delimiter between fields unless it's the last one. in that case write newline
                if (i == fieldOrder.length - 1)
                    fw.append("\r\n");
                else
                    fw.append(delimiter);

            } catch(NoSuchFieldException e) {
                e.printStackTrace();
                return false;
            } catch(IOException e) {
                e.printStackTrace();
                return false;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public static int saveToFile(String dstPath, Iterator<Restaurant> iterator) {
        int success = 0;

        try {
            OutputStreamWriter fw = new OutputStreamWriter(
                    new FileOutputStream(dstPath),
                    Charset.forName("UTF-8").newEncoder()
            );
            SerializableInterface obj = iterator.next();
            String[] headers = obj.getFieldOrder();
            writeHeaders(fw, headers);
            writeObject(fw, headers, obj);
            while (iterator.hasNext()) {
                obj = iterator.next();
                writeObject(fw, headers, obj);
            }
            fw.close();
        } catch(IOException e) {
            e.printStackTrace();
        }


        return success;
    }
}
