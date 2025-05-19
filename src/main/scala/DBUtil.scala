import java.sql.{Connection, DriverManager}

object DBUtil {
  // === Database Config ===
  val url      = "jdbc:oracle:thin:@//localhost:1521/XE"
  val user     = "ruleEngine"
  val password = "1234"

  try {
    Class.forName("oracle.jdbc.driver.OracleDriver")
  } catch {
    case e: ClassNotFoundException =>
      throw new RuntimeException("Oracle JDBC Driver not found. Make sure ojdbc8.jar is on the classpath.", e)
  }

  def getConnection(): Connection = {
    DriverManager.getConnection(url, user, password)
  }


}


