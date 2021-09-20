package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.Contact;
import com.example.demo.models.Dorm;
import com.example.demo.models.Student;
import com.example.demo.models.Class;
import com.example.demo.services.ClassService;
import com.example.demo.services.ContactService;
import com.example.demo.services.DomService;
import com.example.demo.services.StudentService;

@Controller
public class Controller1 {
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private ContactService contactService;
	
	@Autowired
	private DomService dormService;
	
	@Autowired
	private ClassService ClassService;
	
	
	
	@GetMapping("/")
	public String indez() {
		return "redirect:/dashboard";
	}
	
	
	@GetMapping("/dashboard")
	public String Dashboard(Model viewModel) {
		List<Student> allStudents = this.studentService.getAllStudents();
		viewModel.addAttribute("allStudents", allStudents);
		List<Dorm> allDorms = this.dormService.getAllDom();
		viewModel.addAttribute("allDorms", allDorms);
		return "Dashboard.jsp";
	}
	@GetMapping("/students/new")
	public String newStudent(@ModelAttribute("student") Student student, Model viewModel) {//need model to display to frontend from DB
		
		return "addStudent.jsp";
	}

	@GetMapping("/contacts/new")
	public String newContact(@ModelAttribute("contact") Contact contact, Model viewModel) {//need model to display to frontend from DB
		List<Student> allStudents = this.studentService.getAllStudents();//store everything in a list
		viewModel.addAttribute("allStudents", allStudents);

		return "addContact.jsp";
	}

	@GetMapping("/dorms/create")
	public String newDorm(@ModelAttribute("dorm") Dorm dorm) {
		
		return "addDorm.jsp";
	}

	@GetMapping("/dorms/{id}")
	public String newStudent(Model viewModel, @PathVariable("id") Long id) {//need model to display to frontend from DB
		//in order to grab the list of Students:	
		List<Student> allStudents = this.studentService.getAllStudents();//store everything in a list
		viewModel.addAttribute("allStudents", allStudents);

		Dorm showDorm = this.dormService.getSingleDorm(id);
		viewModel.addAttribute("dorm", showDorm);
		
		return "showDorm.jsp";
	}
	

	@PostMapping("/students/create")
	public String addStudent(@Valid @ModelAttribute("student") Student student, BindingResult result) {
	     if(result.hasErrors()) {
	         return "addStudent.jsp";//re-render the page if there are errors
	     }    
	     
	    this.studentService.createStudent(student);
	    
	    return "redirect:/dashboard";
	}
	
