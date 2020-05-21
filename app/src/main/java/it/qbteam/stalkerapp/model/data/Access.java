package it.qbteam.stalkerapp.model.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Access implements Serializable {
   private Map<String, String> accessMap  ;

   public Access(String orgName,String access) {
       accessMap = new HashMap<>();
       String value = access;

       accessMap.put(orgName, value);
   }

   public Map<String,String> getAccessExit(){
       return accessMap;
   }

}
