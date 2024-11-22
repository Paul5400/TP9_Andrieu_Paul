import activeRecord.DBConnection;
import activeRecord.Personne;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPersonne {

    @BeforeAll
    public static void setUp() throws SQLException {
        Connection connection = DBConnection.getConnection();
        String createTable = """
                CREATE TABLE IF NOT EXISTS personne (
                id INTEGER AUTO_INCREMENT PRIMARY KEY,
                nom VARCHAR(50) NOT NULL,
                prenom VARCHAR(50) NOT NULL
                )
                """;
        connection.createStatement().executeUpdate(createTable);
    }

    @AfterAll
    public static void teardownDatabase() throws SQLException {
        // Supprimer la table après les tests
        Connection connection = DBConnection.getConnection();
        String dropTableQuery = "DROP TABLE IF EXISTS personne;";
        connection.createStatement().executeUpdate(dropTableQuery);
    }

    @BeforeEach
    public void populateDatabase() throws SQLException {
        Connection connection = DBConnection.getConnection();
        String deleteDataQuery = "DELETE FROM personne;";
        connection.createStatement().executeUpdate(deleteDataQuery);

        String insertDataQuery = """
            INSERT INTO personne (nom, prenom) VALUES 
            ('Tangigui', 'Th'),
            ('St', 'Binho'),
            ('Desko', 'Go');
            """;
        connection.createStatement().executeUpdate(insertDataQuery);
    }


    @Test
    public void testFindAll() throws SQLException {
        List<Personne> personnes = Personne.findAll();
        assertEquals(3, personnes.size(), "La table doit avoir 3 personnes");
    }

    @Test
    public void testFindById() throws SQLException {
        Personne personne = Personne.findById(1);
        assertNotNull(personne, "Une personne avec l'ID 1 devrait exister");
        assertEquals("Tangigui", personne.getNom(), "Le nom de la personne avec l'ID 1 doit être 'Tangigui'");
        assertEquals("Th", personne.getPrenom(), "Le prénom de la personne avec l'ID 1 doit être 'Th'");

        Personne nonExistent = Personne.findById(999);
        assertNull(nonExistent, "Aucune personne ne devrait exister avec un ID inexistant");
    }

    @Test
    public void testFindByName() throws SQLException {
        List<Personne> personnes = Personne.findByName("Tangigui");
        assertEquals(1, personnes.size(), "Il devrait y avoir 1 personne avec le nom 'Tangigui'");

        personnes = Personne.findByName("St");
        assertEquals(1, personnes.size(), "Il devrait y avoir 1 personne avec le nom 'St'");
    }

    @Test
    public void testUdpatePersonne() throws SQLException {
        Personne personne = Personne.findById(1);
        assertNotNull(personne, "Une personne avec l'ID 1 devrait exister");

        personne.setNom("Tangigui");
        personne.save();

        Personne updatedPersonne = Personne.findById(1);
        assertNotNull(updatedPersonne, "Une personne avec l'ID 1 devrait exister");
        assertEquals("UpdatedName",updatedPersonne.getNom(),"Le nom devrait être mis à jour");

    }

    @Test
    public void testDeletePersonne() throws SQLException {
        Personne personne = Personne.findById(2);
        assertNotNull(personne, "Une personne avec l'ID 2 devrait exister");

        personne.delete();

        Personne deletedPerson = Personne.findById(2);
        assertNull(deletedPerson, "La personne supprimée ne devrait plus exister");

        List<Personne> personnes = Personne.findAll();
        assertEquals(2, personnes.size(), "La table devrait contenir 2 personnes après la suppression");
    }
}

