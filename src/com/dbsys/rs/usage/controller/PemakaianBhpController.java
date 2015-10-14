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
import com.dbsys.rs.lib.entity.PemakaianBhp;
import com.dbsys.rs.usage.service.PemakaianService;

@Controller
@RequestMapping("/bhp")
public class PemakaianBhpController {

	@Autowired
	private PemakaianService pemakaianService;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public EntityRestMessage<PemakaianBhp> simpan(@RequestBody PemakaianBhp pemakaian) throws ApplicationException, PersistenceException {
		pemakaian = (PemakaianBhp)pemakaianService.simpan(pemakaian);
		return EntityRestMessage.createPemakaianBhp(pemakaian);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseBody
	public EntityRestMessage<PemakaianBhp> getById(@PathVariable Long id) throws ApplicationException, PersistenceException {
		PemakaianBhp pemakaian = (PemakaianBhp)pemakaianService.getById(id);
		return EntityRestMessage.createPemakaianBhp(pemakaian);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/pasien/{id}")
	@ResponseBody
	public ListEntityRestMessage<PemakaianBhp> getByPasien(@PathVariable Long id) throws ApplicationException, PersistenceException {
		List<PemakaianBhp> list = pemakaianService.getBhpByPasien(id);
		return ListEntityRestMessage.createListPemakaianBhp(list);
	}
}
