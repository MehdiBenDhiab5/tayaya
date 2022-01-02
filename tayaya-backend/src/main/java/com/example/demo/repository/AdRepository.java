package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Ad;

public interface AdRepository  extends JpaRepository<Ad, Integer>{

}
