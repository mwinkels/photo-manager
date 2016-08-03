package nl.mwinkels.photomanager.config;


import nl.mwinkels.photomanager.dao.PhotoDao;
import nl.mwinkels.photomanager.data.DetectedFace;
import nl.mwinkels.photomanager.data.Photo;
import nl.mwinkels.photomanager.data.RotationSuggestion;
import nl.mwinkels.photomanager.data.Suggestion;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    @Bean
    @DependsOn("photoPath")
    public DataSource dataSource(@Value("${dbPath:./.db/}") String dbPath) {
        return new SingleConnectionDataSource("jdbc:hsqldb:file:" + dbPath + "photos", true);
    }

    @Bean
    public SessionFactory sessionFactory(DataSource dataSource) {
        return new LocalSessionFactoryBuilder(dataSource)
                .addAnnotatedClasses(Photo.class, DetectedFace.class, Suggestion.class, RotationSuggestion.class)
                .buildSessionFactory();
    }

    @Bean
    public PlatformTransactionManager transactionManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }

    @Bean
    public PhotoDao photoDao(SessionFactory sessionFactory) {
        return new PhotoDao(sessionFactory);
    }
}
