package com.example.demo.controller;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserRepository userRepository;

	@PostMapping("/register")
	public ResponseEntity<String> createUser(@RequestBody User userData) {
		//if email is registered to an account,send error message
		if (userRepository.existsUserByEmail(userData.getEmail())) {
			return new ResponseEntity<>("Email already used",HttpStatus.INTERNAL_SERVER_ERROR);
		//if email is not yet used, create new user, and send success message
		}else {
			User newUser = new User(userData.getImg(),userData.getName(),userData.getPhone(),userData.getEmail(),userData.getPassword());
			userRepository.save(newUser);
			return new ResponseEntity<>("Added successfully",HttpStatus.OK);
		}
	}
	
	@GetMapping("/login/{email}/{password}")
	public ResponseEntity<?> login(@PathVariable String email, @PathVariable String password){
		//if there doesnt exist a user with the email, send an error message
		if (!userRepository.existsUserByEmail(email)) {
			return new ResponseEntity<>("No user corresponding to email adress",HttpStatus.INTERNAL_SERVER_ERROR);
		//if there is a user with the email, verify the password, and send appropriate message
		}else{
			User a = userRepository.findByEmail(email);
			
			if (!(a.getPassword().equals(password))) {
				return new ResponseEntity<>("Wrong password",HttpStatus.INTERNAL_SERVER_ERROR);
			}else {
				//return id of user
				return new ResponseEntity<>(a.getId(),HttpStatus.OK);
			}
		}
	}
	
	@GetMapping("/myinfo/{id}")
	public ResponseEntity<User> getCurrentUserInfo(@PathVariable int id){
		//needs jwt verification
		return new ResponseEntity<>(userRepository.findById(id).get(),HttpStatus.OK);
	}
	
	@GetMapping("/otheruserinfo/{id}")
	public ResponseEntity<User> getOtherUserInfo(@PathVariable int id){
		//needs jwt verification
		//find user, overwrite sensitive data then return user
		User user = userRepository.findById(id).get();		
		user.setPassword(null);
		return new ResponseEntity<>(user,HttpStatus.OK);
	}
	
	@PutMapping("/updatemyinfo")
	public ResponseEntity<String> updateUser(@RequestBody User newUser){
		//needs jwt verification (verify jwt id = newUser.id)
		userRepository.save(newUser);
		return new ResponseEntity<>("Updated user successfully",HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteme/{id}")
	public ResponseEntity<String> deleteCurrUser(@PathVariable int id){
		//needs jwt verification
		userRepository.deleteById(id);
		return new ResponseEntity<>("User deleted successfully",HttpStatus.OK);
	}
	
	//for testing purposes
//	@GetMapping("/getall")
//	public ResponseEntity<List<User>> getAllUsers() {
//		try {
//			
//			return new ResponseEntity<>(userRepository.findAll(),HttpStatus.OK);
//		}catch(Exception e) {
//			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
}



























































