package in.adarshit.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.adarshit.entity.User;
import in.adarshit.repo.UserRepository;
import in.adarshit.service.JwtService;

@RestController
public class UserRestController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PasswordEncoder pwdEncoder;

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private JwtService jwtService;

	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome to Ashok IT";
	}

	@PostMapping("/login")
	public ResponseEntity<String> loginCheck(@RequestBody User customer) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(customer.getEmail(),
				customer.getPwd());
		try {
			Authentication authenticate = authManager.authenticate(token);
			if (authenticate.isAuthenticated()) {
				String jwtToken = jwtService.generateToken(customer.getEmail());
				return new ResponseEntity<String>(jwtToken, HttpStatus.OK);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return new ResponseEntity<String>("Invalid Credentials", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/register")
	public ResponseEntity<String> saveCustomer(@RequestBody User user) {
		String encodedPwd = pwdEncoder.encode(user.getPwd());
		user.setPwd(encodedPwd);
		userRepo.save(user);
		return new ResponseEntity<String>("Customer Registered", HttpStatus.OK);
	}

}
