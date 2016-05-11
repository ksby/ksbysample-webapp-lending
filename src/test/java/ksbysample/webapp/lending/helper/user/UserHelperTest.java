package ksbysample.webapp.lending.helper.user;

import ksbysample.common.test.rule.db.TestDataResource;
import ksbysample.webapp.lending.Application;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class UserHelperTest {

    @Rule
    @Autowired
    public TestDataResource testDataResource;

    @Autowired
    private UserHelper userHelper;
    
    @Test
    public void testGetApprovalMailAddrList() throws Exception {
        String[] approvalMailAddrList = userHelper.getApprovalMailAddrList();
        assertThat(approvalMailAddrList).containsOnly("tanaka.taro@sample.com", "suzuki.hanako@test.co.jp");
    }
}