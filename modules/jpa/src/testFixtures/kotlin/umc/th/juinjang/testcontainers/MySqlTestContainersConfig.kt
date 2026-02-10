package umc.th.juinjang.testcontainers

import org.springframework.context.annotation.Configuration
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.DockerImageName

@Configuration
class MySqlTestContainersConfig {
    companion object {
        private val mySqlContainer: MySQLContainer<*> = MySQLContainer(DockerImageName.parse("mysql:8.0"))
            .apply {
                withDatabaseName("juinjang")
                withUsername("test")
                withPassword("test")
                withCommand(
                    "--character-set-server=utf8mb4",
                    "--collation-server=utf8mb4_general_ci",
                    "--skip-character-set-client-handshake",
                )
                start()
            }

        init {
            val mySqlJdbcUrl = mySqlContainer.let { "jdbc:mysql://${it.host}:${it.firstMappedPort}/${it.databaseName}" }
            System.setProperty("spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver")
            System.setProperty("spring.datasource.url", mySqlJdbcUrl)
            System.setProperty("spring.datasource.username", mySqlContainer.username)
            System.setProperty("spring.datasource.password", mySqlContainer.password)
        }
    }
}
