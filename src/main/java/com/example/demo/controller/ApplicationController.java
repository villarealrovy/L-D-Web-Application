package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.modal.Course;
import com.example.demo.modal.User;
import com.example.demo.services.CourseService;
import com.example.demo.services.CourseSvc;
import com.example.demo.services.RightsService;
import com.example.demo.services.UserService;
import com.example.demo.utils.WebUtils;

import java.security.Principal;


import org.springframework.security.core.Authentication;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class ApplicationController {
		
	@Autowired
	private UserService userService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private RightsService rightsService;
	
	@RequestMapping("/")
	public String Home() {
		return "index";
	}
	
	
	@RequestMapping("/welcome2")
	public String Welcome(HttpServletRequest request) {
		request.setAttribute("mode", "MODE_HOME");
		return "welcomepage";
	}
	
	@RequestMapping("/register")
	public String registration(HttpServletRequest request) {
		request.setAttribute("mode", "MODE_REGISTER");
		return "welcomepage";
	}
	
	@RequestMapping("/save-user")
	public String registerUser(@ModelAttribute User user, BindingResult bindingResult, HttpServletRequest request ) {
		userService.saveMyUser(user);
		request.setAttribute("mode", "MODE_HOME");
		return "welcomepage";
	}
	

	
	@RequestMapping("/show-users")
	public String showALLUsers(HttpServletRequest request) {
		request.setAttribute("users", userService.showAllUsers());
		request.setAttribute("mode", "ALL_USER");
		return "welcomepage";
	}
	
	@RequestMapping("/delete-user")
	public String deleteUser(@RequestParam int id,HttpServletRequest request) {
		userService.deleteMyUser(id);
		request.setAttribute("users", userService.showAllUsers());
		request.setAttribute("mode", "ALL_USER");
		return "welcomepage";
	}
	
	@RequestMapping("/edit-user")
	public String editUser(@RequestParam int id, HttpServletRequest request) {
		request.setAttribute("user", userService.editUser(id));
		request.setAttribute("mode", "MODE_UPDATE");
		return "welcomepage";
	}
	
	@RequestMapping("/save-edit-user")
	public String registerUser2(@ModelAttribute User user, BindingResult bindingResult, HttpServletRequest request ) {
		userService.saveMyUser(user);
		request.setAttribute("users", userService.showAllUsers());
		request.setAttribute("mode", "ALL_USER");
		return "welcomepage";
	}
	
	
	@RequestMapping("/login-user")
	public String login(HttpServletRequest request) {
		request.setAttribute("mode", "MODE_LOGIN");
		return "welcomepage";
	}
	
	@RequestMapping("/login-success")
	public String loginUser(@ModelAttribute User user, HttpServletRequest request) {
		if(userService.findByUsernameAndPassword(user.getUsername(), user.getPassword())!=null) {
			return "homepage";
		}
		else {
			request.setAttribute("error", "Invalid Credentials");
			request.setAttribute("mode", "MODE_LOGIN");
			return "welcomepage";
		}
		
	}
	
	@RequestMapping("/login-success2")
	public String loginUser2(@ModelAttribute User user, HttpServletRequest request) {
		if(userService.findByUsernameAndPassword(user.getUsername(), user.getPassword())!=null) {
			return "home";
		}
		else {
			request.setAttribute("error", "Invalid Credentials");
			return "index";
		}
		
	}
	

	
	@RequestMapping("/home")
	public String Homepage() {
		return "home";
	}
	
	@RequestMapping("/courseMain")
	public String CourseMain() {
		return "courseMain";
	}
	
	@RequestMapping("/userMain")
	public String UserMain() {
		return "userMain";
	}
	
	@RequestMapping("/report")
	public String Report() {
		return "report";
	}
	
	
	
    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String welcomePage(Model model) {
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is welcome page!");
        return "index";
    }
 
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) {
         
        org.springframework.security.core.userdetails.User loginedUser = (org.springframework.security.core.userdetails.User) ((Authentication) principal).getPrincipal();
 
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
         
        return "adminPage";
    }
 
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {
 
        return "index";
    }
 
    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        return "index";
    }
 
//    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
//    public String userInfo(Model model, Principal principal) {
// 
//        // After user login successfully.
//        String userName = principal.getName();
// 
//        System.out.println("User Name: " + userName);
// 
//        org.springframework.security.core.userdetails.User loginedUser = (org.springframework.security.core.userdetails.User) ((Authentication) principal).getPrincipal();
// 
//        String userInfo = WebUtils.toString(loginedUser);
//        model.addAttribute("userInfo", userInfo);
//        
//        
//        return "homepage";
//    }
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal) {
 
        // After user login successfully.
        String userName = principal.getName();
 
        System.out.println("User Name: " + userName);
 
        org.springframework.security.core.userdetails.User loginedUser = (org.springframework.security.core.userdetails.User) ((Authentication) principal).getPrincipal();
 
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
        
        model.addAttribute("courses", courseService.showAllCourses());
        return "homepage";
    }
    
    
	@RequestMapping("/save-course")
	public String registerCourse(@ModelAttribute Course course, BindingResult bindingResult, HttpServletRequest request, Model model ) {
		courseService.saveMyCourse(course);
		request.setAttribute("mode", "MODE_HOME");
		model.addAttribute("courses", courseService.showAllCourses());
		return "courseMain";   
	}
	
	

    
    
    
    
    
