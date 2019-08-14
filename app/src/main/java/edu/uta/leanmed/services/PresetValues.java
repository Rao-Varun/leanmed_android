package edu.uta.leanmed.services;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vaibhav's Console on 4/21/2019.
 */

public class PresetValues {
    public static Map<Integer,String> userStatuses;
    static {
        userStatuses=new HashMap<Integer,String>();
        userStatuses.put(1,"Active");
        userStatuses.put(2,"Pending");
        userStatuses.put(3,"Disabled");
        userStatuses.put(4,"Declined");
    }
}
