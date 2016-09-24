package me.songrgg.freshmeat.web;

import me.songrgg.freshmeat.helper.I18NTranslator;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestAttributes;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;


/**
 * @author songrgg
 * @since 1.0
 */
class CustomErrorAttributes extends DefaultErrorAttributes {

  enum ENVIRONMENT {
    PRODUCTION,
    DEBUG
  }

  private ENVIRONMENT env;

  public CustomErrorAttributes() {
    this(ENVIRONMENT.DEBUG);
  }

  CustomErrorAttributes(ENVIRONMENT env) {
    this.env = env;
  }

  @Override
  public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes,
                                                boolean includeStackTrace) {
    includeStackTrace = (this.env == ENVIRONMENT.DEBUG) || includeStackTrace;

    Map<String, Object> errorAttributes = new LinkedHashMap<String, Object>();

    addErrorDetails(errorAttributes, requestAttributes, includeStackTrace);

    if (this.env == ENVIRONMENT.DEBUG) {
      addStatus(errorAttributes, requestAttributes);
      addErrorDetails(errorAttributes, requestAttributes, includeStackTrace);
      addPath(errorAttributes, requestAttributes);
    }
    return errorAttributes;
  }

  private void addStatus(Map<String, Object> errorAttributes,
                         RequestAttributes requestAttributes) {
    Integer status = getAttribute(requestAttributes,
            "javax.servlet.error.status_code");
    if (status == null) {
      errorAttributes.put("status", 999);
      errorAttributes.put("error", "None");
      return;
    }
    errorAttributes.put("status", status);
    try {
      errorAttributes.put("error", HttpStatus.valueOf(status).getReasonPhrase());
    } catch (Exception ex) {
      // Unable to obtain a reason
      errorAttributes.put("error", "Http Status " + status);
    }
  }

  private void addPath(Map<String, Object> errorAttributes,
                       RequestAttributes requestAttributes) {
    String path = getAttribute(requestAttributes, "javax.servlet.error.request_uri");
    if (path != null) {
      errorAttributes.put("path", path);
    }
  }

  private void addErrorDetails(Map<String, Object> errorAttributes,
                               RequestAttributes requestAttributes, boolean includeStackTrace) {
    Throwable error = getError(requestAttributes);
    if (error != null) {
      while (error instanceof ServletException && error.getCause() != null) {
        error = error.getCause();
      }
      addErrorMessage(errorAttributes, error);
      if (this.env == ENVIRONMENT.DEBUG) {
        errorAttributes.put("exception", error.getClass().getName());
      }
      if (includeStackTrace) {
        addStackTrace(errorAttributes, error);
      }
    }

    if (this.env == ENVIRONMENT.DEBUG) {
      Object message = getAttribute(requestAttributes, "javax.servlet.error.message");
      if ((!StringUtils.isEmpty(message) || errorAttributes.get("message") == null)
              && !(error instanceof BindingResult)) {
        errorAttributes.put("message",
                StringUtils.isEmpty(message) ? "No message available" : message);
      }
    }
  }

  private void addErrorMessage(Map<String, Object> errorAttributes, Throwable error) {
    BindingResult result = extractBindingResult(error);
    if (result == null) {
      HashMap<String, String> _error = new HashMap<String, String>();

      _error.put("message", getMessage(error));
      _error.put("message_human", getMessageHuman(error));

      List<Object> errors = new LinkedList<Object>();
      errors.add(_error);
      errorAttributes.put("errors", errors);
      return;
    }

    if (result.getErrorCount() <= 0) {
      return;
    }

    List<Object> errors = new LinkedList<Object>();
    for (ObjectError _error : result.getAllErrors()) {
      HashMap<String, Object> _tmp = new HashMap<String, Object>();
      if (this.env == ENVIRONMENT.DEBUG) {
        // Get the error class
        Class<?> clz = _error.getClass();

        // Fetch the class fields
        Field[] fields = clz.getDeclaredFields();

        for (Field field : fields) {
          System.out.println(field.getName());
        }

        Method[] methods = clz.getMethods();
        for (Method method : methods) {
          try {
            if (method.getName().startsWith("get")) {
              String key = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
              Object val = method.invoke(_error);
              if (method.getName().equalsIgnoreCase("getdefaultmessage")) {
                _tmp.put("message_human", val);
                _tmp.put("message", val);
              } else {
                _tmp.put(key, val);
              }
            } else if (method.getName().startsWith("is")) {
              String key = method.getName().substring(2, 3).toLowerCase() + method.getName().substring(3);
              _tmp.put(key, method.invoke(_error));
            }
          } catch (IllegalAccessException | InvocationTargetException ignored) {
          }
        }
      } else {
        _tmp.put("message", getMessage(_error));
        _tmp.put("message_human", getMessageHuman(_error));
      }
      errors.add(_tmp);
    }
    errorAttributes.put("errors", errors);

    if (this.env == ENVIRONMENT.DEBUG) {
      errorAttributes.put("message",
              "Validation failed for object='" + result.getObjectName()
                      + "'. Error count: " + result.getErrorCount());
    }
  }

  private void addStackTrace(Map<String, Object> errorAttributes, Throwable error) {
    StringWriter stackTrace = new StringWriter();
    error.printStackTrace(new PrintWriter(stackTrace));
    stackTrace.flush();
    errorAttributes.put("trace", stackTrace.toString());
  }

  private BindingResult extractBindingResult(Throwable error) {
    if (error instanceof BindingResult) {
      return (BindingResult) error;
    }
    if (error instanceof MethodArgumentNotValidException) {
      return ((MethodArgumentNotValidException) error).getBindingResult();
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
    return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
  }

  private String getMessage(ObjectError error) {
    return error.getDefaultMessage();
  }

  private String getMessageHuman(ObjectError error) {
    String i18n = I18NTranslator.getInstance().getMessage(error.getDefaultMessage(), null, null);
    if (i18n != null) {
      return i18n;
    }
    return error.getDefaultMessage();
  }

  private String getMessage(Throwable exception) {
    if (exception instanceof NullPointerException) {
      return "SERVER_NULL_POINTER_ERROR";
    } else if (exception instanceof HttpMessageNotReadableException) {
      return "MESSAGE_UNREADABLE";
    } else if (exception instanceof UndeclaredThrowableException) {
      UndeclaredThrowableException exception1 = (UndeclaredThrowableException) exception;
      exception = exception1.getUndeclaredThrowable();
    }
    return exception.getMessage();
  }

  private String getMessageHuman(Throwable exception) {
    if (exception instanceof NullPointerException) {
      return "服务器错误,请联系开发人员";
    } else if (exception instanceof HttpMessageNotReadableException) {
      return "信息读取错误";
    } else if (exception instanceof UndeclaredThrowableException) {
      UndeclaredThrowableException exception1 = (UndeclaredThrowableException) exception;
      exception = exception1.getUndeclaredThrowable();
    }

    // message priority: message i18n > message_human > message
    String i18n = I18NTranslator.getInstance().getMessage(exception.getMessage(), null, null);
    if (i18n != null) {
      return i18n;
    }
    return exception.getMessage();
  }
}
