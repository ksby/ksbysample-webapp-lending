package ksbysample.webapp.lending

import groovy.sql.Sql
import ksbysample.common.test.extension.db.BaseTestData
import ksbysample.common.test.extension.db.TestDataExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import javax.sql.DataSource

class SampleTest {

    // テストクラス内で共通で使用する定数を定義する
    class TestConst {
        static def TEST_ROLE_01 = "ROLE_USER"
        static def TEST_ROLE_02 = "ROLE_ADMIN"
    }

    @Nested
    @SpringBootTest
    // @BaseTestData は TestDataResource クラスが使用する自作のアノテーション
    @BaseTestData("testdata/base")
    class テストクラス {

        @Autowired
        private DataSource dataSource

        // 自作のテーブルのバックアップ・リストア用クラス
        @RegisterExtension
        @Autowired
        public TestDataExtension testDataExtension

        Sql sql

        @BeforeEach
        void before() {
            sql = new Sql(dataSource)
        }

        @AfterEach
        void after() {
            sql.close()
        }

        @Test
        void "Groovy + JUnit5 + Groovy SQL のテストサンプル１"() {
            setup: "データを追加する"
            sql.execute("INSERT INTO user_role(role_id, user_id, role) VALUES (?, ?, ?)"
                    , [100, 1, TestConst.TEST_ROLE_01])

            expect: "データが追加されているか確認する"
            def row = sql.firstRow("SELECT * FROM user_role WHERE role_id = 100 AND user_id = 1")
            assert row.role == "ROLE_USER"
        }

        @Test
        void "Groovy + JUnit5 + Groovy SQL のテストサンプル２"() {
            setup: "データを３件追加する"
            sql.withBatch("INSERT INTO user_role(role_id, user_id, role) VALUES (?, ?, ?)") {
                it.addBatch([100, 6, TestConst.TEST_ROLE_01])
                it.addBatch([101, 7, TestConst.TEST_ROLE_01])
                it.addBatch([102, 7, TestConst.TEST_ROLE_02])
            }

            expect: "追加されたデータをチェックする(カラムは全て取得するが role カラムだけチェックする)"
            def rows = sql.rows("SELECT * FROM user_role WHERE user_id IN (6, 7) ORDER BY role_id")
            assert rows.role == ["ROLE_USER", "ROLE_USER", "ROLE_ADMIN"]

            and: "追加されたデータをチェックする(role_id, role の２カラムのみ取得してチェックする)"
            rows = sql.rows("""\
                SELECT
                  role_id,
                  role
                FROM user_role
                WHERE user_id IN (6, 7)
                ORDER BY role_id
            """)
            assert rows == [[role_id: 100, role: "ROLE_USER"]
                            , [role_id: 101, role: "ROLE_USER"]
                            , [role_id: 102, role: "ROLE_ADMIN"]]
        }

    }

}
