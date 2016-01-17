package com.dbsys.rs.usage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbsys.rs.lib.entity.Pemakaian;

public interface PemakaianRepository extends JpaRepository<Pemakaian, Long> {

	List<Pemakaian> findByPasien_Id(Long id);

	List<Pemakaian> findByNomorResep(String nomorResep);

}
