package ksbysample.webapp.lending.helper.user;

import ksbysample.common.test.extension.db.TestDataExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserHelperTest {

    @RegisterExtension
    @Autowired
    public TestDataExtension testDataExtension;

    @Autowired
    private UserHelper userHelper;

    @Test
    void testGetApprovalMailAddrList() {
        String[] approvalMailAddrList = userHelper.getApprovalMailAddrList();
        assertThat(approvalMailAddrList).containsOnly("tanaka.taro@sample.com", "suzuki.hanako@test.co.jp");
    }

}
