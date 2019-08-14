package com.codegym.cms.controller;

import com.codegym.cms.model.Department;
import com.codegym.cms.model.Employee;
import com.codegym.cms.service.DepartmentService;
import com.codegym.cms.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;



@Controller
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/departments")
    public ModelAndView listDepartment(Pageable pageable){
        Page<Department> departments = departmentService.findAll(pageable);
        ModelAndView modelAndView = new ModelAndView("/department/list");
        modelAndView.addObject("departments",departments);
        return modelAndView;
    }

    @GetMapping("/create-department")
    public ModelAndView showCreateForm(){
        ModelAndView modelAndView = new ModelAndView("/department/create");
        modelAndView.addObject("department",new Department());
        return modelAndView;
    }

    @PostMapping("/create-department")
    public ModelAndView createDepartment(@ModelAttribute ("department") Department department){
        departmentService.save(department);
        ModelAndView modelAndView = new ModelAndView("/department/create");
        modelAndView.addObject("department",department);
        modelAndView.addObject("message","Created successfully");
        return modelAndView;
    }

    @GetMapping("/edit-department/{id}")
    public ModelAndView showEditForm(@PathVariable Long id){
        Department department = departmentService.findById(id);
        ModelAndView modelAndView = new ModelAndView("/department/edit");
        modelAndView.addObject("department",department);
        return modelAndView;
    }

    @PostMapping("/edit-department")
    public ModelAndView editDepartment(@ModelAttribute("department") Department department){
        departmentService.save(department);
        ModelAndView modelAndView = new ModelAndView("/department/edit");
        modelAndView.addObject("department",department);
        modelAndView.addObject("message","Edited successfully");
        return modelAndView;
    }

    @GetMapping("/delete-department/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id){
        Department department = departmentService.findById(id);
        if(department != null) {
        ModelAndView modelAndView = new ModelAndView("/department/delete");
        modelAndView.addObject("department",department);
        return modelAndView;
        }
        else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return  modelAndView;
        }
    }

    @PostMapping("/delete-department")
    public String deleteDepartment(@ModelAttribute ("department") Department department){
        employeeService.remove(department.getId());
        return "redirect:departments";
    }

    @GetMapping("/view-department")
    public ModelAndView viewDepartment(@RequestParam("departmentlist") Long id){
        Department department = departmentService.findById(id);
        if(department == null){
            return new ModelAndView("/error.404");
        }
        Iterable<Employee> employees = employeeService.findAllByDepartment(department);
        ModelAndView modelAndView = new ModelAndView("/department/view");
        modelAndView.addObject("department", department);
        modelAndView.addObject("employees", employees);
        return modelAndView;
    }
}
