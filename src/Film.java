import activeRecord.DBConnection;
import activeRecord.Personne;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Film {
    private int id;
    private String titre;
    private int id_real;

    public Film(String titre, Personne realisateur) throws SQLException, RealisateurAbsentException {
        if (realisateur == null || realisateur.getId() == -1) {
            throw new RealisateurAbsentException("Realisateur absent ou non enregistre.");
        }
        this.titre = titre;
        this.id_real = realisateur.getId();
        this.id = -1;
    }

    public static Film findById(int id) throws SQLException, RealisateurAbsentException {
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM film WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            Film f = new Film(resultSet.getString("titre"), null);
            f.id = resultSet.getInt("id");
            f.id_real = resultSet.getInt("id_real");
            return f;
        }
        return null;
    }

    public Personne getRealisateur() throws SQLException {
        return Personne.findById(this.id_real);
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
        String query = "INSERT INTO film (titre, id_real) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setString(1, this.titre);
        statement.setInt(2, this.id_real);
        statement.executeUpdate();
        ResultSet keys = statement.getGeneratedKeys();
        if (keys.next()) {
            this.id = keys.getInt(1);
        }
    }

    private void update() throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE film SET titre = ?, id_real = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, this.titre);
        statement.setInt(2, this.id_real);
        statement.setInt(3, this.id);
        statement.executeUpdate();
    }

    public void delete() throws SQLException {
        if (this.id != -1) {
            Connection connection = DBConnection.getConnection();
            String query = "DELETE FROM film WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, this.id);
            statement.executeUpdate();
            this.id = -1;
        }
    }
}
