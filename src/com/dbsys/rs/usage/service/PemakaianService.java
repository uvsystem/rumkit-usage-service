package com.dbsys.rs.usage.service;

import java.util.List;

import com.dbsys.rs.lib.NumberException;
import com.dbsys.rs.lib.entity.Pemakaian;
import com.dbsys.rs.lib.entity.PemakaianBhp;
import com.dbsys.rs.lib.entity.PemakaianObat;

/**
 * Interface untuk mengelola data pemakaian.
 * 
 * @author Deddy Christoper Kakunsi
 *
 */
public interface PemakaianService {

	/**
	 * Simpan pemakaian barang.
	 * 
	 * @param pemakaian
	 * 
	 * @return pemakaian yang berhasil disimpan
	 * @throws NumberException jumlah barang tidak mencukupi untuk dikurangi 
	 */
	Pemakaian simpan(Pemakaian pemakaian) throws NumberException;

	/**
	 * Mengambil pemakaian barang berdasarkan id.
	 * 
	 * @param id
	 * 
	 * @return pemakaian barang
	 */
	Pemakaian getById(Long id);

	/**
	 * Mengambil daftar pemakaian bhp berdasarkan pasien.
	 * 
	 * @param id
	 * 
	 * @return daftar pemakaian barang
	 */
	List<PemakaianBhp> getBhpByPasien(Long id);

	/**
	 * Mengambil daftar pemakaian obat berdasarkan pasien.
	 * 
	 * @param id
	 * 
	 * @return daftar pemakaian barang
	 */
	List<PemakaianObat> getObatByPasien(Long id);

	/**
	 * Mengambil daftar pemakaian obat berdasarkan nomor resep.
	 * 
	 * @param nomor
	 * 
	 * @return daftar pemakaian obat
	 */
	List<PemakaianObat> getObatByNomorResep(String nomorResep);

}
