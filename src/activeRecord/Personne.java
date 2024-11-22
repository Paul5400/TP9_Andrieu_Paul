package activeRecord;

import activeRecord.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Personne {
    private int id;
    private String nom;
    private String prenom;

    public Personne(String nom, String prenom) {
        this.id = -1;
        this.nom = nom;
        this.prenom = prenom;
    }

    public static List<Personne> findAll() throws SQLException {
        List<Personne> personnes = new ArrayList<>();
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM personne";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Personne p = new Personne(resultSet.getString("nom"), resultSet.getString("prenom"));
            p.id = resultSet.getInt("id");
            personnes.add(p);
        }
        return personnes;
    }

    public static Personne findById(int id) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM personne WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            Personne p = new Personne(resultSet.getString("nom"), resultSet.getString("prenom"));
            p.id = resultSet.getInt("id");
            return p;
        }
        return null;
    }

    public static List<Personne> findByName(String nom) throws SQLException {
        List<Personne> personnes = new ArrayList<>();
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM personne WHERE nom = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, nom);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Personne p = new Personne(resultSet.getString("nom"), resultSet.getString("prenom"));
            p.id = resultSet.getInt("id");
            personnes.add(p);
        }
        return personnes;
    }

    public void save() throws SQLException {
        if (this.id == -1) {
            saveNew();
        } else {
            update();
        }
    }

    private void saveNew() throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO personne (nom, prenom) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setString(1, this.nom);
        statement.setString(2, this.prenom);
        statement.executeUpdate();
        ResultSet keys = statement.getGeneratedKeys();
        if (keys.next()) {
            this.id = keys.getInt(1);
        }
    }

    private void update() throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE personne SET nom = ?, prenom = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, this.nom);
        statement.setString(2, this.prenom);
        statement.setInt(3, this.id);
        statement.executeUpdate();
    }

    public void delete() throws SQLException {
        if (this.id != -1) {
            Connection connection = DBConnection.getConnection();
            String query = "DELETE FROM personne WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, this.id);
            statement.executeUpdate();
            this.id = -1;
        }
    }
}
