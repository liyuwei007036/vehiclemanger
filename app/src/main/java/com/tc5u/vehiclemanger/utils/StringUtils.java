package com.tc5u.vehiclemanger.utils;


import java.math.BigInteger;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhulx on 2016/10/23.
 */
public class StringUtils {

    public static String substring(String str, int len) {
        if (str == null) return null;
        if (str.length() > len) {
            str = str.substring(0, len);
        }
        return str;
    }


    public static boolean areNotEmpty(String... strings) {
        if (strings == null || strings.length == 0)
            return false;

        for (String string : strings) {
            if (string == null || "".equals(string)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotEmpty(String string) {
        return string != null && !string.trim().equals("");
    }

    public static boolean areNotBlank(String... strings) {
        if (strings == null || strings.length == 0)
            return false;

        for (String string : strings) {
            if (string == null || "".equals(string.trim())) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String string) {
        return string != null && !"".equals(string.trim());
    }

    public static boolean isBlank(String string) {
        return string == null || "".equals(string.trim());
    }

    public static boolean isEmpty(String str) {
        return isBlank(str);
    }

    public static long toLong(String value, Long defaultValue) {
        try {
            if (value == null || "".equals(value.trim()))
                return defaultValue;
            value = value.trim();
            if (value.startsWith("N") || value.startsWith("n"))
                return -Long.parseLong(value.substring(1));
            return Long.parseLong(value);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    public static int toInt(String value, int defaultValue) {
        try {
            if (value == null || "".equals(value.trim()))
                return defaultValue;
            value = value.trim();
            if (value.startsWith("N") || value.startsWith("n"))
                return -Integer.parseInt(value.substring(1));
            return Integer.parseInt(value);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    public static BigInteger toBigInteger(String value, BigInteger defaultValue) {
        try {
            if (value == null || "".equals(value.trim()))
                return defaultValue;
            value = value.trim();
            if (value.startsWith("N") || value.startsWith("n"))
                return new BigInteger(value).negate();
            return new BigInteger(value);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    public static boolean match(String string, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    public static boolean isNumeric(String str) {
        if (str == null)
            return false;
        for (int i = str.length(); --i >= 0; ) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57)
                return false;
        }
        return true;
    }

    public static String escapeHtml(String text) {
        if (text == null) return null;

        char[] chars = text.toCharArray();
        StringBuilder sb = new StringBuilder(chars.length + 20);

        for (int i = 0, len = chars.length; i < len; i++) {

            // 去除不可见字符
            if ((int) chars[i] < 32) continue;

            switch (chars[i]) {
                case '<':
                    sb.append("&#60;");
                    break;
                case '>':
                    sb.append("&#62;");
                    break;
                case '&':
                    sb.append("&#38;");
                    break;
                case '"':
                    sb.append("&#34;");
                    break;
                case '\'':
                    sb.append("&#39;");
                    break;
                case '/':
                    sb.append("&#47;");
                    break;
                default:
                    sb.append(chars[i]);
                    break;
            }
        }
        return sb.toString();
    }

    public static String fix(String str) {
        return (str == null) ? "" : str;
    }

    public static String firstLower(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static String firstUpper(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static int StringToInt(String str, int defaul) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return defaul;
        }
    }

    public static long StringToLong(String str, long defaul) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return defaul;
        }
    }

    public static String uuid() {
        String s = UUID.randomUUID().toString();
        return s.replaceAll("-", "");
    }


    /**
     * Check whether <code>string</code> has been set to something other than
     * <code>""</code> or <code>null</code>.
     *
     * @param string the <code>String</code> to check
     * @return a boolean indicating whether the string was non-empty (and
     * non-null)
     */
    public final static boolean stringSet(String string) {
        return (string != null) && !"".equals(string);
    }

    /**
     * Return <code>string</code>, or <code>defaultString</code> if
     * <code>string</code> is <code>null</code> or <code>""</code>. Never
     * returns <code>null</code>.
     * <p>
     * <p>
     * Examples:
     * </p>
     * <p>
     * <pre>
     * // prints "hello"
     * String s=null;
     * System.out.println(TextUtils.noNull(s,"hello");
     *
     * // prints "hello"
     * s="";
     * System.out.println(TextUtils.noNull(s,"hello");
     *
     * // prints "world"
     * s="world";
     * System.out.println(TextUtils.noNull(s, "hello");
     * </pre>
     *
     * @param string        the String to check.
     * @param defaultString The default string to return if <code>string</code> is
     *                      <code>null</code> or <code>""</code>
     * @return <code>string</code> if <code>string</code> is non-empty, and
     * <code>defaultString</code> otherwise
     * @see #stringSet(String)
     */
    public final static String noNull(String string, String defaultString) {
        return (stringSet(string)) ? string : defaultString;
    }


    /**
     * Return <code>string</code>, or <code>""</code> if <code>string</code> is
     * <code>null</code>. Never returns <code>null</code>.
     * <p>
     * Examples:
     * </p>
     * <p>
     * <pre>
     * // prints 0
     * String s = null;
     * System.out.println(TextUtils.noNull(s).length());
     *
     * // prints 1
     * s = &quot;a&quot;;
     * System.out.println(TextUtils.noNull(s).length());
     * </pre>
     *
     * @param string the String to check
     * @return a valid (non-null) string reference
     */
    public final static String noNull(String string) {
        return noNull(string, "");
    }


    public final static String htmlEncode(String s) {
        return htmlEncode(s, true);
    }

    /**
     * 获得文件扩展名
     *
     * @param filename
     * @return
     */
    public static String extensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * Escape html entity characters and high characters (eg "curvy" Word
     * quotes). Note this method can also be used to encode XML.
     *
     * @param s                  the String to escape.
     * @param encodeSpecialChars if true high characters will be encode other wise not.
     * @return the escaped string
     */
    public final static String htmlEncode(String s, boolean encodeSpecialChars) {
        s = noNull(s);

        StringBuilder str = new StringBuilder();

        for (int j = 0; j < s.length(); j++) {
            char c = s.charAt(j);

            // encode standard ASCII characters into HTML entities where needed
            if (c < '\200') {
                switch (c) {
                    case '"':
                        str.append("&quot;");

                        break;

                    case '&':
                        str.append("&amp;");

                        break;

                    case '<':
                        str.append("&lt;");
                        break;

                    case '>':
                        str.append("&gt;");

                        break;

                    default:
                        str.append(c);
                }
            }
            // encode 'ugly' characters (ie Word "curvy" quotes etc)
            else if (encodeSpecialChars && (c < '\377')) {
                String hexChars = "0123456789ABCDEF";
                int a = c % 16;
                int b = (c - a) / 16;
                String hex = "" + hexChars.charAt(b) + hexChars.charAt(a);
                str.append("&#x" + hex + ";");
            }
            // add other characters back in - to handle charactersets
            // other than ascii
            else {
                str.append(c);
            }
        }

        return str.toString();
    }

}
