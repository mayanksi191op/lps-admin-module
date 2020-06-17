package com.tyss.cg.springbootdatajpa.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tyss.cg.springbootdatajpa.entity.Applyloan;
import com.tyss.cg.springbootdatajpa.entity.LoanPrograms;
import com.tyss.cg.springbootdatajpa.entity.User;
import com.tyss.cg.springbootdatajpa.exception.EntryAlreadyExistsException;
import com.tyss.cg.springbootdatajpa.exception.InvalidDataEnteredException;
import com.tyss.cg.springbootdatajpa.exception.LoanNotFoundException;
import com.tyss.cg.springbootdatajpa.exception.UserNotFoundException;
import com.tyss.cg.springbootdatajpa.response.Response;
import com.tyss.cg.springbootdatajpa.services.ApplyLoanServices;
import com.tyss.cg.springbootdatajpa.services.LoanProgramsServices;
import com.tyss.cg.springbootdatajpa.services.UserServices;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AdminController {
	
	@Autowired
	private ApplyLoanServices service;
	
	@Autowired
	private LoanProgramsServices loanProgramsServices;
	
	@Autowired
	private UserServices userServices;
	
	// VIEW APPLICATIONS
	@GetMapping("/application/requested/")
	public Response<List<Applyloan>> requestedApplications(){
		List<Applyloan> applyloans = service.requestedApplications();
		if (applyloans != null) {
			return new Response<>(false, "list found", applyloans);
		} else {
			return new Response<>(true, "no status with this status", null);
		}
	}
	
	@GetMapping("/application/requested/{pageNo}/{itemsPerPage}")
	public Page<Applyloan> requestedApplication(@PathVariable int pageNo, @PathVariable int itemsPerPage){
		return service.requestedApplication(pageNo, itemsPerPage);
	}

	@GetMapping("/application/requested/{pageNo}/{itemsPerPage}/{fieldname}")
	public Page<Applyloan> sortRequestedApplication(@PathVariable int pageNo, @PathVariable int itemsPerPage, @PathVariable String fieldname){
		return service.sortRequestedApplication(pageNo, itemsPerPage, fieldname);
	}
	
	

	@GetMapping("/application/rejected/")
	public Response<List<Applyloan>> rejectedApplications(){
		List<Applyloan> applyloans = service.rejectedApplications();
		if (applyloans != null) {
			return new Response<>(false, "list found", applyloans);
		} else {
			return new Response<>(true, "no status with this status", null);
		}
	}
	
	@GetMapping("/application/rejected/{pageNo}/{itemsPerPage}")
	public Page<Applyloan> rejectedApplications(@PathVariable int pageNo, @PathVariable int itemsPerPage){
		return service.rejectedApplications(pageNo, itemsPerPage);
	}

	@GetMapping("/application/rejected/{pageNo}/{itemsPerPage}/{fieldname}")
	public Page<Applyloan> sortRejectedApplications(@PathVariable int pageNo, @PathVariable int itemsPerPage, @PathVariable String fieldname){
		return service.sortRejectedApplications(pageNo, itemsPerPage, fieldname);
	}

	@GetMapping("/application/approved/")
	public Response<List<Applyloan>> approvedApplications(){
		List<Applyloan> applyloans = service.approvedApplications();
		if (applyloans != null) {
			return new Response<>(false, "list found", applyloans);
		} else {
			return new Response<>(true, "no status with this status", null);
		}
	}
	
	@GetMapping("/application/approved/{pageNo}/{itemsPerPage}")
	public Page<Applyloan> approvedApplication(@PathVariable int pageNo, @PathVariable int itemsPerPage){
		return service.approvedApplications(pageNo, itemsPerPage);
	}

	@GetMapping("/application/approved/{pageNo}/{itemsPerPage}/{fieldname}")
	public Page<Applyloan> sortApprovedApplication(@PathVariable int pageNo, @PathVariable int itemsPerPage, @PathVariable String fieldname){
		return service.sortApprovedApplications(pageNo, itemsPerPage, fieldname);
	}
	
	
	//VIEW LOAN PROGRAMS
	@GetMapping("/loanprograms")
	public Response<List<LoanPrograms>> findAll() {
		List<LoanPrograms> lists = loanProgramsServices.findAll();
		return new Response<>(false, "list retrieved", lists);
	}
	
	@GetMapping("/loanprograms/{pageNo}/{itemsPerPage}")
	public Page<LoanPrograms> getLoans(@PathVariable int pageNo, @PathVariable int itemsPerPage){
		return loanProgramsServices.getLoans(pageNo, itemsPerPage);
	}
	
	@GetMapping("/loanprograms/{pageNo}/{itemsPerPage}/{fieldname}")
	public Page<LoanPrograms> getLoans(@PathVariable int pageNo, @PathVariable int itemsPerPage, @PathVariable String fieldname){
		return loanProgramsServices.getSortLoans(pageNo, itemsPerPage, fieldname);
	}
	
	@GetMapping("/loanprograms/{loan_no}")
	public Response<LoanPrograms> getById(@PathVariable int lona_no) {
		LoanPrograms loanPrograms = loanProgramsServices.getById(lona_no);
		
		if (loanPrograms == null) {
			throw new LoanNotFoundException("Loan not found!!!");
		}else {
			return new Response<LoanPrograms>(false, "loan found", loanPrograms);
		}
	}
	
	@DeleteMapping("/loanprograms/delete/{lona_no}")
	public Response<LoanPrograms> delete(@PathVariable int lona_no) {
		LoanPrograms loanPrograms = loanProgramsServices.getById(lona_no);
		
		if (loanPrograms == null) {
			throw new LoanNotFoundException("loan not found!!!");
		} {
			loanProgramsServices.deleteLoan(lona_no);
			return new Response<LoanPrograms>(false, "Loan has been deleted!!!", loanPrograms);
		}
	}
	
	@PutMapping("/loanprograms/update")
	public Response<LoanPrograms> update(@Valid @RequestBody LoanPrograms loanPrograms) {
		LoanPrograms loanPrograms2 = loanProgramsServices.getById(loanPrograms.getLoan_no());
		if (loanPrograms2 == null) {
			throw new LoanNotFoundException("Loan not found!!!");
		}
		if (loanProgramsServices.updateLoan(loanPrograms) == false) {
			throw new InvalidDataEnteredException("please enter the data correctly");
		}else {
			return new Response<LoanPrograms>(false, "Loan has been updated!!!", loanPrograms);
		}
	}
	
	@PostMapping("/loanprograms/add")
	public Response<LoanPrograms> save(@Valid @RequestBody LoanPrograms loanPrograms) {
		
		if (loanProgramsServices.saveLoan(loanPrograms) == false) {
			//throw new RuntimeException("please enter correctly");
			throw new EntryAlreadyExistsException("Loan already exists!!!");
		}else {
			return new Response<LoanPrograms>(false, "Loan added successfuly!!!", loanPrograms);
		}
	}
	
	
	//users
	@GetMapping("/clients")
	public Response<List<User>> viewClients() {
		List<User> lists = userServices.viewClients();
		return new Response<>(false, "list retrieved", lists);
	}

	@GetMapping("/clients/{pageNo}/{itemsPerPage}")
	public Page<User> getClients(@PathVariable int pageNo, @PathVariable int itemsPerPage){
		return userServices.getClients(pageNo, itemsPerPage);
	}

	@GetMapping("/clients/{pageNo}/{itemsPerPage}/{fieldname}")
	public Page<User> getClients(@PathVariable int pageNo, @PathVariable int itemsPerPage, @PathVariable String fieldname){
		return userServices.getSortClients(pageNo, itemsPerPage, fieldname);
	}

	@GetMapping("/user/{email}")
	public Response<User> getById(@PathVariable String email) {
		User user = userServices.getByEmail(email);

		if (user == null) {
			throw new UserNotFoundException("User not found!!!");
		}else {
			return new Response<User>(false, "User found!!!", user);
		}
	}

	@PostMapping("/clients")
	public Response<User> save(@Valid @RequestBody User user) {
		user.setRole("ROLE_LAD");
		user.setPassword("Qwerty@123");
		try {
			userServices.saveUser(user);
			return new Response<User>(false, "Client added successfuly.", user);
		} catch (Exception e) {
			throw new EntryAlreadyExistsException("Client already exist!!!");
		}
	}

	@DeleteMapping("/clients/{userid}")
	public Response<User> deleteClient(@PathVariable int userid) {
		System.out.println(userid);
		User user = userServices.getById(userid);
		if (user == null) {
			throw new UserNotFoundException("Client not found!!!");
		} else {
			userServices.deleteUser(userid);
			return new Response<User>(false, "Client deleted", user);
		}
	}

}
