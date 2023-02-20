package com.nmagpie.tfc_ie_addon.common.blockenties;
import java.lang.reflect.Field;
import sun.misc.Unsafe;

/**
 * @author {link:https://github.com/Arnuh}
 * @since Feb 21, 2021
 **/
public class FieldUtil {

    private static Unsafe unsafe;

    static{
        try{
            final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    static void setFinalStatic(Field field, Object value) throws Exception {
        Object fieldBase = unsafe.staticFieldBase(field);
        long fieldOffset = unsafe.staticFieldOffset(field);

        unsafe.putObject(fieldBase, fieldOffset, value);
    }
}