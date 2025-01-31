package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class Util {
    private static final String CONFIG_FILE = "application.properties";
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    private static SessionFactory sessionFactory;

    static {
        loadDatabaseProperties();
        configureHibernate();
    }

    private static void loadDatabaseProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = Util.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                throw new IOException("Файл " + CONFIG_FILE + " не найден в classpath!");
            }
            properties.load(inputStream);

            URL = properties.getProperty("db.url");
            USER = properties.getProperty("db.user");
            PASSWORD = properties.getProperty("db.password");

            log.info("✅ Конфигурация загружена: URL={}, USER={}", URL, USER);
        } catch (IOException e) {
            log.error("❌ Ошибка загрузки файла конфигурации: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка загрузки конфигурации базы данных", e);
        }
    }

    public static Connection getConnection() {
        try {
            log.info("🔄 Попытка подключения к базе данных...");
            log.debug("Используемый URL: {}", URL);
            log.debug("Используемый USER: {}", USER);
            log.debug("Используемый PASSWORD: {}", PASSWORD); // Убедись, что здесь правильный пароль

            // Загрузка драйвера PostgreSQL
            Class.forName("org.postgresql.Driver");

            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            log.info("✅ Подключение к базе данных установлено успешно!");
            return connection;
        } catch (ClassNotFoundException e) {
            log.error("❌ Драйвер PostgreSQL не найден!", e);
            throw new RuntimeException("Драйвер PostgreSQL не найден! Проверьте зависимости.", e);
        } catch (SQLException e) {
            log.error("❌ Ошибка подключения к базе данных: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось подключиться к базе данных. Проверьте параметры подключения.", e);
        }
    }

    // Кнфиг без XML как требуется в задачке
    public static void configureHibernate(){
        try{
            Properties settings = new Properties();
            settings.put(Environment.DRIVER, "org.postgresql.Driver");
            settings.put(Environment.URL, URL);
            settings.put(Environment.USER, USER);
            settings.put(Environment.PASS, PASSWORD);
            settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
            settings.put(Environment.SHOW_SQL, "true");
            settings.put(Environment.HBM2DDL_AUTO, "update"); // создаст базу если ее нету 

            // Сущность юзера добавляем и настройки передаем
            Configuration configuration = new Configuration();
            configuration.setProperties(settings);
            configuration.addAnnotatedClass(User.class);

            sessionFactory = configuration.buildSessionFactory();
            log.info("✅ Hibernate SessionFactory успешно сконфигурирован!");

        }catch (Exception e){
            log.error("❌ Ошибка конфигурации Hibernate: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка конфигурации Hibernate", e);
        }
    }
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
