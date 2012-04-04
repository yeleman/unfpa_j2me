/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package unfpa;
import java.util.Date;

/**
 *
 * @author ALou
 */
public class SharedChecks {
    private static final String month_list[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    
    public static int[] formatDateString(Date date_obj) {
        String date = date_obj.toString();
        int day = Integer.valueOf(date.substring(8, 10)).intValue();
        int month = monthFromString(date.substring(4,7));
        int year = Integer.valueOf(date.substring(30, 34)).intValue();
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
        // all fields are required to be filled.
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

}
