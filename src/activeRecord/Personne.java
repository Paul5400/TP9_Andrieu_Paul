package activeRecord;

import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class Personne {

    private String nom;
    private String prenom;
    private int id;

    public Personne(String nom, String prenom) throws SQLException {
        this.nom = nom;
        this.id = -1;
        this.prenom = prenom;
    }

    public static List<Personne> findAll() throws SQLException {
        List<Personne> personnes = new ArrayList<Personne>();
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM personne";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
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

    public void saveNew() throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO personne(nom, prenom) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, nom);
        statement.setString(2, prenom);
        statement.executeUpdate();
        ResultSet keys = statement.getGeneratedKeys();
        while (keys.next()) {
            this.id = keys.getInt(1);
        }

    }

    public void update() throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE personne SET prenom = ?, nom = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, this.prenom);
        statement.setString(2,this.prenom);
        statement.setInt(3, this.id);
        statement.executeUpdate();

    }

    public void delete() throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "DELETE FROM personne WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, this.id);
        statement.executeUpdate();
        this.id =-1;
    }

}
