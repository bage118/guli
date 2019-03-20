package com.guli.ucenter.util;

import com.guli.ucenter.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Date;

// jwt工具类
@Data
public class JwtUtils {

    public static final String SUBJECT = "guli";

    //秘钥
    public static final String APPSECRET = "guli";

    public static final long EXPIRE = 1000 * 60 * 30;  //过期时间，毫秒，30分钟

    //生成jwt token
    public static String geneJsonWebToken(Member member){

        if (member == null || StringUtils.isEmpty(member.getId())
                || StringUtils.isEmpty(member.getNickname())
                || StringUtils.isEmpty(member.getAvatar())){
            return null;
        }

        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("id",member.getId())
                .claim("nickname",member.getNickname())
                .claim("avatar",member.getAvatar())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))//过期时间
                .signWith(SignatureAlgorithm.HS256,APPSECRET).compact();//加密方式

        return token;
    }

    //校验jwt token
    public static Claims checkJWT(String token){
        Claims claims = Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(token).getBody();
        return claims;
    }

    //测试生成jwt token
    private static String testGeneJwt(){
        Member member = new Member();
        member.setId("666");
        member.setAvatar("http://haohaiyo.jpg");
        member.setNickname("Wanba");

        String token = JwtUtils.geneJsonWebToken(member);
        System.out.println(token);
        return token;
    }

    //测试校验jwt token
    private static void testCheckJwt(String token){

        Claims claims = JwtUtils.checkJWT(token);
        String nickname = (String) claims.get("nickname");
        String avatar = (String) claims.get("avatar");
        String id = (String) claims.get("id");
        System.out.println(nickname);
        System.out.println(id);
        System.out.println(avatar);

    }


    public static void main(String[] args) {
        String token = testGeneJwt();
        testCheckJwt(token);
    }
}
