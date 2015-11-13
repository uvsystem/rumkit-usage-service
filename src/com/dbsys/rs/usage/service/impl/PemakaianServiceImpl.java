package com.dbsys.rs.usage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.NumberException;
import com.dbsys.rs.lib.entity.Barang;
import com.dbsys.rs.lib.entity.Pemakaian;
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
		Barang barang = pemakaian.getBarang();
		barang.substract(pemakaian.getJumlah());
		barangRepository.save(barang);

		if (pemakaian.getTanggal() == null)
			pemakaian.setTanggal(DateUtil.getDate());
		return pemakaianRepository.save(pemakaian);
	}

	@Override
	public Pemakaian getById(Long id) {
		return pemakaianRepository.findOne(id);
	}

	@Override
	public List<Pemakaian> getByPasien(Long id) {
		return pemakaianRepository.findByPasien_Id(id);
	}

	@Override
	public List<Pemakaian> getByNomorResep(String nomorResep) {
		return pemakaianRepository.findByNomorResep(nomorResep);
	}
}