	@PostMapping("/contacts/create")
	public String addContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result) {
	     if(result.hasErrors()) {
	         return "addContact.jsp";//re-render the page if there are errors
	     }    
	     
	    this.contactService.createContact(contact);
	    
	    return "redirect:/dashboard";
	}

	@PostMapping("/dorms/create")
	public String addDorm(@RequestParam(value="name", required=false) String name) {
		Dorm newDorm = new Dorm();
		newDorm.setName(name);

		this.dormService.createDorm(newDorm);
		
		return "redirect:/dashboard";
	}

	@PostMapping("/dorms/{dorm_id}/add")
	public String addStudentToDorm(@PathVariable("dorm_id") Long dorm_id, @RequestParam(value="student", required=false) Long student_id) {
		Student student = this.studentService.getSingleStudent(student_id);
		Dorm dorm = this.dormService.getSingleDorm(dorm_id);
		
		student.setDorm(dorm);
	     
	    this.studentService.updateStudent(student);
	    
	    return "redirect:/dashboard";
	}



	//DELETIONS
	//delete entire student
	@GetMapping("/delete/{id}")
	public String deleteStudent(@PathVariable("id") Long id) {
		this.studentService.deleteStudent(id);
		
		return "redirect:/dashboard";
	}

	//delete entire dorm - note only works if dorm has no students assigned to it
	@GetMapping("/deleteDorm/{id}")
	public String deleteDorm(@PathVariable("id") Long id) {
		this.dormService.deleteDorm(id);
		
		return "redirect:/dashboard";
	}
	


	//UPDATES
	//remove dorm from student
	//	Have a method handler in the controller for the following example url: /dorms/3/remove?student=1. This method should remove student with id 1 from the dorm with id 3. Remove multiple students from a single dormitory.
	@GetMapping("/dorms/{dorm_id}/remove")
	public String removeStudentromDorm(@PathVariable("dorm_id") Long dorm_id, @RequestParam(value="student", required=false) Long student_id) {
		Student student = this.studentService.getSingleStudent(student_id);
//		Dorm dorm = this.dormService.getSingleDorm(dorm_id); //not used
		
		student.setDorm(null);
	     
	    this.studentService.updateStudent(student);
	    
	    return "redirect:/dashboard";
	}
	
	
	@RequestMapping("/classes")
	public String classList(Model model) {
		List<Class> classes = ClassService.AllClasses();
		model.addAttribute("classes", classes);
		return "classes.jsp";
	}
	

	@RequestMapping("/classes/secret")
	public String classListSecret(Model model) {
		List<Class> classes = ClassService.AllClasses();
		model.addAttribute("classes", classes);
		return "classesadv.jsp";
	}
	
	@PostMapping(value = "/classes/create")
	public String createClassProcess(@RequestParam("className") String className,
			@RequestParam("instructorName") String instructorName, RedirectAttributes r) {
		if (className.length() < 2) {
			r.addFlashAttribute("error", "Class name must be at least two characters long");
			return "redirect:/classes";
		}
		else if (instructorName.length() < 2) {
			r.addFlashAttribute("error", "Instructor name must be at least two characters long");
			return "redirect:/classes";
		}
		else {
			Class cla = new Class();
			cla.setClassName(className);
			cla.setInstructorName(instructorName);
			ClassService.CreateClass(cla);
			return "redirect:/classes/" + cla.getId();
		}
	}
	
	@RequestMapping("/classes/{id}")
	public String showClass (@PathVariable("id") Long id, Model model) {
		Class c = ClassService.getSingleClass(id);
		model.addAttribute("c", c);
		List<Student> added = c.getStudents();
		model.addAttribute("added", added);
		
		List<Student> allStudents = studentService.getAllStudents();
		List<Student> menu = new ArrayList<Student>();
		menu.addAll(allStudents);
		menu.removeAll(added);
		
		model.addAttribute("menu", menu);
		return "class.jsp";
	}
	
	@RequestMapping("/classes/edit/{id}")
	public String editClass(Model model, @PathVariable("id") Long id) {
		Class c = ClassService.getSingleClass(id);
		model.addAttribute("c", c);
		return "editclass.jsp";
	}
	
	@PostMapping(value = "/classes/edit/{id}/process")
	public String editClassProcess(@PathVariable("id") Long id, @RequestParam("className") String className,
			@RequestParam("instructorName") String instructorName, RedirectAttributes r) {
		if (className.length() < 2) {
			r.addFlashAttribute("error", "Class name must be at least two characters long");
			return "redirect:/classes/edit/" + id;
		}
		else if (instructorName.length() < 2) {
			r.addFlashAttribute("error", "Instructor name must be at least two characters long");
			return "redirect:/classes/edit/" + id;
		}
		else {
			Class c = ClassService.getSingleClass(id);
			c.setClassName(className);
			c.setInstructorName(instructorName);
			ClassService.CreateClass(c);
			return "redirect:/classes/" + id;
		}
	}
	
	@RequestMapping("/classes/delete/{id}")
	public String deleteClass(@PathVariable("id") Long id) {
		ClassService.deleteClass(id);
		return "redirect:/classes";
	}
	
	@RequestMapping("students/{id}")
	public String showStudent(@PathVariable("id") Long id, Model model) {
		Student student = studentService.getSingleStudent(id);
		model.addAttribute("student", student);
		List<Class> added = student.getClasses();
		model.addAttribute("added", added);
		
		List<Class> allClasses = ClassService.AllClasses();
		List<Class> menu = new ArrayList<Class>();
		menu.addAll(allClasses);
		menu.removeAll(added);
		
		model.addAttribute("menu", menu);
		return "studentinfo.jsp";
	}
	
	@RequestMapping("/students/{id}/addclass")
	public String addClassToStudent (@PathVariable("id") Long id, @RequestParam ("name") String cl) {
		Student student = studentService.getSingleStudent(id);
		Class c = ClassService.findClassName(cl);
		student.getClasses().add(c);
		studentService.createStudent(student);
		return "redirect:/students/" + id;
	}
	
	@RequestMapping("/students/{student_id}/dropclass/{class_id}")
	public String removeClassFromStudent (@PathVariable("student_id") Long student_id,
			@PathVariable("class_id") Long class_id) {
		Student student = studentService.getSingleStudent(student_id);
		Class c = ClassService.getSingleClass(class_id);
		student.getClasses().remove(c);
		studentService.createStudent(student);
		return "redirect:/students/" + student_id;
	}
	
	@RequestMapping("/classes/{id}/addstudent")
	public String addStudentToClass (@PathVariable("id") Long id, @RequestParam ("name") String s) {
		Class c = ClassService.getSingleClass(id);
		Student student = studentService.getSingleStudent(id);
		c.getStudents().add(student);
		ClassService.CreateClass(c);
		return "redirect:/classes/" + id;
	}
	
	@RequestMapping("/classes/{class_id}/dropclass/{student_id}")
	public String removeStudentFromClass (@PathVariable("class_id") Long class_id,
			@PathVariable("student_id") Long student_id) {
		Class c = ClassService.getSingleClass(class_id);
		Student student = studentService.getSingleStudent(student_id);
		c.getStudents().remove(student);
		ClassService.CreateClass(c);
		return "redirect:/classes/" + class_id;
	}
	
	
	
	
	
}
