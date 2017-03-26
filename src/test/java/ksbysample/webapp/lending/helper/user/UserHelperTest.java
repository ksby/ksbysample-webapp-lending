package ksbysample.webapp.lending.helper.user;

import ksbysample.common.test.rule.db.TestDataResource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
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