/*	@RequestMapping("/edit-user")
	public String editUser(@RequestParam int id, HttpServletRequest request) {
		request.setAttribute("user", userService.editUser(id));
		request.setAttribute("mode", "MODE_UPDATE");
		return "welcomepage";
	}
	
	@RequestMapping("/save-edit-user")
	public String registerUser2(@ModelAttribute User user, BindingResult bindingResult, HttpServletRequest request ) {
		userService.saveMyUser(user);
		request.setAttribute("users", userService.showAllUsers());
		request.setAttribute("mode", "ALL_USER");
		return "welcomepage";
	}*/
    
    
/*	@RequestMapping("/show-users")
	public String showALLUsers(HttpServletRequest request) {
		request.setAttribute("users", userService.showAllUsers());
		request.setAttribute("mode", "ALL_USER");
		return "welcomepage";
	}*/
    
    
//    @RequestMapping(value = "/courseMain2", method = RequestMethod.GET)
//    public String courseMain(Model model, Principal principal) {
// 
//        // After user login successfully.
//        String userName = principal.getName();
// 
//        System.out.println("User Name: " + userName);
// 
//        org.springframework.security.core.userdetails.User loginedUser = (org.springframework.security.core.userdetails.User) ((Authentication) principal).getPrincipal();
// 
//        String userInfo = WebUtils.toString(loginedUser);
//        model.addAttribute("userInfo", userInfo);
// 
//        return "courseMain";
//    }
    
    @RequestMapping(value = "/courseMain2")
    public String courseMain(Model model) {
    	model.addAttribute("courses", courseService.showAllCourses());
        return "courseMain";
    }
    
/*    @RequestMapping(value = "/userMain2", method = RequestMethod.GET)
    public String userMain(Model model, Principal principal) {
 
        // After user login successfully.
        String userName = principal.getName();
 
        System.out.println("User Name: " + userName);
 
        org.springframework.security.core.userdetails.User loginedUser = (org.springframework.security.core.userdetails.User) ((Authentication) principal).getPrincipal();
 
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
 
        return "userMain";
    }*/
    
    @RequestMapping(value = "/report2", method = RequestMethod.GET)
    public String report(Model model, Principal principal) {
 
        // After user login successfully.
        String userName = principal.getName();
 
        System.out.println("User Name: " + userName);
 
        org.springframework.security.core.userdetails.User loginedUser = (org.springframework.security.core.userdetails.User) ((Authentication) principal).getPrincipal();
 
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
 
        return "report";
    }
 
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied(Model model, Principal principal) {
 
        if (principal != null) {
            org.springframework.security.core.userdetails.User loginedUser = (org.springframework.security.core.userdetails.User) ((Authentication) principal).getPrincipal();
 
            String userInfo = WebUtils.toString(loginedUser);
 
            model.addAttribute("userInfo", userInfo);
 
            String message = "Hi " + principal.getName() //
                    + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);
 
        }
 
        return "403Page";
    }
	
	@RequestMapping("/delete")
	public String delete(HttpServletRequest request) {
		request.setAttribute("mode", "MODE_REGISTER");
		return "welcomepage";
	}
	
  /*  @RequestMapping(value = "/edit-course/{courseid}", method = RequestMethod.GET)
    public String editCourse(@RequestParam int id, Model model, @PathVariable(required = true, name = "courseid") int courseid) {
    	model.addAttribute("courses", courseService.editCourse(courseid));
    	return "courseMain";
    }*/
	
	  @RequestMapping(value = "/edit-course/{courseid}", method = RequestMethod.GET)
	    public String editCourse(Model model, @PathVariable(required = true, name = "courseid") int courseid) {
		    courseService.editCourse(courseid);
	    	return "courseMain";
	    }
	
	@RequestMapping(value="/courseDelete/{courseid}", method = RequestMethod.GET)
	    public String courseDelete(Model model, @PathVariable(required = true, name = "courseid") int courseid) {
		 	courseService.deleteCourse(courseid);
	        model.addAttribute("courses", courseService.showAllCourses());
	        return "courseMain";

	    }
	
/*USER MAINTENANCE*/
	/*Show users*/
	@RequestMapping(value = "/userMain2", method = RequestMethod.GET)
    public String userMain(HttpServletRequest request) {
    	request.setAttribute("rights", rightsService.showAllRights());
        return "userMain";
    }
	
	/*Delete user */	
	@RequestMapping(value="/userDelete/{employeeID}", method = RequestMethod.GET)
    public String userDelete(Model model, @PathVariable(required = true, name = "employeeID") int employeeID) {
        rightsService.deleteMyUser(employeeID);
        model.addAttribute("rights", rightsService.showAllRights());
        return "userMain";
    }
	
}
 