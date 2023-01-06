package kalo.kaloAdmin_kaloAdmin.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kalo.kaloAdmin_kaloAdmin.domain.Role;
import kalo.kaloAdmin_kaloAdmin.domain.dto.LoginDto.LoginInfoDto;

@Service
public class JwtService {
    private String secretKey = "56g4hftfghtrd46fhgd4tr56hgbdrov34y0f4d5t6fgdrt546";

    public String createJwt(Long id, Role role, String username) {
        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("typ", "JWT");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("username", username);
        map.put("role", role);

        Date expireTime  = new Date();
        expireTime.setTime(expireTime.getTime() + 1000 * 60 * 1);

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
            .setHeader(headerMap)
            .setClaims(map)
            .setExpiration(expireTime)
            .signWith(signatureAlgorithm, signingKey);

        return builder.compact();
    }

    public boolean checkJwt() throws Exception {
        String jwt = getJwt();
        try {
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .parseClaimsJws(jwt).getBody();

            System.out.println("토큰 정상");
            System.out.println("expireTime :" + claims.getExpiration());
            System.out.println("name :" + claims.get("name"));
            System.out.println("Id :" + claims.get("id"));
            return true;
        } catch (ExpiredJwtException exception) {
            System.out.println("토큰 만료");
            return false;
        } catch (JwtException exception) {
            System.out.println("토큰 변조");
            return false;
        }
    }

    public LoginInfoDto getInfo() {
        String accessToken = getJwt();
        if(accessToken == null || accessToken.length() == 0){
            throw new NullPointerException();
        }

        Jws<Claims> claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new NullPointerException();
        }

        Long id = claims.getBody().get("id",Long.class);
        Role role = claims.getBody().get("role",Role.class);
        String username = claims.getBody().get("username",String.class);

        return new LoginInfoDto(id, role, username);
    }

    public String getJwt(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("ACCESS-TOKEN");
    }
}
