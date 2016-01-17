package com.dbsys.rs.usage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.NumberException;
import com.dbsys.rs.lib.PasienOutException;
import com.dbsys.rs.lib.entity.Barang;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pemakaian;
import com.dbsys.rs.usage.repository.BarangRepository;
import com.dbsys.rs.usage.repository.PasienRepository;
import com.dbsys.rs.usage.repository.PemakaianRepository;
import com.dbsys.rs.usage.service.PemakaianService;

@Service
@Transactional(readOnly = true)
public class PemakaianServiceImpl implements PemakaianService {

	@Autowired
	private PemakaianRepository pemakaianRepository;
	@Autowired
	private BarangRepository barangRepository;
	@Autowired
	private PasienRepository pasienRepository;
	
	@Override
	@Transactional(readOnly = false)
	public Pemakaian simpan(Pemakaian pemakaian) throws PasienOutException, NumberException {
		/*
		 * Jumlah pemakaian harus lebih dari 0.
		 */
		if (pemakaian.getJumlah() <= 0)
			throw new NumberException("Jumlah pemakaian harus lebih dari 0");
		
		Pasien pasien = pasienRepository.findOne(pemakaian.getPasien().getId());
		/*
		 * Jika pasien sudah keluar, tidak bisa menambah pemakaian.
		 */
		if (Pasien.StatusPasien.KELUAR.equals(pasien.getStatus()))
			throw new PasienOutException("Tidak bisa menambah tagihan untuk pasien yang sudah keluar");

		Barang barang = barangRepository.findOne(pemakaian.getBarang().getId());
		barang.substract(pemakaian.getJumlah());
		pemakaian.setBarang(barang);

		/*
		 * Jika pemakaian PERSISTED (merupakan fungsi update),
		 * kurangi total tagihan pasien, sesuai tagihan pemakaian yang lama.
		 */
		if (pemakaian.isPersisted()) {
			Pemakaian pemakaianOld = pemakaianRepository.findOne(pemakaian.getId());
			
			pasien.substractTotalTagihan(pemakaianOld.getTagihan());
		}
		
		/*
		 * Tambahkan tagihan pemakaian yang baru ke total tagihan pasien.
		 */
		pasien.addTotalTagihan(pemakaian.getTagihan());
		pemakaian.setPasien(pasien);

		if (pemakaian.getTanggal() == null)
			pemakaian.setTanggal(DateUtil.getDate());
		pemakaianRepository.save(pemakaian);

		return pemakaian;
	}
	
	@Override
	@Transactional(readOnly = false)
	public void hapus(Long id) {
		Pemakaian pemakaian = pemakaianRepository.findOne(id);

		Barang barang = barangRepository.findOne(pemakaian.getBarang().getId());
		long jumlahBarang = barang.getJumlah() + pemakaian.getJumlah();
		barangRepository.updateJumlah(barang.getId(), jumlahBarang);

		Pasien pasien = pasienRepository.findOne(pemakaian.getPasien().getId());
		long tagihanPasien = pasien.getTotalTagihan() == null ? 0 : pasien.getTotalTagihan();
		long totalTagihan = tagihanPasien - pemakaian.getTagihan();
		pasienRepository.updateTagihan(pasien.getId(), totalTagihan);

		pemakaianRepository.delete(id);
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
