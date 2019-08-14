package com.codegym.cms.controller;

import com.codegym.cms.model.Department;
import com.codegym.cms.model.Employee;
import com.codegym.cms.model.EmployeeForm;
import com.codegym.cms.service.DepartmentService;
import com.codegym.cms.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindingResult;
import java.io.File;
import java.io.IOException;


@Controller
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    Environment env;

    @ModelAttribute("departments")
    public Page<Department> departments(Pageable pageable){
        return departmentService.findAll(pageable);
    }

    @GetMapping("/employees")
    public ModelAndView listEmployee(@PageableDefault(size = 3,sort = "salary",direction = Sort.Direction.DESC ) Pageable pageable){
        Page<Employee> employees = employeeService.findAll(pageable);
        ModelAndView modelAndView = new ModelAndView("/employee/list");
        modelAndView.addObject("employees",employees);
        return modelAndView;
    }

    @GetMapping("/create-employee")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/employee/create");
        modelAndView.addObject("employeeform", new EmployeeForm());
        return modelAndView;
    }

    @PostMapping("/create-employee")
    public ModelAndView saveProduct(@ModelAttribute("employeeform") EmployeeForm employeeform,
                                    BindingResult bindingResult){


        // lay ten file
        MultipartFile multipartFile = employeeform.getAvatar();
        String fileName = multipartFile.getOriginalFilename();
        String fileUpload = env.getProperty("file_upload").toString();

        // luu file len server
        try {
            FileCopyUtils.copy(employeeform.getAvatar().getBytes(), new File(fileUpload + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ModelAndView modelAndView = new ModelAndView("/employee/create");

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errorMessage",bindingResult.getAllErrors());
            return modelAndView;
        }
        Employee employeeObject = new Employee(employeeform.getName(),employeeform.getBirthDate(),employeeform.getAddress(),employeeform.getSalary(), fileName,employeeform.getDepartment());
        employeeService.save(employeeObject);
        modelAndView.addObject("employeeform", new EmployeeForm());
        modelAndView.addObject("message","Created.");

        return modelAndView;
    }

    @GetMapping("/edit-employee/{id}")
    public ModelAndView showEditForm(@PathVariable Long id){
       Employee employee = employeeService.findById(id);
        if(employee != null){
            ModelAndView modelAndView = new ModelAndView("/employee/edit");
            modelAndView.addObject("employee",employee);
            return modelAndView;
        }
        else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }


    @PostMapping("/edit-employee")
    public ModelAndView editEmployee(@ModelAttribute ("employee") EmployeeForm employeeform,BindingResult bindingResult){
        // lay ten file
        MultipartFile multipartFile = employeeform.getAvatar();
        String fileName = multipartFile.getOriginalFilename();
        String fileUpload = env.getProperty("file_upload").toString();
        // luu file len server
        try {
            FileCopyUtils.copy(employeeform.getAvatar().getBytes(), new File(fileUpload + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ModelAndView modelAndView = new ModelAndView("/employee/edit");
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errorMessage",bindingResult.getAllErrors());
            return modelAndView;
        }
        Employee employeeObject = new Employee(employeeform.getName(),employeeform.getBirthDate(),employeeform.getAddress(),employeeform.getSalary(), fileName,employeeform.getDepartment());
        employeeObject.setId(employeeform.getId());
        employeeService.save(employeeObject);
        modelAndView.addObject("employee", employeeObject);
        modelAndView.addObject("message","Updated Successfully");
        return modelAndView;
    }

    @GetMapping("/delete-employee/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id){
        Employee employee = employeeService.findById(id);
        if(employee != null){
            ModelAndView modelAndView = new ModelAndView("/employee/delete");
            modelAndView.addObject("employee",employee);
            return modelAndView;
        }
        else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/delete-employee")
    public String deleteEmployee(@ModelAttribute ("employee") Employee employee){
        employeeService.remove(employee.getId());
        return "redirect:employees";
    }

    @GetMapping("/sorting-salaryAsc")
    public ModelAndView sortingSalaryAsc(@PageableDefault(size = 3) Pageable pageable){
        Page<Employee> employees = employeeService.findByOrderBySalaryAsc(pageable);
        ModelAndView modelAndView = new ModelAndView("/employee/list");
        modelAndView.addObject("employees",employees);
        return modelAndView;
    }

    @GetMapping("/sorting-salaryDesc")
    public ModelAndView sortingSalaryDesc(@PageableDefault(size = 3) Pageable pageable){
        Page<Employee> employees = employeeService.findByOrderBySalaryDesc(pageable);
        ModelAndView modelAndView = new ModelAndView("/employee/list");
        modelAndView.addObject("employees",employees);
        return modelAndView;
}
}
