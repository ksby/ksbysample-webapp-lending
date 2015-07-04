package ksbysample.common.test;

import org.junit.rules.ExternalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Component
public class SecurityMockMvcResource extends ExternalResource {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    public MockMvc auth;

    public MockMvc nonauth;

    @Override
    protected void before() throws Throwable {
        // 認証ユーザ用MockMvc ( user = test, role = USER )
        this.auth = MockMvcBuilders.webAppContextSetup(this.context)
                .defaultRequest(get("/").with(user("test").roles("USER")))
                .addFilter(springSecurityFilterChain)
                .build();

        // 非認証用MockMvc
        this.nonauth = MockMvcBuilders.webAppContextSetup(this.context)
                .addFilter(springSecurityFilterChain)
                .build();
    }

}
