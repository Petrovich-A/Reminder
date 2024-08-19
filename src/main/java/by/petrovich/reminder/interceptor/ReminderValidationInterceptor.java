package by.petrovich.reminder.interceptor;

import by.petrovich.reminder.controller.ReminderController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.regex.Matcher;

import static by.petrovich.reminder.constant.Constant.ID_REMINDER_PATTERN;
import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@Component
public class ReminderValidationInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(ReminderController.class);

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String page = request.getParameter("page");
        String size = request.getParameter("size");
        String requestURI = request.getRequestURI();
        Matcher matcher = ID_REMINDER_PATTERN.matcher(requestURI);
        if (!isPageAndSizeValidated(response, page, size)) {
            return false;
        }
        return isIdValidated(response, matcher);
    }

    private boolean isPageAndSizeValidated(HttpServletResponse response, String page, String size) throws IOException {
        if ((page != null && !page.isEmpty() && Integer.parseInt(page) < 0) ||
                (size != null && !size.isEmpty() && Integer.parseInt(size) <= 0)) {
            logger.error("Invalid page or size value: page={}, size={}", page, size);
            response.sendError(SC_BAD_REQUEST, "Invalid request parameters.");
            return false;
        }
        return true;
    }

    private boolean isIdValidated(HttpServletResponse response, Matcher matcher) throws IOException {
        if (matcher.find()) {
            String idStr = matcher.group(1);
            long id = Long.parseLong(idStr);
            if (id <= 0) {
                logger.error("Invalid id supplied: {}", id);
                response.sendError(SC_BAD_REQUEST, "Invalid id value.");
                return false;
            }
        }
        return true;
    }
}
