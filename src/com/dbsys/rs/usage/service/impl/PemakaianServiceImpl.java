package com.dbsys.rs.usage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.NumberException;
import com.dbsys.rs.lib.entity.Barang;
import com.dbsys.rs.lib.entity.Pemakaian;
import com.dbsys.rs.lib.entity.PemakaianBhp;
import com.dbsys.rs.lib.entity.PemakaianObat;
import com.dbsys.rs.usage.repository.BarangRepository;
import com.dbsys.rs.usage.repository.PemakaianRepository;
import com.dbsys.rs.usage.service.PemakaianService;

@Service
@Transactional(readOnly = true)
public class PemakaianServiceImpl implements PemakaianService {

	@Autowired
	private PemakaianRepository pemakaianRepository;
	@Autowired
	private BarangRepository barangRepository;
	
	@Override
	@Transactional(readOnly = false)
	public Pemakaian simpan(Pemakaian pemakaian) throws NumberException {
		if (pemakaian.getTanggal() == null)
			pemakaian.setTanggal(DateUtil.getDate());
		pemakaian = pemakaianRepository.save(pemakaian);

		Barang barang = pemakaian.getBarang();
		barang.substract(pemakaian.getJumlah());
		barangRepository.save(barang);
		
		return pemakaian;
	}

	@Override
	public Pemakaian getById(Long id) {
		return pemakaianRepository.findOne(id);
	}

	@Override
	public List<PemakaianBhp> getBhpByPasien(Long id) {
		return pemakaianRepository.findAllPemakaianBhpByPasien(id);
	}

	@Override
	public List<PemakaianObat> getObatByPasien(Long id) {
		return pemakaianRepository.findAllPemakaianObatByPasien(id);
	}

	@Override
	public List<PemakaianObat> getObatByNomorResep(String nomorResep) {
		return pemakaianRepository.findAllPemakaianObatByNomorResep(nomorResep);
	}

}
