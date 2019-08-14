package com.codegym.cms.service.impl;

import com.codegym.cms.model.Department;
import com.codegym.cms.model.Employee;
import com.codegym.cms.repository.DepartmentRepository;
import com.codegym.cms.repository.EmployeeRepository;
import com.codegym.cms.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Page<Department> findAll(Pageable pageable) {
        return departmentRepository.findAll(pageable);
    }

    @Override
    public Department findById(Long id)
    {

        return departmentRepository.findOne(id);
    }

    @Override
    public void save(Department department) {
        departmentRepository.save(department);
    }

    @Override
    public void remove(Long id) {
       Department department = departmentRepository.findOne(id);
        Iterable<Employee> employeeList = employeeRepository.findAllByDepartment(department);
        for (Employee employee:employeeList){
            employeeRepository.delete(employee.getId());
        }
        departmentRepository.delete(id);
    }
}
