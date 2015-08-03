package ksbysample.webapp.lending.web;

import ksbysample.webapp.lending.config.WebSecurityConfig;
import ksbysample.webapp.lending.dao.UserInfoDao;
import ksbysample.webapp.lending.dao.UserRoleDao;
import ksbysample.webapp.lending.entity.UserInfo;
import ksbysample.webapp.lending.entity.UserRole;
import ksbysample.webapp.lending.security.LendingUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @RequestMapping
    public String index(HttpServletRequest request, HttpServletResponse response) {
        // 有効な remember-me Cookie が存在する場合にはログイン画面を表示させず自動ログインさせる
        TokenBasedRememberMeServices rememberMeServices
                = new TokenBasedRememberMeServices(WebSecurityConfig.REMEMBERME_KEY, userDetailsService);
        rememberMeServices.setCookieName("remember-me");
        Authentication rememberMeAuth = rememberMeServices.autoLogin(request, response);
        if (rememberMeAuth != null) {
            SecurityContextHolder.getContext().setAuthentication(rememberMeAuth);
            return "redirect:" + WebSecurityConfig.DEFAULT_SUCCESS_URL;
        }

        return "login";
    }

    @RequestMapping("/encode")
    @ResponseBody
    public String encode(@RequestParam String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    @RequestMapping("/loginsuccess")
    public String loginsuccess() {
        return "loginsuccess";
    }

    @RequestMapping("/urllogin")
    public String urllogin(@RequestParam String user
            , HttpServletRequest request) {
        // user パラメータで指定されたメールアドレスのユーザが user_info テーブルに存在するかチェックする
        UserInfo userInfo = userInfoDao.selectByMailAddress(user);
        if (userInfo == null) {
            throw new UsernameNotFoundException(
                    messageSource.getMessage("UserInfoUserDetailsService.usernameNotFound"
                            , null, LocaleContextHolder.getLocale()));
        }

        // user_role テーブルから設定されている権限を読み込む
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        List<UserRole> userRoleList = userRoleDao.selectByUserId(userInfo.getUserId());
        if (userRoleList != null) {
            authorities.addAll(
                    userRoleList.stream()
                            .map(userRole -> new SimpleGrantedAuthority(userRole.getRole()))
                            .collect(Collectors.toList()));
        }

        // UsernamePasswordAuthenticationToken を生成して SecurityContext にセットする
        LendingUserDetails lendingUserDetails = new LendingUserDetails(userInfo, authorities);
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(lendingUserDetails, null, authorities);
        // 下の２行は org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter の
        // setDetails メソッドを見て実装しています
        AuthenticationDetailsSource<HttpServletRequest,?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
        token.setDetails(authenticationDetailsSource.buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(token);

        return "redirect:" + WebSecurityConfig.DEFAULT_SUCCESS_URL;
    }

}
