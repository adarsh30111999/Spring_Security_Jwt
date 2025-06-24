package in.adarshit.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import in.adarshit.service.JwtService;
import in.adarshit.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AppFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		// take request header
		// checl token is present or not
		// verify token is valid or not
		String token = null;
		String username = null;
		// Authorization=Bearer token
		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer ")) {
			token = header.substring(7);
			username = jwtService.extractUsername(token);

		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// You can create your own custom UserDetails or skip it
			UserDetails userByUsername = userService.loadUserByUsername(username);
			boolean isValid = jwtService.validateToken(token, username);
			if (isValid) {
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userByUsername, null,
						userByUsername.getAuthorities());
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(auth);
			}

		}
		filterChain.doFilter(request, response);
	}
}
