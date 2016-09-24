package me.songrgg.freshmeat.form;

import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import me.songrgg.freshmeat.exception.ServerLogicalException;

/**
 * @author songrgg
 * @since 1.0
 */
public class BaseForm<T> {

  private Logger logger = Logger.getLogger(this.getClass());

  /**
   * Get the specified-class model from the form.
   *
   * @param clazz the specified class
   * @param model the given model
   * @return a filled model
   * @throws ServerLogicalException Throws server logic exception when get model failed.
   */
  public T getModel(Class<T> clazz, T model) throws ServerLogicalException {
    try {
      if (model == null) {
        model = clazz.newInstance();
      }
    } catch (InstantiationException | IllegalAccessException e) {
      Logger.getLogger(this.getClass()).error("fail to instantiate the model: " + e.getMessage());
      throw new ServerLogicalException("SERVER_IS_BUSY");
    }

    Method[] methods = clazz.getMethods();
    for (Method method : methods) {
      String methodName = method.getName();
      if (methodName.startsWith("set")) {
        String getMethod = "get" + methodName.substring(3);
        try {
          Method getValueMethod = this.getClass().getMethod(getMethod);
          if (getValueMethod == null) {
            getValueMethod = this.getClass().getMethod("is" + methodName.substring(2));
            if (getValueMethod == null) {
              logger.debug("no get value method found");
              continue;
            }
          }

          Object value = getValueMethod.invoke(this);
          method.invoke(model, value);
        } catch (NoSuchMethodException e) {
          logger.info("no such method: " + methodName);
        } catch (InvocationTargetException e) {
          logger.info("invocation target error: " + e.getMessage());
        } catch (IllegalAccessException e) {
          logger.info("illegal access to the method: " + e.getMessage());
        }
      }
    }
    return model;
  }

  public T getModel(Class<T> clazz) throws ServerLogicalException {
    return getModel(clazz, null);
  }

}
