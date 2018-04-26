package com.example.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.modal.Course;
import com.google.gson.Gson;

/**
 * Servlet implementation class CalendarJsonServlet
 */
@WebServlet("/CalendarJsonServlet")
public class CalendarJsonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CalendarJsonServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*response.getWriter().append("Served at: ").append(request.getContextPath());*/
		 
/*		List l = new ArrayList();
		 
		 CalendarDTO c = new CalendarDTO();
		 c.setId(1);
		 c.setStart("2013-07-28");
		 c.setEnd("2013-07-29");
		 c.setTitle("Task in Progress");
		 
		CalendarDTO d = new CalendarDTO();
		 c.setId(2);
		 c.setStart("2013-07-26");
		 c.setEnd("2013-08-28");
		 c.setTitle("Task in Progress");
		 
		 l.add(c);
		l.add(d);*/
		
		List<Course> l = new ArrayList<Course>();
		
		Course c = new Course();
		c.setCourse_desc("sample");
		c.setStart_date("2018-04-18");
		c.setEnd_date("2018-04-20");
		
		l.add(c);
		 
		response.setContentType("application/json");
		 response.setCharacterEncoding("UTF-8");
		 PrintWriter out = response.getWriter();
		 out.write(new Gson().toJson(l));
		 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
