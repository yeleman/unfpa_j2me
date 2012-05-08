/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package unfpa;
import java.util.Date;

/**
 * @author ALou
 */
public class SharedChecks {
    private static final String month_list[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    
    public static int[] formatDateString(Date date_obj) {
        String date = date_obj.toString();
        int day = Integer.valueOf(date.substring(8, 10)).intValue();
        int month = monthFromString(date.substring(4,7));
        int start = 24;
        int end = 28;
        if (date.length()==34){
            start = 30;
            end = 34;
        }
        int year = Integer.valueOf(date.substring(start, end)).intValue();
        int list_date[] = {day, month, year};
        return list_date;
    }

    public static int monthFromString(String month_str) {
        int i;
        for(i=0; i<=month_list.length; i++){
            if(month_list[i].equals(month_str)){
                return i + 1;
            }
        }
        return 1;
    }

    public static boolean isDateValide(Date date_obj) {
        int array[] = formatDateString(date_obj);
        int day = array[0];
        int month = array[1];
        int year = array[2];

        Date now = new Date();
        int now_array[] = formatDateString(now);
        int now_day = now_array[0];
        int now_month = now_array[1];
        int now_year = now_array[2];

        if (now_year < year){
            return false;
        }
        else {
            if (now_month < month){
                return false;
            }
            else {
                if (now_day < day){
                    return false;
                }
            }
        }
        return true;
    }

    private static int[] previous_date( int day, int month, int year) {
        int new_month[] = {day, month, year};
        if (month > 1){
            new_month[1] = month -1;
            new_month[2] = year;
        }
        else{
            new_month[1] = 12;
            new_month[2] = year - 1;
        }
        return new_month;
    }

    private static int[] dateU5() {
        Date now = new Date();
        int i;
        int array[] = formatDateString(now);
        int now_day = array[0];
        int now_month = array[1];
        int now_year = array[2];
        
        // calcule date 5 ans
        int d[] = {now_day, now_month, now_year};

        for(i=0; i<=60; i++){
            d = previous_date(d[0], d[1], d[2]);
        }

        int list_date[] = {d[0], d[1], d[2]};
        return list_date;
    }

    private static boolean is_before(int day1, int month1, int year1, int day2, int month2, int year2) {

        if (year1 < year2) {
            return true;
        }
        else if (year1 == year2) {

            if (month1 < month2){
                return true;
            }
            else if (month1 == month2){
                if (day1 < day2){
                    return true;
                }
                else {
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public static boolean Under5(Date dob) {
        // all fields are required to be filled.
        int array[] = formatDateString(dob);
        int dob_day = array[0];
        int dob_month = array[1];
        int dob_year = array[2];

        int[] dateU5 = dateU5();
        int u5_day = dateU5[0];
        int u5_month = dateU5[1];
        int u5_year = dateU5[2];
        if (is_before(u5_day, u5_month, u5_year, dob_day, dob_month, dob_year) == true)
            return true;
        else
            return false;
    }

        public static boolean compareDobDod(Date dob, Date dod) {
        // all fields are required to be filled.
        int array[] = formatDateString(dob);
        int dob_day = array[0];
        int dob_month = array[1];
        int dob_year = array[2];

        int[] dateU5 = formatDateString(dod);
        int dod_day = dateU5[0];
        int dod_month = dateU5[1];
        int dod_year = dateU5[2];

        if (is_before(dod_day, dod_month, dod_year, dob_day, dob_month, dob_year) == true)
            return true;
        else
            return false;
    }

    public static boolean ValidateCode(String code) {
        if (code.length() == 3 && code.indexOf(" ") == -1) {
            return false;
        }
        else {
            return true;
        }
    }
    public static String addzero(int num){
        String snum = "";
        if (num < 10)
            snum = "0" + num;
        else
            snum = snum + num;
        return snum;
    }

}
