package com.stockai.trader.view.framework;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.*;
import java.util.*;
import java.io.*;

@WebServlet(urlPatterns = "/")
public class DispatcherServlet extends HttpServlet
{
    private Map<String, Dispatcher> dispatcherMappings;
    private ViewEngine viewEngine;

    @Override
    public void init() throws ServletException
    {
        this.dispatcherMappings = scanControllers();
        this.viewEngine = new ViewEngine(getServletContext());
    }

    /**
     * 掃描初始化所有控制器列表
     */
    private Map<String, Dispatcher> scanControllers()
    {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getRequestURI().substring(req.getContextPath().length());
        // 根据路径查找GetDispatcher:
        Dispatcher dispatcher = this.dispatcherMappings.get(path);
        if (dispatcher == null)
        {
            // 未找到返回404:
            resp.sendError(404);
            return;
        }
        // 调用Controller方法获得返回值:
        ModelAndView mv = dispatcher.invokeGet(req, resp);
        // 允许返回null:
        if (mv == null)
        {
            return;
        }
        // 允许返回`redirect:`开头的view表示重定向:
        if (mv.view.startsWith("redirect:"))
        {
            resp.sendRedirect(mv.view.substring(9));
            return;
        }
        // 将模板引擎渲染的内容写入响应:
        PrintWriter pw = resp.getWriter();
        this.viewEngine.render(mv, pw);
        pw.flush();
    }
}
