import java.sql.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
void main() {
    Scanner sc = new Scanner(System.in);

    String urlArcheologie = "jdbc:mysql://localhost/archeologie";
    String usernameArcheologie = "root";
    String passwordArcheologie = "";

    try (Connection conn = DriverManager.getConnection(urlArcheologie, usernameArcheologie, passwordArcheologie)) {
        System.out.println("Připojení bylo úspěšné.");

        String SELECTNalezy = "SELECT Nazev,Material,Stari FROM nalezy ORDER BY Stari DESC";

        try (PreparedStatement stmt = conn.prepareStatement(SELECTNalezy);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String natev = rs.getString("Nazev");
                String material = rs.getString("Material");
                String stari = rs.getString("Stari");
                System.out.println(natev + ", " + material + ", " + stari);
            }
        }


        System.out.println("=====Vyhledani pokladu=====");
        System.out.print("Material: ");
        String material = sc.nextLine();
        System.out.print("Min. hodnota pokladu: ");
        int minHodnota = sc.nextInt();
        sc.nextLine();

        String SELECTPoklady = "SELECT Nazev,Hodnota FROM nalezy WHERE Material=? AND Hodnota>=?";

        try (PreparedStatement stmt = conn.prepareStatement(SELECTPoklady)) {
            stmt.setString(1, material);
            stmt.setInt(2, minHodnota);

            try (ResultSet rs = stmt.executeQuery()){
                if (rs == null) {
                    System.out.println("Zadne vysledky nebyly nalezeny.");
                }else {
                    while (rs.next()) {
                        String nazev = rs.getString("Nazev");
                        String hodnota = rs.getString("Hodnota");
                        System.out.println(nazev + " " + material + " " + hodnota);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Problem s daty");
            }
        }catch (SQLException e){
            throw new RuntimeException("Problem s dotazem");
        }


        System.out.println("=====Vyhledani lokality=====");
        System.out.print("Stat: ");
        String stat = sc.nextLine();

        String SELECTLokality= "SELECT nalezy.Nazev,lokality.Nazev,nalezy.Stari FROM nalezy INNER JOIN lokality ON nalety.Id_lokality = lokality.Id_lokality WHERE lokality.Stat=? GROUP BY lokality.Stat";

        try (PreparedStatement stmt = conn.prepareStatement(SELECTLokality)) {
            stmt.setString(1, stat);
            try (ResultSet rs = stmt.executeQuery()){
                if (rs == null) {
                    System.out.println("Zadne vysledky nebyly nalezeny.");
                }else {
                    while (rs.next()) {
                        String nazevNalezu = rs.getString("nalezy.Nazev");
                        String nazevLokality = rs.getString("lokality.Nazev");
                        int stari = rs.getInt("nazely.Stari");
                        System.out.println(nazevNalezu + " " + nazevLokality + " " + stari);
                    }
                }
            }catch (SQLException e) {
                throw new RuntimeException("Problem s daty");
            }
        }catch (SQLException e){
            throw new RuntimeException("Problem s dotazem");
        }

        System.out.println("=====Pocet nalezu podle materialu=====");
        String SELECTNalezyPodelMaterialu = "SELECT Material,count(*) FROM nalezy GROUP BY Material";
        try (PreparedStatement stmt = conn.prepareStatement(SELECTNalezyPodelMaterialu);
             ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                String materialP = rs.getString("Material");
                int pocet = rs.getInt("COUNT(*)");
                System.out.println(materialP + ": "+ pocet);
            }
        }catch (SQLException e) {
            throw new RuntimeException("Problem s dotazem");
        }


    } catch (SQLException e) {
        throw new RuntimeException("Nepodarilo se propojit s databazi.");
    }
}
