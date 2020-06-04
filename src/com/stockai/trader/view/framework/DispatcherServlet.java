package com.stockai.trader.view.framework;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.*;
import java.util.*;
import java.io.*;

@WebServlet(urlPatterns = "/")
public class DispatcherServlet extends HttpServlet
{
    private Map<String, GetDispatcher> getMappings;
    private Map<String, PostDispatcher> postMappings;
    private ViewEngine viewEngine;

    @Override
    public void init() throws ServletException
    {
        this.getMappings = scanGetControllers();
        this.postMappings = scanPostControllers();
        this.viewEngine = new ViewEngine(getServletContext());
    }

    /**
     * 掃描初始化Get控制器列表
     */
    private Map<String, GetDispatcher> scanGetControllers()
    {

    }

    /**
     * 扫描初始化Post控制器列表，同一个Class，Get和Post各用不同的实例
     *
     * @return
     */
    private Map<String, PostDispatcher> scanPostControllers()
    {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getRequestURI().substring(req.getContextPath().length());
        // 根据路径查找Dispatcher:
        GetDispatcher dispatcher = this.getMappings.get(path);
        if (dispatcher == null)
        {
            // 未找到返回404:
            resp.sendError(404);
            return;
        }
        // 调用Controller方法获得返回值:
        ModelAndView mv = dispatcher.invoke(req, resp);
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getRequestURI().substring(req.getContextPath().length());
        // 根据路径查找Dispatcher:
        PostDispatcher dispatcher = this.postMappings.get(path);
        if (dispatcher == null)
        {
            // 未找到返回404:
            resp.sendError(404);
            return;
        }
        // 调用Controller方法获得返回值:
        ModelAndView mv = dispatcher.invoke(req, resp);
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
