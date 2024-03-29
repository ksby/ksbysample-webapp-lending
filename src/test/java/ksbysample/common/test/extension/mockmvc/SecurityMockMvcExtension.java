package ksbysample.common.test.extension.mockmvc;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Component
@ConditionalOnWebApplication
public class SecurityMockMvcExtension implements BeforeEachCallback {

    public final String MAILADDR_TANAKA_TARO = "tanaka.taro@sample.com";
    public final String MAILADDR_SUZUKI_HANAKO = "suzuki.hanako@test.co.jp";
    public final String MAILADDR_KIMURA_MASAO = "kimura.masao@test.co.jp";
    public final String MAILADDR_ENDO_YOKO = "endo.yoko@sample.com";
    public final String MAILADDR_SATO_MASAHIKO = "sato.masahiko@sample.com";
    public final String MAILADDR_TAKAHASI_NAOKO = "takahasi.naoko@test.co.jp";
    public final String MAILADDR_ITO_AOI = "ito.aoi@test.co.jp";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserDetailsService userDetailsService;

    public MockMvc authTanakaTaro;
    public MockMvc authSuzukiHanako;
    public MockMvc authItoAoi;
    public MockMvc noauth;

    @Override
    public void beforeEach(ExtensionContext context) {
        // 認証ユーザ用MockMvc ( user = tanaka.taro@sample.com )
        UserDetails userDetailsTanakaTaro = userDetailsService.loadUserByUsername(MAILADDR_TANAKA_TARO);
        this.authTanakaTaro = MockMvcBuilders.webAppContextSetup(this.context)
                .defaultRequest(get("/").with(user(userDetailsTanakaTaro)))
                .apply(springSecurity())
                .build();

        // 認証ユーザ用MockMvc ( user = suzuki.hanako@test.co.jp )
        UserDetails userDetailsSuzukiHanako = userDetailsService.loadUserByUsername(MAILADDR_SUZUKI_HANAKO);
        this.authSuzukiHanako = MockMvcBuilders.webAppContextSetup(this.context)
                .defaultRequest(get("/").with(user(userDetailsSuzukiHanako)))
                .apply(springSecurity())
                .build();

        // 認証ユーザ用MockMvc ( user = ito.aoi@test.co.jp )
        UserDetails userDetailsItoAoi = userDetailsService.loadUserByUsername(MAILADDR_ITO_AOI);
        this.authItoAoi = MockMvcBuilders.webAppContextSetup(this.context)
                .defaultRequest(get("/").with(user(userDetailsItoAoi)))
                .apply(springSecurity())
                .build();

        // 非認証ユーザ用MockMvc
        this.noauth = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();
    }

}
