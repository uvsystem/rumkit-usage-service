package com.dbsys.rs.usage.test.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.dbsys.rs.lib.DateUtil;
import com.dbsys.rs.lib.NumberException;
import com.dbsys.rs.lib.Tanggungan;
import com.dbsys.rs.lib.entity.BahanHabisPakai;
import com.dbsys.rs.lib.entity.Barang;
import com.dbsys.rs.lib.entity.Pasien;
import com.dbsys.rs.lib.entity.Pemakaian;
import com.dbsys.rs.lib.entity.PemakaianBhp;
import com.dbsys.rs.lib.entity.Penduduk;
import com.dbsys.rs.lib.entity.Pasien.StatusPasien;
import com.dbsys.rs.lib.entity.Pasien.Type;
import com.dbsys.rs.lib.entity.Penduduk.Kelamin;
import com.dbsys.rs.usage.repository.PemakaianRepository;
import com.dbsys.rs.usage.service.PemakaianService;
import com.dbsys.rs.usage.test.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
@TransactionConfiguration (defaultRollback = true)
public class PemakaianBhpControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;
	private long count;

	@Autowired
	private PemakaianService pemakaianService;
	@Autowired
	private PemakaianRepository pemakaianRepository;

	private Pemakaian pemakaian;
	
	@Before
	public void setup() throws NumberException {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		
		count = pemakaianRepository.count();
		
		Barang barang = new BahanHabisPakai();
		barang.setHarga(20000l);
		barang.setJumlah(20l);
		barang.setKode("BHPxxx");
		barang.setNama("Bahan Habis Pakai");
		barang.setSatuan("Satuan");
		barang.setTanggungan(Tanggungan.BPJS);
		
		Penduduk penduduk = new Penduduk();
		penduduk.setAgama("Kristen");
		penduduk.setDarah("O");
		penduduk.setKelamin(Kelamin.PRIA);
		penduduk.setNama("Penduduk xxx");
		penduduk.setNik("Nik xxx");
		penduduk.setTanggalLahir(DateUtil.getDate());
		penduduk.setTelepon("Telepon");
		penduduk.generateKode();

		Pasien pasien = new Pasien();
		pasien.setPenduduk(penduduk);
		pasien.setTanggungan(Tanggungan.BPJS);
		pasien.setStatus(StatusPasien.OPEN);
		pasien.setTipe(Type.RAWAT_JALAN);
		pasien.setTanggalMasuk(DateUtil.getDate());
		pasien.generateKode();

		pemakaian = new PemakaianBhp();
		pemakaian.setBarang(barang);
		pemakaian.setPasien(pasien);
		pemakaian.setBiayaTambahan(10000L);
		pemakaian.setJumlah(2);
		pemakaian.setKeterangan("Biaya Administrasi");
		pemakaian.setTanggal(DateUtil.getDate());
		pemakaian = pemakaianService.simpan(pemakaian);

		assertEquals(count + 1, pemakaianRepository.count());
		assertEquals(new Long(50000), pemakaian.getTagihan());
	}
	
	@Test
	public void testSimpan() throws Exception {
		this.mockMvc.perform(
				post("/bhp")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"bahanHabisPakai\": {"
						+ "\"harga\": \"20000\","
						+ "\"jumlah\": \"100\","
						+ "\"kode\": \"BHPxxxx\","
						+ "\"nama\":\"BHP xxxx\","
						+ "\"satuan\":\"satuan\","
						+ "\"tanggungan\":\"UMUM\""
						+ "},"
						+ "\"pasien\": {"
						+ "\"penduduk\": {"
						+ "\"agama\": \"Kristen\","
						+ "\"darah\": \"O\","
						+ "\"kelamin\": \"PRIA\","
						+ "\"nama\":\"Penduduk xxxx\","
						+ "\"nik\":\"nik xxxx\","
						+ "\"tanggalLahir\":\"1991-12-05\","
						+ "\"telepon\":\"telepon 2\","
						+ "\"kode\": \"KODE\""
						+ "},"
						+ "\"tanggungan\": \"BPJS\","
						+ "\"status\": \"OPEN\","
						+ "\"tipe\": \"RAWAT_JALAN\","
						+ "\"tanggalMasuk\": \"2015-10-1\","
						+ "\"kode\": \"KODE\""
						+ "},"
						+ "\"biayaTambahan\":\"10000\","
						+ "\"jumlah\":\"2\","
						+ "\"keterangan\":\"Keterangan\","
						+ "\"tanggal\":\"2015-10-14\""
						+ "}")
			)
			.andExpect(jsonPath("$.tipe").value("ENTITY"))
			.andExpect(jsonPath("$.model.tagihan").value(50000))
			.andExpect(jsonPath("$.message").value("Berhasil"));

		assertEquals(count + 2, pemakaianRepository.count());
	}

	@Test
	public void testGetById() throws Exception {
		this.mockMvc.perform(
				get(String.format("/bhp/%d", pemakaian.getId()))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath("$.tipe").value("ENTITY"))
			.andExpect(jsonPath("$.message").value("Berhasil"));
	}

	@Test
	public void testGetByPasien() throws Exception {
		this.mockMvc.perform(
				get(String.format("/bhp/pasien/%d", pemakaian.getPasien().getId()))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath("$.tipe").value("LIST"))
			.andExpect(jsonPath("$.message").value("Berhasil"));
	}
}
