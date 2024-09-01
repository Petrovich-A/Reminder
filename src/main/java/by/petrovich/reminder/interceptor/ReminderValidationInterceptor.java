package by.petrovich.reminder.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.regex.Matcher;

import static by.petrovich.reminder.constant.Constant.ID_REMINDER_PATTERN;
import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@Component
@Log4j2
public class ReminderValidationInterceptor implements HandlerInterceptor {

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
            log.error("Invalid page or size value: page={}, size={}", page, size);
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
                log.error("Invalid id supplied: {}", id);
                response.sendError(SC_BAD_REQUEST, "Invalid id value.");
                return false;
            }
        }
        return true;
    }
}
