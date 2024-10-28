package com.task.studentPortal.utils.services;

import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Random;

@Service
public class CommonService {

    private final static String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890$";

    public static Integer countWorkingDays(Calendar calendar) {
        int workingDays = 0;
//        int month = calendar.get(Calendar.MONTH); // Get the month (0-based)

        // Iterate through all days of the month
        for (int day = 1; day <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            // Check if the day is not Saturday (7) or Sunday (1)
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                workingDays++;
            }
        }

        return workingDays;
    }

    public String generateRandomString() {
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 20) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }
}
