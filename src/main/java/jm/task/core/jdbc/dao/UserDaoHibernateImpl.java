package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
// Тут будет реализация

@Slf4j
@NoArgsConstructor
public class UserDaoHibernateImpl implements UserDao {

    @Override
    public void createUsersTable() {
        //Таблица сама создается,что указано в конфигурации Hibernate в Util settings.put(Environment.HBM2DDL_AUTO, "update");
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createNativeQuery("DROP TABLE IF EXISTS users CASCADE").executeUpdate();
            session.getTransaction().commit();
            System.out.println("✅ Таблица users удалена!");
        } catch (Exception e) {
            throw new RuntimeException("❌ Ошибка при удалении таблицы пользователей!", e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(new User(name, lastName, age));
            session.getTransaction().commit();
            System.out.println("✅ Пользователь сохранен: " + name + " " + lastName);
        } catch (Exception e) {
            throw new RuntimeException("❌ Ошибка при сохранении пользователя!", e);
        }
    }


    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) session.remove(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("❌ Ошибка при удалении пользователя с id: " + id, e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = Util.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            throw new RuntimeException("❌ Ошибка при получении списка пользователей!", e);
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("❌ Ошибка при очистке таблицы пользователей!", e);
        }
    }

}
