package com.example.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.repositories.ClassRepository;
import com.example.demo.models.Class;

@Service
public class ClassService {
	private ClassRepository classRepository;
	public ClassService(ClassRepository classrepo) {
		this.classRepository = classrepo;
	}
	
	public List<Class>AllClasses(){
		return this.classRepository.findAll();
	}
	public Class getSingleClass(Long id) {
		return this.classRepository.findById(id).orElse(null);
	}
	public Class CreateClass(Class newClass) {
		return this.classRepository.save(newClass);
	}
	public void deleteClass(Long id) {
		classRepository.deleteById(id);
	}
	public Class findClassName(String name) {
		return this.classRepository.findByClassName(name);
	}
	
}
