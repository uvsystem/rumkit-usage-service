package com.dbsys.rs.usage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dbsys.rs.lib.entity.Pemakaian;
import com.dbsys.rs.lib.entity.PemakaianBhp;
import com.dbsys.rs.lib.entity.PemakaianObat;

public interface PemakaianRepository extends JpaRepository<Pemakaian, Long> {

	@Query("FROM PemakaianBhp p WHERE p.pasien.id = :id")
	List<PemakaianBhp> findAllPemakaianBhpByPasien(@Param("id") Long id);

	@Query("FROM PemakaianObat p WHERE p.pasien.id = :id")
	List<PemakaianObat> findAllPemakaianObatByPasien(@Param("id") Long id);

	@Query("FROM PemakaianObat p WHERE p.nomorResep = :nomor")
	List<PemakaianObat> findAllPemakaianObatByNomorResep(@Param("nomor") String nomorResep);

}
