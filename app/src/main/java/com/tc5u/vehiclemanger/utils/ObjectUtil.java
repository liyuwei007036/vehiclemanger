package com.tc5u.vehiclemanger.utils;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;


public class ObjectUtil {
    public static Object[] nullFilter(Object... ps) {
        List<Object> pls = new ArrayList<Object>();
        for (Object p : ps) {
            if (p != null) {
                pls.add(p);
            }
        }
        Object[] parray = new Object[pls.size()];
        for (int i = 0; i < pls.size(); i++) {
            parray[i] = pls.get(i);
        }
        return parray;
    }


    public static <T> Set<T> array2Set(T[] args) {
        Set<T> ret = new HashSet<T>();
        for (T t : args) {
            ret.add(t);
        }
        return ret;
    }


    /**
     * 取得整数值
     *
     * @param o
     * @return
     */
    static public int getInteger(Object o) {
        try {
            if (o instanceof Integer) return (Integer) o;
            if (o == null) return 0;
            return Integer.parseInt(o.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 取得整数值
     *
     * @param o
     * @return
     */
    static public short getShort(Object o) {
        try {
            if (o instanceof Short) return (Short) o;
            if (o == null) return 0;
            return Short.parseShort(o.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 取得字符串值
     *
     * @param o
     * @return
     */
    static public String getString(Object o) {
        try {
            if (o == null) {
                return "";
            } else {
                return o.toString().trim();
            }
        } catch (Exception e) {
            return "";
        }
    }

    static public Boolean getBoolean(Object o) {
        try {
            if (o == null) {
                return false;
            } else {
                if (o instanceof Boolean) return (Boolean) o;
                String s = o.toString().toLowerCase().trim();
                return ("T".equals(s) || "1".equals(s) || "true".equals(s) || "yes".equals(s) || "on".equals(s) || "是".equals(s));
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 取得长整型值
     *
     * @param o
     * @return
     */
    static public long getLong(Object o) {
        try {
            if (o instanceof Long) return (Long) o;
            if (o == null) return 0;
            return Long.parseLong(o.toString());
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     * 取得浮点数值
     *
     * @param o
     * @return
     */
    static public float getFloat(Object o) {
        try {
            if (o instanceof Float) return (Float) o;
            if (o == null) return 0;
            return Float.valueOf(o.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 取得浮点数值
     *
     * @param o
     * @return
     */
    static public double getDouble(Object o) {
        try {
            if (o instanceof Double) return (Double) o;
            if (o == null) return 0;
            return Double.valueOf(o.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 取得浮点数值
     *
     * @param o
     * @return
     */
    static public BigDecimal getBigDecimal(Object o) {
        try {
            if (o instanceof BigDecimal) return (BigDecimal) o;
            if (o == null) return BigDecimal.ZERO;
            return BigDecimal.valueOf(Double.valueOf(o.toString()));
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 取得浮点数值
     *
     * @param o
     * @return
     */
    static public float getFloat(Object o, int precision) {
        try {
            int p = (int) Math.pow(10, precision);
            int v = (int) Math.round(Float.valueOf(o.toString()) * p);
            return ((float) v) / p;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 将字符串转换成为日期类型
     *
     * @param o
     * @return
     */
    public static Date getDate(Object o) {
        try {
            String dateStr = o.toString();
            String[] d = dateStr.split("[^0-9]");
            int l = Math.min(6, d.length);
            Calendar date = Calendar.getInstance();
            int[] di = {0, 0, 0, 0, 0, 0};
            for (int i = 0; i < l; i++) di[i] = Integer.parseInt(d[i]);
            date.set(di[0], di[1] - 1, di[2], di[3], di[4], di[5]);
            date.set(Calendar.MILLISECOND, 0);
            return date.getTime();
        } catch (Exception e) {
            return null;
        }
    }

    static public Object getValue(Object val, Class clazz) {
        try {
            if (val.getClass().equals(clazz)) return val;
            if (clazz.equals(String.class)) return getString(val);
            if (clazz.equals(Long.class)) return getLong(val);
            if (clazz.equals(Boolean.class)) return getBoolean(val);
            if (clazz.equals(Date.class)) return getDate(val);
            if (clazz.equals(Double.class)) return getDouble(val);
            if (clazz.equals(BigDecimal.class)) return getBigDecimal(val);
            if (clazz.equals(Float.class)) return getFloat(val);
            if (clazz.equals(Short.class)) return getShort(val);
        } catch (Exception e) {
        }
        return val;
    }


    public static JSONObject objectToJson(Object obj) {
        JSONObject object = new JSONObject();
        try {
            String cls = obj.getClass().toString();

            if (!cls.contains("com.tc5u")) throw new RuntimeException("当前对象非自定义JAVA类");

            Field[] field = obj.getClass().getDeclaredFields();
            // 遍历所有属性
            for (int j = 0; j < field.length; j++) {
                if (Modifier.isStatic(field[j].getModifiers())) continue;
                if (Modifier.isFinal(field[j].getModifiers())) continue;
                String name = field[j].getName();
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                if (name.startsWith("_")) continue;
                String type = field[j].getGenericType().toString();
                if (!type.contains("class java")) continue;

                Method get = obj.getClass().getMethod("get" + name);
                Object value;
                if (type.contains("Date")) {
                    value = DateUtils.dateToStr((Date) get.invoke(obj));
                } else {
                    value = get.invoke(obj);
                }
                object.put(name.toLowerCase(), value);
            }
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

}
