package com.codegym.cms.service.impl;

import com.codegym.cms.model.Department;
import com.codegym.cms.model.Employee;
import com.codegym.cms.repository.DepartmentRepository;
import com.codegym.cms.repository.EmployeeRepository;
import com.codegym.cms.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Page<Employee> findAll(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    public Employee findById(Long id) {return employeeRepository.findOne(id);
    }

    @Override
    public void save(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public void remove(Long id) {
        Department department = departmentRepository.findOne(id);
        Iterable<Employee> employeeList = employeeRepository.findAllByDepartment(department);
        for (Employee employee:employeeList){
            employeeRepository.delete(employee);
        }
        departmentRepository.delete(id);
    }

    @Override
    public Iterable<Employee> findAllByDepartment(Department department) {
        return employeeRepository.findAllByDepartment(department);
    }

    @Override
    public Page<Employee> findByOrderBySalaryAsc(Pageable pageable) {
        return employeeRepository.findByOrderBySalaryAsc(pageable);
    }

    @Override
    public Page<Employee> findByOrderBySalaryDesc(Pageable pageable) {
        return employeeRepository.findByOrderBySalaryDesc(pageable);
    }
}
