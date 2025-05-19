import java.util.logging.{FileHandler, Logger, SimpleFormatter}

object LoggerUtil {
  val logger: Logger = Logger.getLogger("RulesEngineLogger")
  private val fileHandler = new FileHandler("rules_engine.log", true)
  fileHandler.setFormatter(new SimpleFormatter())
  logger.addHandler(fileHandler)
  logger.setUseParentHandlers(false)
}


