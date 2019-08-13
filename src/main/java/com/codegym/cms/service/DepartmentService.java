package com.codegym.cms.service;

import com.codegym.cms.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {
    Page<Department> findAll(Pageable pageable);

    Department findById(Long id);

    void save(Department department);

    void remove(Long id);
}
