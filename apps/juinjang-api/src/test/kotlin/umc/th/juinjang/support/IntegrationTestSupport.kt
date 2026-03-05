package umc.th.juinjang.support

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import umc.th.juinjang.testcontainers.MySqlTestContainersConfig

@ActiveProfiles("test")
@SpringBootTest
@Import(MySqlTestContainersConfig::class)
abstract class IntegrationTestSupport
