package activeRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class PrincipaleJDBC {

    // IL FAUT PENSER A AJOUTER MYSQL CONNECTOR AU CLASSPATH

    public static void main(String[] args) {

        // variables de connection
        String userName = "root";
        String password = "";
        String serverName = "127.0.0.1";
        //String portNumber = "3306";
        String portNumber = "3306";
        String tableName = "personne";

        // il faut une base nommee testPersonne !
        String dbName = "testpersonne";

        try {
            // chargement du driver jdbc
            Class.forName("com.mysql.cj.jdbc.Driver");

            // creation de la connection
            Properties connectionProps = new Properties();
            connectionProps.put("user", userName);
            connectionProps.put("password", password);
            String urlDB = "jdbc:mysql://" + serverName + ":";
            urlDB += portNumber + "/" + dbName;
            System.out.println(urlDB);
            Connection connect = DriverManager.getConnection(urlDB, connectionProps);
            //Connection connect = DriverManager.getConnection("jdbc:mysql://db4free.net/testpersonne","scruzlara", "root2014");
            // creation de la table Personne
            String createString = "CREATE TABLE Personne ( "
                    + "ID INTEGER  AUTO_INCREMENT, " + "NOM varchar(40) NOT NULL, "
                    + "PRENOM varchar(40) NOT NULL, " + "PRIMARY KEY (ID))";
            Statement stmt = connect.createStatement();
            stmt.executeUpdate(createString);

            // ajout de personne avec requete preparee
            String SQLPrep = "INSERT INTO Personne (nom, prenom) VALUES (?,?);";
            PreparedStatement prep;
            //l'option RETURN_GENERATED_KEYS permet de recuperer l'id
            prep = connect.prepareStatement(SQLPrep,
                    Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, "Steven");
            prep.setString(2, "Spielberg");
            prep.executeUpdate();

            SQLPrep = "INSERT INTO Personne (nom, prenom) VALUES (?,?);";
            //l'option RETURN_GENERATED_KEYS permet de récupérer l'id auto increment
            prep = connect.prepareStatement(SQLPrep,
                    Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, "Ridley");
            prep.setString(2, "Scott");
            prep.executeUpdate();

            // recuperation de la derniere ligne ajoutée (auto increment)
            // recupere le nouvel id
            int autoInc = -1;
            ResultSet rs = prep.getGeneratedKeys();
            if (rs.next()) {
                autoInc = rs.getInt(1);
            }
            System.out.println("**** id utilise lors de l'ajout ****");
            System.out.println(autoInc);


            // récupération de toutes les personnes + affichage
            System.out.println("***** AFFICHE TOUTES PERSONNES ***** ");
            SQLPrep = "SELECT * FROM Personne;";
            PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
            prep1.execute();
            rs = prep1.getResultSet();
            // s'il y a un resultat
            while (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                int id = rs.getInt("id");
                System.out.println("-> (" + id + ") " + nom + ", " + prenom);
            }

            // suppression de la personne 1
            prep = connect.prepareStatement("DELETE FROM Personne WHERE id=?");
            prep.setInt(1, 1);
            prep.execute();

            // recuperation de la seconde personne + affichage
            System.out.println("***** AFFICHE PERSONNE 2***** ");
            SQLPrep = "SELECT * FROM Personne WHERE id=?;";
            prep1 = connect.prepareStatement(SQLPrep);
            prep1.setInt(1, 2);
            prep1.execute();
            rs = prep1.getResultSet();
            // s'il y a un resultat
            if (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                int id = rs.getInt("id");
                System.out.println("-> (" + id + ") " + nom + ", " + prenom);
            }

            // met � jour personne 2
            String SQLprep = "update Personne set nom=?, prenom=? where id=?;";
            prep1 = connect.prepareStatement(SQLprep);
            prep1.setString(1, "R_i_d_l_e_y");
            prep1.setString(2, "S_c_o_t_t");
            prep1.setInt(3, 2);
            prep1.execute();

            // recuperation de la seconde personne + affichage
            System.out.println("***** AFFICHE PERSONNE 2 après modif***** ");
            SQLPrep = "SELECT * FROM Personne WHERE id=?;";
            prep1 = connect.prepareStatement(SQLPrep);
            prep1.setInt(1, 2);
            prep1.execute();
            rs = prep1.getResultSet();
            // s'il y a un resultat
            if (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                int id = rs.getInt("id");
                System.out.println("-> (" + id + ") " + nom + ", " + prenom);
            }

            // suppression de la table personne
            String drop = "DROP TABLE Personne";
            stmt = connect.createStatement();
            stmt.executeUpdate(drop);

        } catch (SQLException e) {
            System.out.println("*** ERREUR SQL ***");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("*** ERREUR lors du chargement du driver ***");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("*** ERREUR inconnue... ***");
            e.printStackTrace();
        }
    }
}