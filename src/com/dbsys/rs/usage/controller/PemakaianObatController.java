package com.dbsys.rs.usage.controller;

import java.util.List;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbsys.rs.lib.ApplicationException;
import com.dbsys.rs.lib.EntityRestMessage;
import com.dbsys.rs.lib.ListEntityRestMessage;
import com.dbsys.rs.lib.entity.PemakaianObat;
import com.dbsys.rs.usage.service.PemakaianService;

@Controller
@RequestMapping("/obat")
public class PemakaianObatController {

	@Autowired
	private PemakaianService pemakaianService;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public EntityRestMessage<PemakaianObat> simpan(@RequestBody PemakaianObat pemakaian) throws ApplicationException, PersistenceException {
		pemakaian = (PemakaianObat)pemakaianService.simpan(pemakaian);
		return EntityRestMessage.createPemakaianObat(pemakaian);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseBody
	public EntityRestMessage<PemakaianObat> getById(@PathVariable Long id) throws ApplicationException, PersistenceException {
		PemakaianObat pemakaian = (PemakaianObat)pemakaianService.getById(id);
		return EntityRestMessage.createPemakaianObat(pemakaian);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/pasien/{id}")
	@ResponseBody
	public ListEntityRestMessage<PemakaianObat> getByPasien(@PathVariable Long id) throws ApplicationException, PersistenceException {
		List<PemakaianObat> list = pemakaianService.getObatByPasien(id);
		return ListEntityRestMessage.createListPemakaianObat(list);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/nomor/{nomor}")
	@ResponseBody
	public ListEntityRestMessage<PemakaianObat> getByNomorResep(@PathVariable String nomor) throws ApplicationException, PersistenceException {
		List<PemakaianObat> list = pemakaianService.getObatByNomorResep(nomor);
		return ListEntityRestMessage.createListPemakaianObat(list);
	}
}
