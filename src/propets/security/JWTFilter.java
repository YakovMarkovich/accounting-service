package propets.security;

import java.io.IOException;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerMapping;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Component
@Order(20)
public class JWTFilter extends OncePerRequestFilter {

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	CustomUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		System.out.println("Im in jwt filter");
		if (checkEndpoint(request.getServletPath(), request.getMethod())) {
			String authorizationHeader = request.getHeader("Authorization");

			if (authorizationHeader == null) {
				authorizationHeader = request.getHeader("X-Token");
			}

			System.out.println(authorizationHeader);

			if (authorizationHeader == null) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token not sended");
				return;
			}

			String username = null;
			String jwt = null;

			try {

				if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
					jwt = authorizationHeader.substring(7);
					System.out.println("jwt " + jwt);
					username = jwtUtil.extractUsername(jwt);
				}

				else {
					jwt = authorizationHeader;
					System.out.println("jwt in else" + jwt);
					username = jwtUtil.extractUsername(jwt);
					System.out.println("username " + username);
				}

				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

					UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
					// if signature not correct SignatureException will be thrown
					if (this.jwtUtil.validateToken(jwt, userDetails)) {

						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());

						usernamePasswordAuthenticationToken
								.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					}
					if(checkForAdminActions(request.getServletPath(), request.getMethod())){ 
						String[] paths = request.getServletPath().split("/");
						System.out.println(paths[3]);
						System.out.println(userDetails.getUsername());
						System.out.println(userDetails.getAuthorities().parallelStream().anyMatch(e->e.getAuthority().equalsIgnoreCase("admin")));
						if(!userDetails.getAuthorities().parallelStream().anyMatch(e->e.getAuthority().equalsIgnoreCase("admin")) ) {
							response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access only for admin");
						}
					}
					//chain.doFilter(request, response);
				}
			} catch (Exception e) {
				System.out.println("I'm in catch jwtfilter");
				response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
				return;
			}
		}
		System.out.println(response + " responce");
		chain.doFilter(request, response);
	}

	private boolean checkForAdminActions(String path, String method) {
		System.out.println(path + " path");
		//boolean res = path.matches("/propets-app.herokuapp.com/account/.*/role/.*")
		//		|| 
		//		(path.matches("/propets-app.herokuapp.com/account/.*/block/.*") && method.equalsIgnoreCase("PUT"));
		boolean res = path.matches("/account/en/v1/.*/role/.*")
						|| 
						(path.matches("/account/en/v1/.*/block/.*") && method.equalsIgnoreCase("PUT"));
		
		System.out.println(res + " res");
		
		return res;
	}

	private boolean checkEndpoint(String path, String method) {
		//boolean res = !path.matches("/propets-app.herokuapp.com/account/authenticate")
		//		&& !path.matches("/propets-app.herokuapp.com/account/register");
		//return res;
		boolean res = !path.matches("/account/en/v1/authenticate")
						&& !path.matches("/account/en/v1/register");
				return res;
	}
}