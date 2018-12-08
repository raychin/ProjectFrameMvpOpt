package com.ray.project.commons;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 利用反射自动赋值
 */
public class AssignValueForAttributeUtils {
    /** 正则表达式 用于匹配属性的第一个字母 {@value [a-zA-Z]} **/
    private static final String REGEX = "[a-zA-Z]";
    @SuppressWarnings("rawtypes")
    public static String JsonToModel(String json, Object obj){
    	String flag = "";
		try {
			JSONObject obj1 = new JSONObject(json);
			String result = (String) obj1.getString("result");
			setAttrributeValue(obj, "result", result);
			if(result.equals("fail")) {
				flag = obj1.getString("msg");
			}else {
				JSONObject obj2 = (JSONObject) obj1.get("data");
				Iterator it = obj2.keys();
                while(it.hasNext()) {
                    String str = (String) it.next();
                    setAttrributeValue(obj, str, obj2.get(str));
                    flag = "信息保存成功";
                }
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return flag;
    }

    /**
     * 利用发射，将json字符串转为实体
     * @param json 完整的json字符串
     * @param obj 最终转换的实体，该实体需要实例化
     * @param keyResult 结果成功标志key
     * @param markFail 结果失败标志
     * @param keyMsg 结果失败消息key
     * @param keyValue 需要转化为实体的json标志key
     * @return
     */
    public static String JsonToModel(String json, Object obj, String keyResult, String markFail,
                                     String keyMsg, String keyValue){
        String flag = "";
        try {
            JSONObject obj1 = new JSONObject(json);
            String result = (String) obj1.getString(keyResult);
            setAttrributeValue(obj, keyResult, result);
            if(result.equals(markFail)) {
                flag = obj1.getString(keyMsg);
            }else {
                JSONObject obj2 = (JSONObject) obj1.get(keyValue);
                Iterator it = obj2.keys();
                while(it.hasNext()) {
                    String str = (String) it.next();
                    setAttrributeValue(obj, str, obj2.get(str));
                    flag = "信息保存成功";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    @SuppressWarnings("rawtypes")
    public static void setAttrributeValue(Object obj, String attribute, Object value) {
        String method_name = convertToMethodName(attribute,obj.getClass(), true);
        Method[] methods = obj.getClass().getMethods();
        for (Method method : methods) {
            /**
             *     因为这里只是调用bean中属性的set方法，属性名称不能重复
             * 所以set方法也不会重复，所以就直接用方法名称去锁定一个方法
             * （注：在java中，锁定一个方法的条件是方法名及参数）
             * **/
            if(method.getName().equals(method_name))
            {
                Class[] parameterC = method.getParameterTypes();
                try {
                    /**如果是基本数据类型时（如int、float、double、byte、char、boolean）
                     * 需要先将Object转换成相应的封装类之后再转换成对应的基本数据类型
                     * 否则会报 ClassCastException**/
                    if(parameterC[0] == int.class) {
                        method.invoke(obj,((Integer)value).intValue());
                        break;
                    } else if (parameterC[0] == float.class) {
                        method.invoke(obj, ((Float)value).floatValue());
                        break;
                    } else if (parameterC[0] == double.class) {
                        method.invoke(obj, ((Double)value).doubleValue());
                        break;
                    } else if (parameterC[0] == byte.class) {
                        method.invoke(obj, ((Byte)value).byteValue());
                        break;
                    } else if (parameterC[0] == char.class) {
                        method.invoke(obj, ((Character)value).charValue());
                        break;
                    } else if (parameterC[0] == boolean.class) {
                        method.invoke(obj, ((Boolean)value).booleanValue());
                        break;
                    } else if(parameterC[0] == String.class) {
                    	method.invoke(obj, value.toString());
                    } else {
                        method.invoke(obj,parameterC[0].cast(value));
                        break;
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } 
            }
        }
    }
    
    @SuppressWarnings("rawtypes")
    private static String convertToMethodName(String attribute, Class objClass, boolean isSet) {
        /** 通过正则表达式来匹配第一个字符 **/
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(attribute);
        StringBuilder sb = new StringBuilder();
        /** 如果是set方法名称 **/
        if(isSet) {
            sb.append("set");
        } else {
        /** get方法名称 **/
            try {
                Field attributeField = objClass.getDeclaredField(attribute);
                /** 如果类型为boolean **/
                if(attributeField.getType() == boolean.class || attributeField.getType() == Boolean.class)
                {
                    sb.append("is");
                }else
                {
                    sb.append("get");
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        /** 针对以下划线开头的属性 **/
        if(attribute.charAt(0) != '_' && m.find()) {
            sb.append(m.replaceFirst(m.group().toUpperCase()));
        } else {
            sb.append(attribute);
        }
        return sb.toString();
    }
    
    public static Object getAttrributeValue(Object obj, String attribute) {
        String methodName = convertToMethodName(attribute, obj.getClass(), false);
        Object value = null;
        try {
            /** 由于get方法没有参数且唯一，所以直接通过方法名称锁定方法 **/
            Method methods = obj.getClass().getDeclaredMethod(methodName);
            if(methods != null) {
                value = methods.invoke(obj);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return value;
    }

}