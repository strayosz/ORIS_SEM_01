package org.example.orissem01.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.orissem01.models.User;
import org.example.orissem01.services.UserService;

import java.io.IOException;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException{
        ServletContext servletContext = filterConfig.getServletContext();
        this.userService = (UserService) servletContext.getAttribute("userService");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession(false);

        if ((session == null || session.getAttribute("userLogin") == null)){
            ((HttpServletResponse) response).sendRedirect("/slotSwap/welcome");
        } else {
            User user = userService.findUserByLogin((HttpServletRequest) request);
            if (user.getRole().equalsIgnoreCase("админ")) {
                filterChain.doFilter(request, response);
            } else {
                ((HttpServletResponse) response).sendRedirect("/slotSwap/welcome");
            }
        }
    }
}
