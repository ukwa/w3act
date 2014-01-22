package controllers;

import java.util.Map;

import play.mvc.Controller;

public class AbstractController extends Controller {
        
        protected static String getQueryParam(String name) {
                String[] value = request().queryString().get(name);
                if (value == null)
                        return null;
                
                if (value.length == 0)
                        return null;
                
                return value[0];
        }
        
        protected static int getQueryParamAsInt(String name, int defaultValue) {
                String[] param = request().queryString().get(name);
                if (param == null)
                        return defaultValue;
                
                if (param.length < 1)
                        return defaultValue;
                
                try {
                        return Integer.parseInt(param[0]);
                } catch (Throwable t) {
                        return defaultValue;
                }
        }
        
        protected static double getQueryParamAsDouble(String name, double defaultValue) {
                String[] param = request().queryString().get(name);
                if (param == null)
                        return defaultValue;
                
                if (param.length < 1)
                        return defaultValue;
                
                try {
                        return Double.parseDouble(param[0]);
                } catch (Throwable t) {
                        return defaultValue;
                }
        }
        
        protected static String getFormParam(String name) {
//        		Logger.info("getFormParam: " + request().body().asFormUrlEncoded());
                Map<String, String[]> formParams = request().body().asFormUrlEncoded();
                if (formParams == null)
                        return null;
//                Logger.info("form params: " + formParams);
                String[] values = formParams.get(name);
//                Logger.info("values: " + values);
                if (values == null)
                        return null;
                
                if (values.length < 1)
                        return null;
                
                return values[0];
        }
        
}