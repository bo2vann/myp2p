package com.think.android.p2p.base;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amarsoft.support.android.animate.AnimationShow;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验类
 * Created by Think on 2017/11/12.
 */

public class CommonValid {

    private static CommonValid commonValid;
    Context context;

    public static void init(Context context) {
        commonValid = new CommonValid();
        commonValid.setContext(context.getApplicationContext());
    }

    public static CommonValid getInstance(Context context) {
        if (commonValid == null) {
            commonValid = new CommonValid();
            commonValid.setContext(context.getApplicationContext());
        }
        return commonValid;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static final int MOBILE_MODE = 0x10;
    public static final int LOGON_PASSWORD_MODE = 0x20;
    public static final int NAME_MODE = 0x30;
    public static final int ID_MODE = 0x40;
    public static final int PAY_PASSWORD_MODE = 0x50;
    public static final int OTHER_MODE = 0x00;

    public static final String MOBILE_VALID = "^(13|14|15|16|17|18|19)[0-9]{9}$";
    public static final String LOGON_PASSWORD_VALID = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
    public static final String NAME_VALID = "[\u4e00-\u9fa5]{2,15}";
    public static final String PAY_PASSWORD_VALID = "^\\d{6}$";


    public boolean check(EditText editText, int mode, boolean checkEmpty, int emptyId, int validId) {
        String value = editText.getText().toString();
        if (checkEmpty && !checkEmpty(value)) {
            prompt(editText);
            toast(emptyId);
            return false;
        }
        boolean result = true;
        switch (mode) {
            case MOBILE_MODE:
                result = checkMobile(value);
                break;
            case LOGON_PASSWORD_MODE:
                result = checkLogonPassword(value);
                break;
            case NAME_MODE:
                result = checkName(value);
                break;
            case ID_MODE:
                result = checkID(value);
                break;
            case PAY_PASSWORD_MODE:
                result = checkPayPassword(value);
                break;
        }
        if (!result) {
            prompt(editText);
            toast(validId);
        }
        return result;
    }

    private void prompt(EditText editText) {
        AnimationShow.shakeView(editText);
        editText.findFocus();
    }

    private void toast(int id) {
        if (context != null) {
            Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean checkEmpty(String value) {
        if (value == null || "".equals(value)) {
            return false;
        }
        return true;
    }

    public static boolean checkMobile(String mobile) {
        if (!checkEmpty(mobile)) return true;
        Pattern pattern = Pattern.compile(MOBILE_VALID);
        Matcher matcher = pattern.matcher(mobile);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public static boolean checkLogonPassword(String password) {
        if (!checkEmpty(password)) return true;
        Pattern pattern = Pattern.compile(LOGON_PASSWORD_VALID);
        Matcher matcher = pattern.matcher(password);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public static boolean checkName(String name) {
        if (!checkEmpty(name)) return true;
        Pattern pattern = Pattern.compile(NAME_VALID);
        Matcher matcher = pattern.matcher(name);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public static boolean checkPayPassword(String password) {
        if (!checkEmpty(password)) return true;
        Pattern pattern = Pattern.compile(PAY_PASSWORD_VALID);
        Matcher matcher = pattern.matcher(password);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public static boolean checkID(String id) {
        id = id.toLowerCase();
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (id.length() != 15 && id.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return false;
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (id.length() == 18) {
            Ai = id.substring(0, 17);
        } else if (id.length() == 15) {
            Ai = id.substring(0, 6) + "19" + id.substring(6, 15);
        }
        if (!isNumeric(Ai)) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return false;
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(
                    strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                errorInfo = "身份证生日不在有效范围。";
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return false;
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            return false;
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;
        System.out.println(Ai);

        if (id.length() == 18 && !Ai.equals(id)) {
            errorInfo = "身份证无效，不是合法的身份证号码";
            return false;
        }
        // =====================(end)=====================
        return true;
    }

    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

}
